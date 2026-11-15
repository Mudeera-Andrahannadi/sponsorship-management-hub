package com.sponsorhub.service;

import com.sponsorhub.dto.CompanyView;
import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import com.sponsorhub.entity.User;
import com.sponsorhub.repository.CommitteeRepository;
import com.sponsorhub.repository.CompanyRepository;
import com.sponsorhub.repository.UserRepository;
import com.sponsorhub.security.Me;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LockService
{
    private final CompanyRepository compRepo;
    private final UserRepository userRepo;
    private final CommitteeRepository teamRepo;
    private final Rules rules;

    public LockService(CompanyRepository compRepo, UserRepository userRepo, CommitteeRepository teamRepo, Rules rules)
    {
        this.compRepo = compRepo;
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
        this.rules = rules;
    }

    @Transactional
    public List<CompanyView> list(Me me, String field, String status)
    {
        LocalDateTime now = LocalDateTime.now();
        compRepo.freeExpired(now);
        List<Company> found = compRepo.findAll().stream()
            .filter(item -> field == null || field.equalsIgnoreCase(item.getField()))
            .filter(item -> status == null || status.equalsIgnoreCase(item.getStatus().label()))
            .sorted(Comparator.comparing(Company::getName, String.CASE_INSENSITIVE_ORDER))
            .toList();
        Map<Integer, String> names = holders(found);
        return found.stream().map(item -> view(item, names, me, now)).toList();
    }

    @Transactional
    public CompanyView claim(Integer compId, Me me)
    {
        if (!teamRepo.hasActive(me.userId()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only active committee members can claim a company");
        }
        Company comp = find(compId);
        LocalDateTime now = LocalDateTime.now();
        if (compRepo.held(me.userId(), now) >= rules.maxHolds())
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "You are already holding " + rules.maxHolds() + " companies. Release one before claiming another");
        }
        if (compRepo.claim(compId, me.userId(), now.plusDays(rules.holdFor(comp)), now) == 0)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, taken(find(compId)));
        }
        return one(find(compId), me, now);
    }

    @Transactional
    public CompanyView release(Integer compId, Me me)
    {
        find(compId);
        if (compRepo.release(compId, me.userId()) == 0)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are not holding this company");
        }
        return one(find(compId), me, LocalDateTime.now());
    }

    public CompanyView show(Integer compId, Me me)
    {
        return one(find(compId), me, LocalDateTime.now());
    }

    public Company find(Integer compId)
    {
        return compRepo.findById(compId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such company"));
    }

    private String taken(Company comp)
    {
        String until = comp.getLockUntil() == null ? "" : " until " + comp.getLockUntil().toLocalDate();
        if (comp.getStatus() == Light.Locked)
        {
            return "This company is in its cooldown period" + until;
        }
        return "Another society is already talking to this company" + until;
    }

    private CompanyView one(Company comp, Me me, LocalDateTime now)
    {
        return view(comp, holders(List.of(comp)), me, now);
    }

    private Map<Integer, String> holders(List<Company> comps)
    {
        Set<Integer> ids = comps.stream().map(Company::getLockBy).filter(Objects::nonNull).collect(Collectors.toSet());
        return userRepo.findAllById(ids).stream().collect(Collectors.toMap(User::getUserId, User::getName));
    }

    private CompanyView view(Company comp, Map<Integer, String> names, Me me, LocalDateTime now)
    {
        Long left = null;
        if (comp.getLockUntil() != null)
        {
            left = Math.max(0L, (Duration.between(now, comp.getLockUntil()).toMinutes() + 1439) / 1440);
        }
        boolean mine = me.userId().equals(comp.getLockBy());
        return new CompanyView(comp.getCompId(), comp.getName(), comp.getContact(), comp.getJob(), comp.getField(),
            comp.getEmail(), comp.getPhone(), comp.getStatus().label(), comp.getLockUntil(), left,
            names.get(comp.getLockBy()), mine, rules.holdFor(comp), comp.getLockDays(), comp.getCoolDays());
    }
}
