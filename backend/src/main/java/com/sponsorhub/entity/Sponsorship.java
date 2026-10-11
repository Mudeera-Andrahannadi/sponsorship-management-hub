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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "sponsorship_history")
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

    public Integer getDeal_id()
    {
        return deal_id;
    }

    public void setDeal_id(Integer deal_id)
    {
        this.deal_id = deal_id;
    }

    public Integer getComp_id()
    {
        return comp_id;
    }

    public void setComp_id(Integer comp_id)
    {
        this.comp_id = comp_id;
    }

    public Integer getEvent_id()
    {
        return event_id;
    }

    public void setEvent_id(Integer event_id)
    {
        this.event_id = event_id;
    }

    public Integer getHandler()
    {
        return handler;
    }

    public void setHandler(Integer handler)
    {
        this.handler = handler;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Outcome getStatus()
    {
        return status;
    }

    public void setStatus(Outcome status)
    {
        this.status = status;
    }

    public LocalDate getAsked_on()
    {
        return asked_on;
    }

    public void setAsked_on(LocalDate asked_on)
    {
        this.asked_on = asked_on;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}
