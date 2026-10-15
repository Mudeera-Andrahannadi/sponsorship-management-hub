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
    private final CommitteeRepository team_repo;
    private final UserRepository user_repo;
    private final EventRepository event_repo;

    public TeamService(CommitteeRepository team_repo, UserRepository user_repo, EventRepository event_repo)
    {
        this.team_repo = team_repo;
        this.user_repo = user_repo;
        this.event_repo = event_repo;
    }

    public Integer assign(AssignForm form)
    {
        User user = user_repo.findById(form.user_id())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));
        if (user.getRole() != Role.Student)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only students can join a committee");
        }
        if (!event_repo.existsById(form.event_id()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such event");
        }
        Committee team = new Committee();
        team.setUser_id(form.user_id());
        team.setEvent_id(form.event_id());
        team.setPost(form.post());
        team.setAssigned_on(LocalDate.now());
        team.setActive(true);
        try
        {
            return team_repo.saveAndFlush(team).getTeam_id();
        }
        catch (DataIntegrityViolationException ex)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This student is already on that committee");
        }
    }

    public List<TeamView> everyone()
    {
        return team_repo.everyone();
    }

    @Transactional
    public void setActive(Integer team_id, boolean active)
    {
        Committee team = team_repo.findById(team_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such committee entry"));
        team.setActive(active);
    }
}
