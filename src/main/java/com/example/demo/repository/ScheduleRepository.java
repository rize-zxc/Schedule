package com.example.demo.repository;

import com.example.demo.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByEmployeeId(Long employeeId);

    List<Schedule> findByWorkDate(LocalDate workDate);

    @Query("SELECT s FROM Schedule s WHERE s.workDate BETWEEN :startDate AND :endDate")
    List<Schedule> findSchedulesInDateRange(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);
}