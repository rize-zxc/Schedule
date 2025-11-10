package com.example.demo.controllers;

import com.example.demo.models.Department;
import com.example.demo.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // ========== REST API ENDPOINTS ==========

    @GetMapping("/api")
    @ResponseBody
    public List<Department> getAllDepartmentsApi() {
        return departmentService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Department> getDepartmentByIdApi(@PathVariable Long id) {
        Optional<Department> department = departmentService.findById(id);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Department> createDepartmentApi(@RequestBody Department department) {
        try {
            System.out.println("Creating department: " + department.getName());
            Department savedDepartment = departmentService.save(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
        } catch (Exception e) {
            System.out.println("Error creating department: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Department> updateDepartmentApi(@PathVariable Long id, @RequestBody Department departmentDetails) {
        Optional<Department> existingDepartment = departmentService.findById(id);
        if (existingDepartment.isPresent()) {
            Department department = existingDepartment.get();


            department.setDepartmentNumber(departmentDetails.getDepartmentNumber());
            department.setName(departmentDetails.getName());
            department.setServiceType(departmentDetails.getServiceType());
            department.setHeadOfDepartment(departmentDetails.getHeadOfDepartment());
            department.setEmployeeCount(departmentDetails.getEmployeeCount());
            department.setDepartmentStructure(departmentDetails.getDepartmentStructure());

            Department updatedDepartment = departmentService.save(department);
            return ResponseEntity.ok(updatedDepartment);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteDepartmentApi(@PathVariable Long id) {
        if (departmentService.findById(id).isPresent()) {
            departmentService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ========== HTML ENDPOINTS ==========

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/list";
    }

    @GetMapping("/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/view";
        }
        return "redirect:/departments";
    }

    @GetMapping("/new")
    public String showDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @PostMapping
    public String saveDepartment(@RequestParam("departmentNumber") String departmentNumber,
                                 @RequestParam("name") String name,
                                 @RequestParam("serviceType") String serviceType,
                                 @RequestParam(value = "headOfDepartment", required = false) String headOfDepartment,
                                 @RequestParam(value = "employeeCount", required = false) Integer employeeCount,
                                 @RequestParam(value = "departmentStructure", required = false) String departmentStructure,
                                 @RequestParam(value = "id", required = false) Long id) {

        System.out.println("=== SAVING DEPARTMENT ===");
        System.out.println("Department Number: " + departmentNumber);
        System.out.println("Name: " + name);
        System.out.println("Service Type: " + serviceType);

        Department department;
        if (id != null) {
            department = departmentService.findById(id).orElse(new Department());
            System.out.println("Editing existing department ID: " + id);
        } else {
            department = new Department();
            System.out.println("Creating new department");
        }

        department.setDepartmentNumber(departmentNumber);
        department.setName(name);
        department.setServiceType(serviceType);
        department.setHeadOfDepartment(headOfDepartment);
        department.setEmployeeCount(employeeCount != null ? employeeCount : 0);
        department.setDepartmentStructure(departmentStructure);

        try {
            Department saved = departmentService.save(department);
            System.out.println("=== DEPARTMENT SAVED SUCCESSFULLY ===");
            System.out.println("Saved department ID: " + saved.getId());
        } catch (Exception e) {
            System.out.println("=== ERROR SAVING DEPARTMENT ===");
            e.printStackTrace();
        }

        return "redirect:/departments";
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/form";
        }
        return "redirect:/departments";
    }

    @PostMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return "redirect:/departments";
    }
}