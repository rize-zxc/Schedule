package com.example.demo.services;

import com.example.demo.models.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    public Optional<Department> findByDepartmentNumber(String departmentNumber) {
        return departmentRepository.findByDepartmentNumber(departmentNumber);
    }

    public boolean existsByDepartmentNumber(String departmentNumber) {
        return departmentRepository.existsByDepartmentNumber(departmentNumber);
    }
}