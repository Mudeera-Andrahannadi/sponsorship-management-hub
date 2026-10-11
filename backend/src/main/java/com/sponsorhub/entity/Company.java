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
    private Integer comp_id;

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
    private LocalDateTime lock_until;

    @Column(name = "Locked_By")
    private Integer lock_by;

    @Column(name = "Lock_Days")
    private Integer lock_days;

    @Column(name = "Cooldown_Days")
    private Integer cool_days;

    public Integer getComp_id()
    {
        return comp_id;
    }

    public void setComp_id(Integer comp_id)
    {
        this.comp_id = comp_id;
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

    public LocalDateTime getLock_until()
    {
        return lock_until;
    }

    public void setLock_until(LocalDateTime lock_until)
    {
        this.lock_until = lock_until;
    }

    public Integer getLock_by()
    {
        return lock_by;
    }

    public void setLock_by(Integer lock_by)
    {
        this.lock_by = lock_by;
    }

    public Integer getLock_days()
    {
        return lock_days;
    }

    public void setLock_days(Integer lock_days)
    {
        this.lock_days = lock_days;
    }

    public Integer getCool_days()
    {
        return cool_days;
    }

    public void setCool_days(Integer cool_days)
    {
        this.cool_days = cool_days;
    }
}
