package com.sponsorhub.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.MountableFile;

@SpringBootTest(properties = "hub.jwt-secret=test-secret-test-secret-test-secret-123")
@AutoConfigureMockMvc
@EnabledIf("dockerUp")
abstract class ApiBase
{
    private static MySQLContainer<?> box;

    static boolean dockerUp()
    {
        return DockerClientFactory.instance().isDockerAvailable();
    }

    static synchronized MySQLContainer<?> db()
    {
        if (box == null)
        {
            box = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("sponsorship_hub")
                .withCommand("--lower-case-table-names=1")
                .withCopyFileToContainer(MountableFile.forHostPath("../db/init.sql"), "/docker-entrypoint-initdb.d/init.sql");
            box.start();
        }
        return box;
    }

    @DynamicPropertySource
    static void wire(DynamicPropertyRegistry reg)
    {
        reg.add("spring.datasource.url", () -> db().getJdbcUrl());
        reg.add("spring.datasource.username", () -> db().getUsername());
        reg.add("spring.datasource.password", () -> db().getPassword());
    }

    @Autowired
    MockMvc mvc;

    Integer newCompany(String name) throws Exception
    {
        String json = mvc.perform(post("/api/companies").header("Authorization", login("nimali.perera@eng.uni.example"))
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"" + name + "\"}"))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return JsonPath.read(json, "$.comp_id");
    }

    String login(String email) throws Exception
    {
        String body = "{\"email\":\"" + email + "\",\"password\":\"Hub@2026\"}";
        String json = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(json, "$.token");
    }
}
