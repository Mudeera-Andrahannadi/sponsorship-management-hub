package com.sponsorhub.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AdminTest extends ApiBase
{
    private static final String ADMIN = "nimali.perera@eng.uni.example";

    @Test
    void shorterHoldTimeAppliesToTheNextClaim() throws Exception
    {
        String token = login(ADMIN);
        String json = mvc.perform(post("/api/companies").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Short Hold Co\"}"))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        Integer comp_id = JsonPath.read(json, "$.comp_id");
        mvc.perform(put("/api/admin/companies/" + comp_id + "/lock-days").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"lock_days\":3}"))
            .andExpect(status().isOk());
        mvc.perform(post("/api/companies/" + comp_id + "/lock").header("Authorization", login("tharushi.j@eng.uni.example")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.days_left").value(3));
    }

    @Test
    void adminCanLockAndUnlockByHand() throws Exception
    {
        String token = login(ADMIN);
        mvc.perform(post("/api/admin/companies/2/lock").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"days\":10}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("Locked"))
            .andExpect(jsonPath("$.days_left").value(10));
        mvc.perform(post("/api/companies/2/lock").header("Authorization", login("dilshan.f@eng.uni.example")))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/admin/companies/2/unlock").header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("Available"));
    }

    @Test
    void studentsCannotUseAdminRoutes() throws Exception
    {
        String student = login("sachini.r@eng.uni.example");
        mvc.perform(post("/api/admin/companies/2/lock").header("Authorization", student)).andExpect(status().isForbidden());
        mvc.perform(put("/api/admin/companies/2/lock-days").header("Authorization", student).contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void lockDaysMustBePositive() throws Exception
    {
        mvc.perform(put("/api/admin/companies/6/lock-days").header("Authorization", login(ADMIN)).contentType(MediaType.APPLICATION_JSON).content("{\"lock_days\":0}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void directoryCrud() throws Exception
    {
        String token = login(ADMIN);
        String body = "{\"name\":\"Test Corp\",\"contact\":\"Ama Silva\",\"email\":\"ama@testcorp.example\"}";
        mvc.perform(post("/api/companies").header("Authorization", login("sachini.r@eng.uni.example")).contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isForbidden());
        String json = mvc.perform(post("/api/companies").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("Available"))
            .andReturn().getResponse().getContentAsString();
        Integer comp_id = JsonPath.read(json, "$.comp_id");
        mvc.perform(put("/api/companies/" + comp_id).header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Test Corp\",\"phone\":\"+94 11 555 0999\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.phone").value("+94 11 555 0999"));
        mvc.perform(delete("/api/companies/" + comp_id).header("Authorization", token)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/companies/" + comp_id).header("Authorization", token)).andExpect(status().isNotFound());
    }

    @Test
    void companiesWithHistoryCannotBeDeleted() throws Exception
    {
        mvc.perform(delete("/api/companies/3").header("Authorization", login(ADMIN)))
            .andExpect(status().isConflict());
    }
}
