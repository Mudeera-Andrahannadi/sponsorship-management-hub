package com.sponsorhub.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sponsorhub.dto.SignupForm;
import com.sponsorhub.entity.Role;
import com.sponsorhub.repository.UserRepository;
import org.junit.jupiter.api.Test;

class AdminSeedTest
{
    private final UserRepository userRepo = mock(UserRepository.class);
    private final AuthService auth = mock(AuthService.class);

    @Test
    void makesTheFirstAdminOnAnEmptyTable()
    {
        when(userRepo.count()).thenReturn(0L);
        new AdminSeed(userRepo, auth, "dean@eng.uni.example", "a-long-pass").run(null);
        verify(auth).create(any(SignupForm.class), eq(Role.Admin));
    }

    @Test
    void leavesExistingUsersAlone()
    {
        when(userRepo.count()).thenReturn(3L);
        new AdminSeed(userRepo, auth, "dean@eng.uni.example", "a-long-pass").run(null);
        verify(auth, never()).create(any(), any());
    }

    @Test
    void doesNothingWithoutCredentials()
    {
        when(userRepo.count()).thenReturn(0L);
        new AdminSeed(userRepo, auth, "", "").run(null);
        verify(auth, never()).create(any(), any());
    }

    @Test
    void refusesAShortPassword()
    {
        when(userRepo.count()).thenReturn(0L);
        AdminSeed seed = new AdminSeed(userRepo, auth, "dean@eng.uni.example", "short");
        assertThrows(IllegalStateException.class, () -> seed.run(null));
    }
}
