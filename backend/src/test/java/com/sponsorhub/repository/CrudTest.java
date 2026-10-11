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
        .withCommand("--lower-case-table-names=1")
        .withCopyFileToContainer(MountableFile.forHostPath("../db/init.sql"), "/docker-entrypoint-initdb.d/init.sql");

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
    UserRepository user_repo;

    @Autowired
    CompanyRepository comp_repo;

    @Autowired
    SponsorshipRepository deal_repo;

    @Test
    void seedRowsLoad()
    {
        long busy = comp_repo.findAll().stream().filter(item -> item.getStatus() == Light.In_Progress).count();
        assertEquals(14, comp_repo.count());
        assertEquals(3, busy);
    }

    @Test
    void findsAdminByEmail()
    {
        User admin = user_repo.findByEmail("nimali.perera@eng.uni.example").orElseThrow();
        assertEquals(Role.Admin, admin.getRole());
    }

    @Test
    void companyCrud()
    {
        Company comp = new Company();
        comp.setName("Test Co");
        comp.setStatus(Light.Available);
        Integer comp_id = comp_repo.saveAndFlush(comp).getComp_id();
        comp.setStatus(Light.In_Progress);
        comp.setLock_until(LocalDateTime.now().plusDays(7));
        comp_repo.saveAndFlush(comp);
        em.clear();
        Company back = comp_repo.findById(comp_id).orElseThrow();
        assertEquals(Light.In_Progress, back.getStatus());
        comp_repo.delete(back);
        comp_repo.flush();
        assertFalse(comp_repo.existsById(comp_id));
    }

    @Test
    void dealRoundTrip()
    {
        Sponsorship deal = new Sponsorship();
        deal.setComp_id(1);
        deal.setEvent_id(1);
        deal.setHandler(2);
        deal.setAmount(new BigDecimal("50000.00"));
        deal.setStatus(Outcome.Pending);
        deal.setAsked_on(LocalDate.now());
        deal.setNotes("first call went well");
        Integer deal_id = deal_repo.saveAndFlush(deal).getDeal_id();
        em.clear();
        Sponsorship back = deal_repo.findById(deal_id).orElseThrow();
        assertEquals(Outcome.Pending, back.getStatus());
        assertEquals("first call went well", back.getNotes());
        assertEquals(0, new BigDecimal("50000.00").compareTo(back.getAmount()));
    }
}
