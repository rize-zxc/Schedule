package com.example.demo.services;

import com.example.demo.models.Schedule;
import com.example.demo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> findByEmployeeId(Long employeeId) {
        return scheduleRepository.findByEmployeeId(employeeId);
    }

    public List<Schedule> findByWorkDate(LocalDate workDate) {
        return scheduleRepository.findByWorkDate(workDate);
    }

    public List<Schedule> findSchedulesInDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findSchedulesInDateRange(startDate, endDate);
    }
}