package com.sponsorhub.service;

import com.sponsorhub.dto.AssignForm;
import com.sponsorhub.dto.TeamView;
import com.sponsorhub.entity.Committee;
import com.sponsorhub.entity.Role;
import com.sponsorhub.entity.User;
import com.sponsorhub.repository.CommitteeRepository;
import com.sponsorhub.repository.EventRepository;
import com.sponsorhub.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeamService
{
    private final CommitteeRepository teamRepo;
    private final UserRepository userRepo;
    private final EventRepository eventRepo;

    public TeamService(CommitteeRepository teamRepo, UserRepository userRepo, EventRepository eventRepo)
    {
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    public Integer assign(AssignForm form)
    {
        User user = userRepo.findById(form.userId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));
        if (user.getRole() != Role.Student)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only students can join a committee");
        }
        if (!eventRepo.existsById(form.eventId()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such event");
        }
        Committee team = new Committee();
        team.setUserId(form.userId());
        team.setEventId(form.eventId());
        team.setPost(form.post());
        team.setAssignedOn(LocalDate.now());
        team.setActive(true);
        try
        {
            return teamRepo.saveAndFlush(team).getTeamId();
        }
        catch (DataIntegrityViolationException ex)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This student is already on that committee");
        }
    }

    public List<TeamView> everyone()
    {
        return teamRepo.everyone();
    }

    @Transactional
    public void setActive(Integer teamId, boolean active)
    {
        Committee team = teamRepo.findById(teamId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such committee entry"));
        team.setActive(active);
    }
}
