package com.example.demo.controllers;

import com.example.demo.models.Employee;
import com.example.demo.services.DepartmentService;
import com.example.demo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    // ========== REST API ENDPOINTS ==========

    @GetMapping("/api")
    @ResponseBody
    public List<Employee> getAllEmployeesApi() {
        return employeeService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Employee> getEmployeeByIdApi(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Employee> createEmployeeApi(@RequestBody Employee employee) {
        try {
            // Если departmentId передан в JSON, устанавливаем отдел
            if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
                departmentService.findById(employee.getDepartment().getId())
                        .ifPresent(employee::setDepartment);
            }

            Employee savedEmployee = employeeService.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Employee> updateEmployeeApi(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Optional<Employee> existingEmployee = employeeService.findById(id);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();

            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setEmail(employeeDetails.getEmail());
            employee.setPosition(employeeDetails.getPosition());
            employee.setIsAdmin(employeeDetails.getIsAdmin());

            if (employeeDetails.getDepartment() != null && employeeDetails.getDepartment().getId() != null) {
                departmentService.findById(employeeDetails.getDepartment().getId())
                        .ifPresent(employee::setDepartment);
            }

            Employee updatedEmployee = employeeService.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteEmployeeApi(@PathVariable Long id) {
        if (employeeService.findById(id).isPresent()) {
            employeeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ========== EXISTING HTML ENDPOINTS (оставляем как есть) ==========
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employees/view";
        }
        return "redirect:/employees";
    }

    @GetMapping("/new")
    public String showEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/form";
    }

    @PostMapping
    public String saveEmployee(@RequestParam("firstName") String firstName,
                               @RequestParam("lastName") String lastName,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "position", required = false) String position,
                               @RequestParam(value = "departmentId", required = false) Long departmentId,
                               @RequestParam(value = "isAdmin", required = false) Boolean isAdmin,
                               @RequestParam(value = "id", required = false) Long id) {

        System.out.println("=== SAVING EMPLOYEE ===");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Position: " + position);
        System.out.println("Department ID: " + departmentId);
        System.out.println("Is Admin: " + isAdmin);

        Employee employee;
        if (id != null) {
            employee = employeeService.findById(id).orElse(new Employee());
            System.out.println("Editing existing employee ID: " + id);
        } else {
            employee = new Employee();
            System.out.println("Creating new employee");
        }

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPosition(position);
        employee.setIsAdmin(isAdmin != null ? isAdmin : false);

        if (departmentId != null) {
            departmentService.findById(departmentId).ifPresent(employee::setDepartment);
            System.out.println("Set department: " + departmentId);
        } else {
            employee.setDepartment(null);
            System.out.println("No department set");
        }

        try {
            Employee saved = employeeService.save(employee);
            System.out.println("=== EMPLOYEE SAVED SUCCESSFULLY ===");
            System.out.println("Saved employee ID: " + saved.getId());
        } catch (Exception e) {
            System.out.println("=== ERROR SAVING EMPLOYEE ===");
            e.printStackTrace();
        }

        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String editEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("departments", departmentService.findAll());
            return "employees/form";
        }
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }
}