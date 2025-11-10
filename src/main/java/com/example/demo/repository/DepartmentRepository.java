package com.example.demo.repository;

import com.example.demo.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentNumber(String departmentNumber);
    boolean existsByDepartmentNumber(String departmentNumber);
}