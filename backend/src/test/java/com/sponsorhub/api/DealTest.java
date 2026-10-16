package com.sponsorhub.api;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class DealTest extends ApiBase
{
    private static final String ADMIN = "nimali.perera@eng.uni.example";
    private static final String SACHINI = "sachini.r@eng.uni.example";
    private static final String DILSHAN = "dilshan.f@eng.uni.example";

    private String dealBody(int comp_id, int event_id, String amount)
    {
        return "{\"comp_id\":" + comp_id + ",\"event_id\":" + event_id + ",\"amount\":" + amount + ",\"notes\":\"first call\"}";
    }

    private void claim(String token, int comp_id) throws Exception
    {
        mvc.perform(post("/api/companies/" + comp_id + "/lock").header("Authorization", token)).andExpect(status().isOk());
    }

    private Integer log(String token, int comp_id, int event_id) throws Exception
    {
        String json = mvc.perform(post("/api/sponsorships").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(dealBody(comp_id, event_id, "120000")))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return JsonPath.read(json, "$.id");
    }

    private void decide(String token, Integer deal_id, String result, int code) throws Exception
    {
        mvc.perform(patch("/api/sponsorships/" + deal_id + "/status").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"" + result + "\"}"))
            .andExpect(status().is(code));
    }

    @Test
    void approvalLocksTheCompany() throws Exception
    {
        String student = login(SACHINI);
        claim(student, 11);
        Integer deal_id = log(student, 11, 3);
        decide(student, deal_id, "Approved", 403);
        decide(login(ADMIN), deal_id, "Approved", 200);
        mvc.perform(get("/api/companies").header("Authorization", student))
            .andExpect(jsonPath("$[?(@.comp_id==11)].status", contains("Locked")));
        mvc.perform(get("/api/history/company/11").header("Authorization", student))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].company").value("LSEG Technology"))
            .andExpect(jsonPath("$[0].status").value("Approved"));
    }

    @Test
    void rejectionFreesTheCompanyAndCannotRepeat() throws Exception
    {
        String student = login(DILSHAN);
        claim(student, 13);
        Integer deal_id = log(student, 13, 3);
        decide(login(ADMIN), deal_id, "Rejected", 200);
        decide(login(ADMIN), deal_id, "Approved", 409);
        mvc.perform(get("/api/companies").header("Authorization", student))
            .andExpect(jsonPath("$[?(@.comp_id==13)].status", contains("Available")));
    }

    @Test
    void logNeedsAHoldAndACommitteeSeat() throws Exception
    {
        String student = login(SACHINI);
        mvc.perform(post("/api/sponsorships").header("Authorization", student).contentType(MediaType.APPLICATION_JSON).content(dealBody(14, 3, "5000")))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/sponsorships").header("Authorization", student).contentType(MediaType.APPLICATION_JSON).content(dealBody(14, 4, "5000")))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/sponsorships").header("Authorization", student).contentType(MediaType.APPLICATION_JSON).content(dealBody(14, 3, "0")))
            .andExpect(status().isBadRequest());
    }

    @Test
    void studentsEditOnlyTheirOwnPendingRequests() throws Exception
    {
        String owner = login(DILSHAN);
        claim(owner, 14);
        Integer deal_id = log(owner, 14, 3);
        String edit = "{\"amount\":99000.50}";
        mvc.perform(patch("/api/sponsorships/" + deal_id + "/status").header("Authorization", login(SACHINI)).contentType(MediaType.APPLICATION_JSON).content(edit))
            .andExpect(status().isForbidden());
        mvc.perform(patch("/api/sponsorships/" + deal_id + "/status").header("Authorization", owner).contentType(MediaType.APPLICATION_JSON).content(edit))
            .andExpect(status().isOk());
        decide(login(ADMIN), deal_id, "Approved", 200);
        mvc.perform(patch("/api/sponsorships/" + deal_id + "/status").header("Authorization", owner).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":1}"))
            .andExpect(status().isConflict());
    }

    @Test
    void historyCanBeFilteredByEvent() throws Exception
    {
        String student = login(SACHINI);
        mvc.perform(get("/api/history").header("Authorization", student))
            .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(12)));
        mvc.perform(get("/api/history/event/1").header("Authorization", student))
            .andExpect(jsonPath("$.length()").value(3));
    }
}
