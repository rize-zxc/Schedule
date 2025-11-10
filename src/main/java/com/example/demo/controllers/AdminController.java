package com.example.demo.controllers;

import com.example.demo.services.EmployeeService;
import com.example.demo.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("todaySchedules", scheduleService.findByWorkDate(LocalDate.now()));
            model.addAttribute("totalEmployees", employeeService.findAll().size());
        } catch (Exception e) {
            // В случае ошибки добавляем пустые коллекции
            model.addAttribute("employees", Collections.emptyList());
            model.addAttribute("todaySchedules", Collections.emptyList());
            model.addAttribute("totalEmployees", 0);
        }
        return "admin/dashboard";
    }

    @GetMapping("/employees")
    public String manageEmployees() {
        return "redirect:/employees";
    }

    @GetMapping("/schedules")
    public String manageSchedules() {
        return "redirect:/schedules";
    }
}