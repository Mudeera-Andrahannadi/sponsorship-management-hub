package com.sponsorhub.service;

import com.sponsorhub.dto.HistoryRow;
import com.sponsorhub.dto.SponsorForm;
import com.sponsorhub.dto.UpdateForm;
import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import com.sponsorhub.entity.Outcome;
import com.sponsorhub.entity.Role;
import com.sponsorhub.entity.Sponsorship;
import com.sponsorhub.repository.CommitteeRepository;
import com.sponsorhub.repository.SponsorshipRepository;
import com.sponsorhub.security.Me;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SponsorshipService
{
    private final SponsorshipRepository dealRepo;
    private final CommitteeRepository teamRepo;
    private final LockService locks;
    private final Rules rules;

    public SponsorshipService(SponsorshipRepository dealRepo, CommitteeRepository teamRepo, LockService locks, Rules rules)
    {
        this.dealRepo = dealRepo;
        this.teamRepo = teamRepo;
        this.locks = locks;
        this.rules = rules;
    }

    @Transactional
    public Integer log(SponsorForm form, Me me)
    {
        if (!teamRepo.isActiveIn(me.userId(), form.eventId()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not on that event's committee");
        }
        Company comp = locks.find(form.compId());
        LocalDateTime now = LocalDateTime.now();
        if (!holds(comp, me, now))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Claim this company first. Your hold may have run out");
        }
        Sponsorship deal = new Sponsorship();
        deal.setCompId(form.compId());
        deal.setEventId(form.eventId());
        deal.setHandler(me.userId());
        deal.setAmount(form.amount());
        deal.setNotes(form.notes());
        deal.setStatus(Outcome.Pending);
        deal.setAskedOn(LocalDate.now());
        comp.setLockUntil(now.plusDays(rules.holdFor(comp)));
        return dealRepo.save(deal).getDealId();
    }

    @Transactional
    public void update(Integer dealId, UpdateForm form, Me me)
    {
        Sponsorship deal = dealRepo.findById(dealId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such sponsorship"));
        boolean admin = me.role() == Role.Admin;
        if (form.status() != null)
        {
            decide(deal, form.status(), admin);
        }
        if (form.amount() != null || form.notes() != null)
        {
            edit(deal, form, me, admin);
        }
    }

    public List<HistoryRow> history(Integer compId, Integer eventId)
    {
        return dealRepo.rows(compId, eventId);
    }

    private boolean holds(Company comp, Me me, LocalDateTime now)
    {
        return comp.getStatus() == Light.In_Progress && me.userId().equals(comp.getLockBy())
            && comp.getLockUntil() != null && comp.getLockUntil().isAfter(now);
    }

    private boolean heldByOther(Company comp, Sponsorship deal)
    {
        return comp.getStatus() != Light.Available && comp.getLockUntil() != null
            && comp.getLockUntil().isAfter(LocalDateTime.now()) && !deal.getHandler().equals(comp.getLockBy());
    }

    private void decide(Sponsorship deal, Outcome result, boolean admin)
    {
        if (!admin)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the faculty admin can approve or reject");
        }
        if (result == Outcome.Pending)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose Approved or Rejected");
        }
        if (deal.getStatus() != Outcome.Pending)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This request was already decided");
        }
        deal.setStatus(result);
        settle(locks.find(deal.getCompId()), deal, result);
    }

    private void settle(Company comp, Sponsorship deal, Outcome result)
    {
        if (result == Outcome.Approved)
        {
            if (heldByOther(comp, deal))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Another society holds this company right now. Unlock it first, then approve");
            }
            comp.setStatus(Light.Locked);
            comp.setLockUntil(LocalDateTime.now().plusDays(rules.coolFor(comp)));
            comp.setLockBy(deal.getHandler());
        }
        else if (comp.getStatus() == Light.In_Progress && deal.getHandler().equals(comp.getLockBy()))
        {
            comp.setStatus(Light.Available);
            comp.setLockUntil(null);
            comp.setLockBy(null);
        }
    }

    private void edit(Sponsorship deal, UpdateForm form, Me me, boolean admin)
    {
        if (!admin && !me.userId().equals(deal.getHandler()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own requests");
        }
        if (!admin && deal.getStatus() != Outcome.Pending)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending requests can be edited");
        }
        if (form.amount() != null)
        {
            deal.setAmount(form.amount());
        }
        if (form.notes() != null)
        {
            deal.setNotes(form.notes());
        }
    }
}
