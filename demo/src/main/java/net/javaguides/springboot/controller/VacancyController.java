package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.model.Vacancy;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5432")
@RestController
// base URL
@RequestMapping("/api/")
public class VacancyController {
    private final VacancyRepository vacancyRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public VacancyController(VacancyRepository vacancyRepository, EmployeeRepository employeeRepository) {
        this.vacancyRepository = vacancyRepository;
        this.employeeRepository = employeeRepository;
    }

    // create
    @PreAuthorize("hasRole('ROLE_3')")
    @PostMapping("/vacancy")
    public Vacancy createVacancy(@RequestBody @Valid Vacancy vacancy,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        Employee employee = (Employee) employeeRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        vacancy.setCompany(employee.getCompany());
        return vacancyRepository.save(vacancy);
    }

}
