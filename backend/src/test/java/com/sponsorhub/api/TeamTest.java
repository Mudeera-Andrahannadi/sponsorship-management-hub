package com.sponsorhub.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TeamTest extends ApiBase
{
    private static final String ADMIN = "nimali.perera@eng.uni.example";

    private String assignBody(int userId, int eventId)
    {
        return "{\"user_id\":" + userId + ",\"event_id\":" + eventId + ",\"post\":\"Treasurer\"}";
    }

    @Test
    void onlyAdminCreatesEvents() throws Exception
    {
        String body = "{\"name\":\"Industry Night\",\"society\":\"Alumni Society\",\"held_on\":\"2027-03-12\"}";
        mvc.perform(post("/api/events").header("Authorization", login("sachini.r@eng.uni.example")).contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/events").header("Authorization", login(ADMIN)).contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.held_month").value("March"));
    }

    @Test
    void assignRejectsDuplicatesAdminsAndUnknownUsers() throws Exception
    {
        String token = login(ADMIN);
        mvc.perform(post("/api/committees/assign").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(assignBody(7, 1)))
            .andExpect(status().isOk());
        mvc.perform(post("/api/committees/assign").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(assignBody(7, 1)))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/committees/assign").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(assignBody(1, 1)))
            .andExpect(status().isBadRequest());
        mvc.perform(post("/api/committees/assign").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(assignBody(999, 1)))
            .andExpect(status().isNotFound());
    }

    @Test
    void mineListsOnlyActiveEvents() throws Exception
    {
        mvc.perform(get("/api/committees/mine").header("Authorization", login("sachini.r@eng.uni.example")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("CareerXpo 2026"));
    }

    @Test
    void deactivatedMembersLoseTheEvent() throws Exception
    {
        String token = login(ADMIN);
        String json = mvc.perform(post("/api/committees/assign").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(assignBody(7, 2)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Integer teamId = JsonPath.read(json, "$.id");
        mvc.perform(patch("/api/committees/" + teamId).header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"active\":false}"))
            .andExpect(status().isOk());
        mvc.perform(get("/api/committees/mine").header("Authorization", login("ishara.g@eng.uni.example")))
            .andExpect(jsonPath("$[?(@.event_id==2)]").isEmpty());
        mvc.perform(get("/api/committees").header("Authorization", login("ishara.g@eng.uni.example")))
            .andExpect(status().isForbidden());
    }
}
