package com.sponsorhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "sponsorship_history")
@Getter
@Setter
public class Sponsorship
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Sponsorship_ID")
    private Integer deal_id;

    @Column(name = "Company_ID")
    private Integer comp_id;

    @Column(name = "Event_ID")
    private Integer event_id;

    @Column(name = "Handled_By")
    private Integer handler;

    @Column(name = "Amount")
    private BigDecimal amount;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Outcome status;

    @Column(name = "Requested_Date")
    private LocalDate asked_on;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "Notes")
    private String notes;
}
