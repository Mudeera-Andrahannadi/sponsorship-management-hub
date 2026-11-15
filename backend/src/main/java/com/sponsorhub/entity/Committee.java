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
    private Integer teamId;

    @Column(name = "User_ID")
    private Integer userId;

    @Column(name = "Event_ID")
    private Integer eventId;

    @Column(name = "Position")
    private String post;

    @Column(name = "Assigned_Date")
    private LocalDate assignedOn;

    @Column(name = "Is_Active")
    private Boolean active;

    public Integer getTeamId()
    {
        return teamId;
    }

    public void setTeamId(Integer teamId)
    {
        this.teamId = teamId;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getEventId()
    {
        return eventId;
    }

    public void setEventId(Integer eventId)
    {
        this.eventId = eventId;
    }

    public String getPost()
    {
        return post;
    }

    public void setPost(String post)
    {
        this.post = post;
    }

    public LocalDate getAssignedOn()
    {
        return assignedOn;
    }

    public void setAssignedOn(LocalDate assignedOn)
    {
        this.assignedOn = assignedOn;
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
