package com.sponsorhub.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import com.sponsorhub.entity.Outcome;
import com.sponsorhub.entity.Role;
import com.sponsorhub.entity.Sponsorship;
import com.sponsorhub.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
class CrudTest
{
    @Container
    static MySQLContainer<?> db = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("sponsorship_hub")
        .withCopyFileToContainer(MountableFile.forHostPath("../db/init.sql"), "/docker-entrypoint-initdb.d/01-init.sql")
        .withCopyFileToContainer(MountableFile.forHostPath("../db/demo.sql"), "/docker-entrypoint-initdb.d/02-demo.sql");

    @DynamicPropertySource
    static void wire(DynamicPropertyRegistry reg)
    {
        reg.add("spring.datasource.url", db::getJdbcUrl);
        reg.add("spring.datasource.username", db::getUsername);
        reg.add("spring.datasource.password", db::getPassword);
    }

    @Autowired
    TestEntityManager em;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CompanyRepository compRepo;

    @Autowired
    SponsorshipRepository dealRepo;

    @Test
    void seedRowsLoad()
    {
        long busy = compRepo.findAll().stream().filter(item -> item.getStatus() == Light.In_Progress).count();
        assertEquals(14, compRepo.count());
        assertEquals(3, busy);
    }

    @Test
    void findsAdminByEmail()
    {
        User admin = userRepo.findByEmail("nimali.perera@eng.uni.example").orElseThrow();
        assertEquals(Role.Admin, admin.getRole());
    }

    @Test
    void companyCrud()
    {
        Company comp = new Company();
        comp.setName("Test Co");
        comp.setStatus(Light.Available);
        Integer compId = compRepo.saveAndFlush(comp).getCompId();
        comp.setStatus(Light.In_Progress);
        comp.setLockUntil(LocalDateTime.now().plusDays(7));
        compRepo.saveAndFlush(comp);
        em.clear();
        Company back = compRepo.findById(compId).orElseThrow();
        assertEquals(Light.In_Progress, back.getStatus());
        compRepo.delete(back);
        compRepo.flush();
        assertFalse(compRepo.existsById(compId));
    }

    @Test
    void dealRoundTrip()
    {
        Sponsorship deal = new Sponsorship();
        deal.setCompId(1);
        deal.setEventId(1);
        deal.setHandler(2);
        deal.setAmount(new BigDecimal("50000.00"));
        deal.setStatus(Outcome.Pending);
        deal.setAskedOn(LocalDate.now());
        deal.setNotes("first call went well");
        Integer dealId = dealRepo.saveAndFlush(deal).getDealId();
        em.clear();
        Sponsorship back = dealRepo.findById(dealId).orElseThrow();
        assertEquals(Outcome.Pending, back.getStatus());
        assertEquals("first call went well", back.getNotes());
        assertEquals(0, new BigDecimal("50000.00").compareTo(back.getAmount()));
    }
}
