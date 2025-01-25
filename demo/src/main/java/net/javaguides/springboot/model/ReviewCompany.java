package net.javaguides.springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "reviewsCompany")
public class ReviewCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Size(max = 1000)
    private String feedback;

    @Min(0)
    @Max(5)
    @NotNull
    private int grade;

    public ReviewCompany() {
    }

    public ReviewCompany(String feedback, int grade, Company company) {
        this.feedback = feedback;
        this.grade = grade;
        this.company = company;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public int getGrade() {
        return grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public Company getCompany() {
        return company;
    }

}