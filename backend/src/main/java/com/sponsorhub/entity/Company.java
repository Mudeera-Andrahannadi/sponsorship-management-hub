package com.sponsorhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "companies")
public class Company
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Company_ID")
    private Integer compId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Contact_Person")
    private String contact;

    @Column(name = "Position")
    private String job;

    @Column(name = "Field")
    private String field;

    @Column(name = "Email")
    private String email;

    @Column(name = "Contact_Number")
    private String phone;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "Tracking_Status")
    private Light status;

    @Column(name = "Locked_Until")
    private LocalDateTime lockUntil;

    @Column(name = "Locked_By")
    private Integer lockBy;

    @Column(name = "Lock_Days")
    private Integer lockDays;

    @Column(name = "Cooldown_Days")
    private Integer coolDays;

    public Integer getCompId()
    {
        return compId;
    }

    public void setCompId(Integer compId)
    {
        this.compId = compId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Light getStatus()
    {
        return status;
    }

    public void setStatus(Light status)
    {
        this.status = status;
    }

    public LocalDateTime getLockUntil()
    {
        return lockUntil;
    }

    public void setLockUntil(LocalDateTime lockUntil)
    {
        this.lockUntil = lockUntil;
    }

    public Integer getLockBy()
    {
        return lockBy;
    }

    public void setLockBy(Integer lockBy)
    {
        this.lockBy = lockBy;
    }

    public Integer getLockDays()
    {
        return lockDays;
    }

    public void setLockDays(Integer lockDays)
    {
        this.lockDays = lockDays;
    }

    public Integer getCoolDays()
    {
        return coolDays;
    }

    public void setCoolDays(Integer coolDays)
    {
        this.coolDays = coolDays;
    }
}
