package com.sponsorhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
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
}
