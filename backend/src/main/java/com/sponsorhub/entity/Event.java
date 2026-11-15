package com.sponsorhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Event_ID")
    private Integer eventId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Organized_Society")
    private String society;

    @Column(name = "Event_Month")
    private String heldMonth;

    @Column(name = "Event_Date")
    private LocalDate heldOn;

    public Integer getEventId()
    {
        return eventId;
    }

    public void setEventId(Integer eventId)
    {
        this.eventId = eventId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSociety()
    {
        return society;
    }

    public void setSociety(String society)
    {
        this.society = society;
    }

    public String getHeldMonth()
    {
        return heldMonth;
    }

    public void setHeldMonth(String heldMonth)
    {
        this.heldMonth = heldMonth;
    }

    public LocalDate getHeldOn()
    {
        return heldOn;
    }

    public void setHeldOn(LocalDate heldOn)
    {
        this.heldOn = heldOn;
    }
}
