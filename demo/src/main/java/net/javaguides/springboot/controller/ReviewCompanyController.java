package net.javaguides.springboot.controller;
import java.util.*;

import jakarta.validation.Valid;
import net.javaguides.springboot.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.ReviewCompany;
import net.javaguides.springboot.repository.ReviewCompanyRepository;

@CrossOrigin(origins = "http://localhost:5432")
@RestController
// base URL
@RequestMapping("/api/v1/")
public class ReviewCompanyController {

    private final ReviewCompanyRepository reviewCompanyRepository;

    @Autowired
    public ReviewCompanyController(ReviewCompanyRepository reviewCompanyRepository) {
        this.reviewCompanyRepository = reviewCompanyRepository;
    }


    @GetMapping("/reviewsCompany")
    public List<ReviewCompany> getAllReviewsCompany(){
        return reviewCompanyRepository.findAll();
    }

   /* @PostMapping("/reviewsCompany")
    public ReviewCompany createReviewCompany(@RequestBody ReviewCompany ReviewCompany) {
        /* return reviewCompanyRepository.save(ReviewCompany);
    }
    */
   @PostMapping("/reviewsCompany")
   public ResponseEntity<Map<String, Boolean>> createReviewCompany(@Valid @RequestBody ReviewCompany reviewCompany) {
       reviewCompanyRepository.save(reviewCompany);

       Map<String, Boolean> response = new HashMap<>();
       response.put("created", Boolean.TRUE);
       return ResponseEntity.ok(response);
   }

    @GetMapping("/reviewsCompany/{id}")
    public ResponseEntity<ReviewCompany> getReviewCompanyById(@PathVariable Long id) {
        ReviewCompany company = reviewCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review company not exist with id :" + id));
        return ResponseEntity.ok(company);
    }

    @PutMapping("/reviewsCompany/{id}")
    public ResponseEntity<ReviewCompany> updateReviewCompany(@PathVariable Long id, @RequestBody ReviewCompany reviewCompanyDetails){
        ReviewCompany reviewCompany = reviewCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review company not exist with id :" + id));

        reviewCompany.setCompany(reviewCompanyDetails.getCompany());
        reviewCompany.setFeedback(reviewCompanyDetails.getFeedback());
        reviewCompany.setGrade((reviewCompanyDetails.getGrade()));

        ReviewCompany updatedReviewCompany = reviewCompanyRepository.save(reviewCompany);
        return ResponseEntity.ok(updatedReviewCompany);
    }

    @DeleteMapping("/reviewsCompany/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteReviewCompany(@PathVariable Long id){
        ReviewCompany reviewCompany = reviewCompanyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review company not exist with id :" + id));

        reviewCompanyRepository.delete(reviewCompany);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
