package com.sponsorhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "companies")
@Getter
@Setter
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
}
