package net.javaguides.springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company_review")
public class Company_review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_review_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Size(max = 1000)
    private String feedback;

    @Min(0)
    @Max(5)
    @NotNull
    private int grade;

    public Company_review() {
    }

    public Company_review(String feedback, int grade, User user, Employee employee) {
        this.feedback = feedback;
        this.grade = grade;
        this.user = user;
        this.employee = employee;
    }
}