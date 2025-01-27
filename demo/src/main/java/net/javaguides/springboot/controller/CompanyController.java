package net.javaguides.springboot.controller;
import java.util.*;

import jakarta.validation.Valid;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Company;
import net.javaguides.springboot.repository.CompanyRepository;

@CrossOrigin(origins = "http://localhost:5432")
@RestController
// base URL
@RequestMapping("/api/v1/")
public class CompanyController {

    @Autowired
    private RoleService roleService;

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // get all
    @GetMapping("/companies")
    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    // create
    @PreAuthorize("hasRole('ROLE_1')")
    @PostMapping("/companies")
    public Company createCompany(@RequestBody @Valid Company company) {

        Role role = roleService.getRoleById(3L);
        company.setRole(role);
        if (companyRepository.existsByLogin(company.getLogin())) {
            throw new RuntimeException("A company user with this login already exists!");
        }
        return companyRepository.save(company);
    }

    // get
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not exist with id :" + id));
        return ResponseEntity.ok(company);
    }

    // update
    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails){
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not exist with id :" + id));

        company.setInn(companyDetails.getInn());
        company.setKpp(companyDetails.getKpp());
        company.setOgrn(companyDetails.getOgrn());
        company.setAddress(companyDetails.getAddress());
        company.setDirector(companyDetails.getDirector());
        company.setDateRegister(companyDetails.getDateRegister());
        company.setStatus(companyDetails.getStatus());

        Company updatedCompany = companyRepository.save(company);
        return ResponseEntity.ok(updatedCompany);
    }

    // delete employee rest api
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCompany(@PathVariable Long id){
        Company employee = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not exist with id :" + id));

        companyRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
