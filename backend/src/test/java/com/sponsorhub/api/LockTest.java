package com.sponsorhub.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import com.sponsorhub.repository.CompanyRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LockTest extends ApiBase
{
    @Autowired
    CompanyRepository compRepo;

    private int claimCode(String token, int compId, CyclicBarrier gate) throws Exception
    {
        gate.await();
        return mvc.perform(post("/api/companies/" + compId + "/lock").header("Authorization", token))
            .andReturn().getResponse().getStatus();
    }

    @Test
    void listShowsLiveStatus() throws Exception
    {
        mvc.perform(get("/api/companies").header("Authorization", login("sachini.r@eng.uni.example")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(14)))
            .andExpect(jsonPath("$[?(@.name=='Hemas Holdings')].status", contains("Available")))
            .andExpect(jsonPath("$[?(@.name=='IFS')].status", contains("Locked")));
    }

    @Test
    void secondSocietyIsBlocked() throws Exception
    {
        mvc.perform(post("/api/companies/1/lock").header("Authorization", login("sachini.r@eng.uni.example")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("In Progress"))
            .andExpect(jsonPath("$.mine").value(true))
            .andExpect(jsonPath("$.hold_days").value(7));
        mvc.perform(post("/api/companies/1/lock").header("Authorization", login("dilshan.f@eng.uni.example")))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/companies/3/lock").header("Authorization", login("dilshan.f@eng.uni.example")))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value(containsString("cooldown")));
    }

    @Test
    void onlyActiveStudentsCanClaim() throws Exception
    {
        mvc.perform(post("/api/companies/4/lock").header("Authorization", login("kavindu.m@eng.uni.example")))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/companies/4/lock").header("Authorization", login("nimali.perera@eng.uni.example")))
            .andExpect(status().isForbidden());
    }

    @Test
    void onlyOneOfTwoParallelClaimsWins() throws Exception
    {
        String first = login("tharushi.j@eng.uni.example");
        String second = login("nuwan.s@eng.uni.example");
        CyclicBarrier gate = new CyclicBarrier(2);
        Callable<Integer> oneCall = () -> claimCode(first, 5, gate);
        Callable<Integer> twoCall = () -> claimCode(second, 5, gate);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<Integer> one = pool.submit(oneCall);
        Future<Integer> two = pool.submit(twoCall);
        List<Integer> codes = List.of(one.get(), two.get()).stream().sorted().toList();
        pool.shutdown();
        assertEquals(List.of(200, 409), codes);
    }

    @Test
    void aStudentCanHoldOnlyThreeAtOnce() throws Exception
    {
        String student = login("ishara.g@eng.uni.example");
        Integer first = newCompany("Cap Test One");
        for (String name : List.of("Cap Test Two", "Cap Test Three"))
        {
            mvc.perform(post("/api/companies/" + newCompany(name) + "/lock").header("Authorization", student))
                .andExpect(status().isOk());
        }
        mvc.perform(post("/api/companies/" + first + "/lock").header("Authorization", student)).andExpect(status().isOk());
        mvc.perform(post("/api/companies/" + newCompany("Cap Test Four") + "/lock").header("Authorization", student))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value(containsString("already holding")));
        mvc.perform(post("/api/companies/" + first + "/release").header("Authorization", student)).andExpect(status().isOk());
        Integer five = newCompany("Cap Test Five");
        mvc.perform(post("/api/companies/" + five + "/lock").header("Authorization", student)).andExpect(status().isOk());

        // one slot left and two claims at the same moment: only one may take it
        mvc.perform(post("/api/companies/" + five + "/release").header("Authorization", student)).andExpect(status().isOk());
        int six = newCompany("Cap Test Six");
        int seven = newCompany("Cap Test Seven");
        CyclicBarrier gate = new CyclicBarrier(2);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<Integer> one = pool.submit(() -> claimCode(student, six, gate));
        Future<Integer> two = pool.submit(() -> claimCode(student, seven, gate));
        List<Integer> codes = List.of(one.get(), two.get()).stream().sorted().toList();
        pool.shutdown();
        assertEquals(List.of(200, 409), codes);
    }

    @Test
    void releaseWorksOnlyForTheHolder() throws Exception
    {
        mvc.perform(post("/api/companies/8/lock").header("Authorization", login("sachini.r@eng.uni.example")))
            .andExpect(status().isOk());
        mvc.perform(post("/api/companies/8/release").header("Authorization", login("dilshan.f@eng.uni.example")))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/companies/8/release").header("Authorization", login("sachini.r@eng.uni.example")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("Available"));
    }

    @Test
    void expiredLockCanBeClaimedAgain() throws Exception
    {
        Company comp = compRepo.findById(10).orElseThrow();
        comp.setStatus(Light.In_Progress);
        comp.setLockUntil(LocalDateTime.now().minusHours(1));
        comp.setLockBy(2);
        compRepo.saveAndFlush(comp);
        mvc.perform(post("/api/companies/10/lock").header("Authorization", login("nuwan.s@eng.uni.example")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.held_by").value("Nuwan Silva"));
    }
}
