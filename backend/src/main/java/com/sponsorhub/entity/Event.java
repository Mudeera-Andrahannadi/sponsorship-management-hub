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
    private Integer event_id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Organized_Society")
    private String society;

    @Column(name = "Event_Month")
    private String held_month;

    @Column(name = "Event_Date")
    private LocalDate held_on;

    public Integer getEvent_id()
    {
        return event_id;
    }

    public void setEvent_id(Integer event_id)
    {
        this.event_id = event_id;
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

    public String getHeld_month()
    {
        return held_month;
    }

    public void setHeld_month(String held_month)
    {
        this.held_month = held_month;
    }

    public LocalDate getHeld_on()
    {
        return held_on;
    }

    public void setHeld_on(LocalDate held_on)
    {
        this.held_on = held_on;
    }
}
