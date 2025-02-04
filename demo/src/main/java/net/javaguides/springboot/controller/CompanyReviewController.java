package net.javaguides.springboot.controller;

import java.util.*;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Company_review;
import net.javaguides.springboot.repository.CompanyReviewRepository;

@CrossOrigin(origins = "http://localhost:5432")
@RestController
// base URL
@RequestMapping("/api/v1/")
public class CompanyReviewController {

    private final CompanyReviewRepository reviewCompanyRepository;

    @Autowired
    public CompanyReviewController(CompanyReviewRepository reviewCompanyRepository) {
        this.reviewCompanyRepository = reviewCompanyRepository;
    }


    @GetMapping("/company_reviews")
    public List<Company_review> getAllReviewsCompany() {
        return reviewCompanyRepository.findAll();
    }


    @PostMapping("/company_reviews")
    public ResponseEntity<Map<String, Boolean>> createReviewCompany(@Valid @RequestBody Company_review reviewCompany) {
        reviewCompanyRepository.save(reviewCompany);

        Map<String, Boolean> response = new HashMap<>();
        response.put("created", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company_reviews/{id}")
    public ResponseEntity<Company_review> getReviewCompanyById(@PathVariable Long id) {
        Company_review company = reviewCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review company not exist with id :" + id));
        return ResponseEntity.ok(company);
    }

    /*
    @PutMapping("/reviews/{id}")
    public ResponseEntity<Review> updateReviewCompany(@PathVariable Long id, @RequestBody Review reviewCompanyDetails){
        Review reviewCompany = reviewCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review company not exist with id :" + id));

        reviewCompany.setCompany(reviewCompanyDetails.getCompany());
        reviewCompany.setFeedback(reviewCompanyDetails.getFeedback());
        reviewCompany.setGrade((reviewCompanyDetails.getGrade()));

        Review updatedReviewCompany = reviewCompanyRepository.save(reviewCompany);
        return ResponseEntity.ok(updatedReviewCompany);
    }
     */

    @DeleteMapping("/reviewsCompany/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteReviewCompany(@PathVariable Long id) {
        Company_review reviewCompany = reviewCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review company not exist with id :" + id));

        reviewCompanyRepository.delete(reviewCompany);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
