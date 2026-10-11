package com.sponsorhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "event_committee")
public class Committee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Committee_ID")
    private Integer team_id;

    @Column(name = "User_ID")
    private Integer user_id;

    @Column(name = "Event_ID")
    private Integer event_id;

    @Column(name = "Position")
    private String post;

    @Column(name = "Assigned_Date")
    private LocalDate assigned_on;

    @Column(name = "Is_Active")
    private Boolean active;

    public Integer getTeam_id()
    {
        return team_id;
    }

    public void setTeam_id(Integer team_id)
    {
        this.team_id = team_id;
    }

    public Integer getUser_id()
    {
        return user_id;
    }

    public void setUser_id(Integer user_id)
    {
        this.user_id = user_id;
    }

    public Integer getEvent_id()
    {
        return event_id;
    }

    public void setEvent_id(Integer event_id)
    {
        this.event_id = event_id;
    }

    public String getPost()
    {
        return post;
    }

    public void setPost(String post)
    {
        this.post = post;
    }

    public LocalDate getAssigned_on()
    {
        return assigned_on;
    }

    public void setAssigned_on(LocalDate assigned_on)
    {
        this.assigned_on = assigned_on;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }
}
