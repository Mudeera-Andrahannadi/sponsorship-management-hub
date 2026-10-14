package com.sponsorhub.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuthTest extends ApiBase
{
    private String signup(String email, String reg_no, String extra)
    {
        return "{\"name\":\"New Student\",\"reg_no\":\"" + reg_no + "\",\"email\":\"" + email + "\",\"password\":\"longenough1\"" + extra + "}";
    }

    @Test
    void registerAlwaysMakesStudent() throws Exception
    {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(signup("a1@eng.uni.example", "EE/24/001", ",\"role\":\"Admin\"")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.role").value("Student"));
    }

    @Test
    void duplicateEmailIsRejected() throws Exception
    {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(signup("a2@eng.uni.example", "EE/24/002", "")))
            .andExpect(status().isCreated());
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(signup("a2@eng.uni.example", "EE/24/003", "")))
            .andExpect(status().isConflict());
    }

    @Test
    void shortPasswordIsRejected() throws Exception
    {
        String body = "{\"name\":\"X\",\"reg_no\":\"EE/24/004\",\"email\":\"a3@eng.uni.example\",\"password\":\"short\"}";
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("password size must be between 8 and 2147483647"));
    }

    @Test
    void wrongPasswordGets401() throws Exception
    {
        String body = "{\"email\":\"nimali.perera@eng.uni.example\",\"password\":\"nope-nope\"}";
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedRoutesNeedAValidToken() throws Exception
    {
        mvc.perform(get("/api/companies")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/companies").header("Authorization", "Bearer not.a.token")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/companies").header("Authorization", login("sachini.r@eng.uni.example"))).andExpect(status().isOk());
    }
}
