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
@Table(name = "event_committee")
@Getter
@Setter
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
}
