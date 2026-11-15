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
    private Integer dealId;

    @Column(name = "Company_ID")
    private Integer compId;

    @Column(name = "Event_ID")
    private Integer eventId;

    @Column(name = "Handled_By")
    private Integer handler;

    @Column(name = "Amount")
    private BigDecimal amount;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Outcome status;

    @Column(name = "Requested_Date")
    private LocalDate askedOn;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "Notes")
    private String notes;

    public Integer getDealId()
    {
        return dealId;
    }

    public void setDealId(Integer dealId)
    {
        this.dealId = dealId;
    }

    public Integer getCompId()
    {
        return compId;
    }

    public void setCompId(Integer compId)
    {
        this.compId = compId;
    }

    public Integer getEventId()
    {
        return eventId;
    }

    public void setEventId(Integer eventId)
    {
        this.eventId = eventId;
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

    public LocalDate getAskedOn()
    {
        return askedOn;
    }

    public void setAskedOn(LocalDate askedOn)
    {
        this.askedOn = askedOn;
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
