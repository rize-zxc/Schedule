package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Номер отдела обязателен")
    @Column(name = "department_number", unique = true, nullable = false)
    private String departmentNumber;

    @NotBlank(message = "Название отдела обязательно")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Тип услуг обязателен")
    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "head_of_department")
    private String headOfDepartment;

    @Column(name = "employee_count")
    private Integer employeeCount = 0;

    @Column(name = "department_structure")
    private String departmentStructure;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

    public Department() {}

    public Department(String departmentNumber, String name, String serviceType, String headOfDepartment) {
        this.departmentNumber = departmentNumber;
        this.name = name;
        this.serviceType = serviceType;
        this.headOfDepartment = headOfDepartment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDepartmentNumber() { return departmentNumber; }
    public void setDepartmentNumber(String departmentNumber) { this.departmentNumber = departmentNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getHeadOfDepartment() { return headOfDepartment; }
    public void setHeadOfDepartment(String headOfDepartment) { this.headOfDepartment = headOfDepartment; }

    public Integer getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Integer employeeCount) { this.employeeCount = employeeCount; }

    public String getDepartmentStructure() { return departmentStructure; }
    public void setDepartmentStructure(String departmentStructure) { this.departmentStructure = departmentStructure; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}