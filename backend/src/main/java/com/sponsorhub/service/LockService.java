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
    private final CompanyRepository comp_repo;
    private final UserRepository user_repo;
    private final CommitteeRepository team_repo;
    private final Rules rules;

    public LockService(CompanyRepository comp_repo, UserRepository user_repo, CommitteeRepository team_repo, Rules rules)
    {
        this.comp_repo = comp_repo;
        this.user_repo = user_repo;
        this.team_repo = team_repo;
        this.rules = rules;
    }

    @Transactional
    public List<CompanyView> list(Me me, String field, String status)
    {
        LocalDateTime now = LocalDateTime.now();
        comp_repo.freeExpired(now);
        List<Company> found = comp_repo.findAll().stream()
            .filter(item -> field == null || field.equalsIgnoreCase(item.getField()))
            .filter(item -> status == null || status.equalsIgnoreCase(item.getStatus().label()))
            .sorted(Comparator.comparing(Company::getName, String.CASE_INSENSITIVE_ORDER))
            .toList();
        Map<Integer, String> names = holders(found);
        return found.stream().map(item -> view(item, names, me, now)).toList();
    }

    @Transactional
    public CompanyView claim(Integer comp_id, Me me)
    {
        if (!team_repo.hasActive(me.user_id()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only active committee members can claim a company");
        }
        Company comp = find(comp_id);
        LocalDateTime now = LocalDateTime.now();
        if (comp_repo.claim(comp_id, me.user_id(), now.plusDays(rules.holdFor(comp)), now) == 0)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, taken(find(comp_id)));
        }
        return one(find(comp_id), me, now);
    }

    @Transactional
    public CompanyView release(Integer comp_id, Me me)
    {
        find(comp_id);
        if (comp_repo.release(comp_id, me.user_id()) == 0)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are not holding this company");
        }
        return one(find(comp_id), me, LocalDateTime.now());
    }

    public CompanyView show(Integer comp_id, Me me)
    {
        return one(find(comp_id), me, LocalDateTime.now());
    }

    public Company find(Integer comp_id)
    {
        return comp_repo.findById(comp_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such company"));
    }

    private String taken(Company comp)
    {
        String until = comp.getLock_until() == null ? "" : " until " + comp.getLock_until().toLocalDate();
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
        Set<Integer> ids = comps.stream().map(Company::getLock_by).filter(Objects::nonNull).collect(Collectors.toSet());
        return user_repo.findAllById(ids).stream().collect(Collectors.toMap(User::getUser_id, User::getName));
    }

    private CompanyView view(Company comp, Map<Integer, String> names, Me me, LocalDateTime now)
    {
        Long left = null;
        if (comp.getLock_until() != null)
        {
            left = Math.max(0L, (Duration.between(now, comp.getLock_until()).toMinutes() + 1439) / 1440);
        }
        boolean mine = me.user_id().equals(comp.getLock_by());
        return new CompanyView(comp.getComp_id(), comp.getName(), comp.getContact(), comp.getJob(), comp.getField(),
            comp.getEmail(), comp.getPhone(), comp.getStatus().label(), comp.getLock_until(), left,
            names.get(comp.getLock_by()), mine);
    }
}
