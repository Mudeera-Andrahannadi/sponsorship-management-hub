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
    private final SponsorshipRepository deal_repo;
    private final CommitteeRepository team_repo;
    private final LockService locks;
    private final Rules rules;

    public SponsorshipService(SponsorshipRepository deal_repo, CommitteeRepository team_repo, LockService locks, Rules rules)
    {
        this.deal_repo = deal_repo;
        this.team_repo = team_repo;
        this.locks = locks;
        this.rules = rules;
    }

    @Transactional
    public Integer log(SponsorForm form, Me me)
    {
        if (!team_repo.isActiveIn(me.user_id(), form.event_id()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not on that event's committee");
        }
        Company comp = locks.find(form.comp_id());
        LocalDateTime now = LocalDateTime.now();
        if (!holds(comp, me, now))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Claim this company first. Your hold may have run out");
        }
        Sponsorship deal = new Sponsorship();
        deal.setComp_id(form.comp_id());
        deal.setEvent_id(form.event_id());
        deal.setHandler(me.user_id());
        deal.setAmount(form.amount());
        deal.setNotes(form.notes());
        deal.setStatus(Outcome.Pending);
        deal.setAsked_on(LocalDate.now());
        comp.setLock_until(now.plusDays(rules.holdFor(comp)));
        return deal_repo.save(deal).getDeal_id();
    }

    @Transactional
    public void update(Integer deal_id, UpdateForm form, Me me)
    {
        Sponsorship deal = deal_repo.findById(deal_id)
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

    public List<HistoryRow> history(Integer comp_id, Integer event_id)
    {
        return deal_repo.rows(comp_id, event_id);
    }

    private boolean holds(Company comp, Me me, LocalDateTime now)
    {
        return comp.getStatus() == Light.In_Progress && me.user_id().equals(comp.getLock_by())
            && comp.getLock_until() != null && comp.getLock_until().isAfter(now);
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
        settle(locks.find(deal.getComp_id()), deal, result);
    }

    private void settle(Company comp, Sponsorship deal, Outcome result)
    {
        if (result == Outcome.Approved)
        {
            comp.setStatus(Light.Locked);
            comp.setLock_until(LocalDateTime.now().plusDays(rules.coolFor(comp)));
            comp.setLock_by(deal.getHandler());
        }
        else if (comp.getStatus() == Light.In_Progress && deal.getHandler().equals(comp.getLock_by()))
        {
            comp.setStatus(Light.Available);
            comp.setLock_until(null);
            comp.setLock_by(null);
        }
    }

    private void edit(Sponsorship deal, UpdateForm form, Me me, boolean admin)
    {
        if (!admin && !me.user_id().equals(deal.getHandler()))
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
