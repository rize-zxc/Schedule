package com.example.demo.controllers;

import com.example.demo.models.Schedule;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private EmployeeService employeeService;

    // ========== REST API ENDPOINTS ==========

    @GetMapping("/api")
    @ResponseBody
    public List<Schedule> getAllSchedulesApi() {
        return scheduleService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Schedule> getScheduleByIdApi(@PathVariable Long id) {
        Optional<Schedule> schedule = scheduleService.findById(id);
        return schedule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Schedule> createScheduleApi(@RequestBody Schedule schedule) {
        try {
            // Проверяем и устанавливаем сотрудника
            if (schedule.getEmployee() != null && schedule.getEmployee().getId() != null) {
                employeeService.findById(schedule.getEmployee().getId())
                        .ifPresent(schedule::setEmployee);
            } else {
                return ResponseEntity.badRequest().build();
            }

            Schedule savedSchedule = scheduleService.save(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Schedule> updateScheduleApi(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        Optional<Schedule> existingSchedule = scheduleService.findById(id);
        if (existingSchedule.isPresent()) {
            Schedule schedule = existingSchedule.get();

            schedule.setWorkDate(scheduleDetails.getWorkDate());
            schedule.setStartTime(scheduleDetails.getStartTime());
            schedule.setEndTime(scheduleDetails.getEndTime());
            schedule.setDescription(scheduleDetails.getDescription());

            if (scheduleDetails.getEmployee() != null && scheduleDetails.getEmployee().getId() != null) {
                employeeService.findById(scheduleDetails.getEmployee().getId())
                        .ifPresent(schedule::setEmployee);
            }

            Schedule updatedSchedule = scheduleService.save(schedule);
            return ResponseEntity.ok(updatedSchedule);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteScheduleApi(@PathVariable Long id) {
        if (scheduleService.findById(id).isPresent()) {
            scheduleService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/api/search")
    @ResponseBody
    public List<Schedule> searchSchedulesApi(@RequestParam(required = false) LocalDate date) {
        if (date != null) {
            return scheduleService.findByWorkDate(date);
        }
        return scheduleService.findAll();
    }

    // ========== EXISTING HTML ENDPOINTS ==========
    @GetMapping
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.findAll());
        return "schedules/list";
    }

    @GetMapping("/new")
    public String showScheduleForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("employees", employeeService.findAll());
        return "schedules/form";
    }

    @PostMapping
    public String saveSchedule(@RequestParam("employeeId") Long employeeId,
                               @RequestParam("workDate") LocalDate workDate,
                               @RequestParam(value = "startTime", required = false) LocalTime startTime,
                               @RequestParam(value = "endTime", required = false) LocalTime endTime,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "id", required = false) Long id) {

        Schedule schedule;
        if (id != null) {
            schedule = scheduleService.findById(id).orElse(new Schedule());
        } else {
            schedule = new Schedule();
        }

        schedule.setWorkDate(workDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setDescription(description);

        employeeService.findById(employeeId).ifPresent(schedule::setEmployee);

        scheduleService.save(schedule);
        return "redirect:/schedules";
    }



    @PostMapping("/{id}/delete")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteById(id);
        return "redirect:/schedules";
    }

    @GetMapping("/{id}/edit")
    public String editSchedule(@PathVariable Long id, Model model) {
        Optional<Schedule> schedule = scheduleService.findById(id);
        if (schedule.isPresent()) {
            model.addAttribute("schedule", schedule.get());
            model.addAttribute("employees", employeeService.findAll());
            return "schedules/form";
        }
        return "redirect:/schedules";
    }

    @GetMapping("/search")
    public String searchSchedules(@RequestParam(required = false) LocalDate date, Model model) {
        if (date != null) {
            model.addAttribute("schedules", scheduleService.findByWorkDate(date));
            model.addAttribute("searchDate", date);
        } else {
            model.addAttribute("schedules", scheduleService.findAll());
        }
        return "schedules/list";
    }

}