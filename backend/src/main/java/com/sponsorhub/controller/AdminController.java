package com.sponsorhub.controller;

import com.sponsorhub.dto.Analytics;
import com.sponsorhub.dto.CompanyView;
import com.sponsorhub.dto.DaysForm;
import com.sponsorhub.dto.LockForm;
import com.sponsorhub.dto.NewUserForm;
import com.sponsorhub.dto.UserView;
import com.sponsorhub.entity.Role;
import com.sponsorhub.security.Me;
import com.sponsorhub.service.AdminService;
import com.sponsorhub.service.ReportService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('Admin')")
public class AdminController
{
    private final AdminService admin;
    private final ReportService reports;

    public AdminController(AdminService admin, ReportService reports)
    {
        this.admin = admin;
        this.reports = reports;
    }

    @PutMapping("/companies/{id}/lock-days")
    public CompanyView setDays(@AuthenticationPrincipal Me me, @PathVariable Integer id, @Valid @RequestBody DaysForm form)
    {
        return admin.setDays(id, form, me);
    }

    @PostMapping("/companies/{id}/lock")
    public CompanyView lock(@AuthenticationPrincipal Me me, @PathVariable Integer id, @Valid @RequestBody(required = false) LockForm form)
    {
        return admin.lock(id, form == null ? null : form.days(), me);
    }

    @PostMapping("/companies/{id}/unlock")
    public CompanyView unlock(@AuthenticationPrincipal Me me, @PathVariable Integer id)
    {
        return admin.unlock(id, me);
    }

    @GetMapping("/analytics")
    public Analytics analytics()
    {
        return reports.analytics();
    }

    @GetMapping("/users")
    public List<UserView> users(@RequestParam(required = false) Role role)
    {
        return admin.users(role);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserView addUser(@Valid @RequestBody NewUserForm form)
    {
        return admin.addUser(form);
    }
}
