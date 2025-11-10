package com.example.demo.controllers;

import com.example.demo.services.EmployeeService;
import com.example.demo.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class HomeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("employees", employeeService.findAll());

        var todaySchedules = scheduleService.findByWorkDate(LocalDate.now());
        model.addAttribute("todaySchedules", todaySchedules);

        return "home";
    }

    @GetMapping("/view-schedules")
    public String viewSchedules(@RequestParam(required = false) LocalDate date, Model model) {
        LocalDate viewDate = date != null ? date : LocalDate.now();

        var schedules = scheduleService.findByWorkDate(viewDate);
        model.addAttribute("schedules", schedules);
        model.addAttribute("viewDate", viewDate);

        return "view-schedules";
    }
}