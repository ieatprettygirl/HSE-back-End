package net.javaguides.springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String login;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @JsonProperty("full_name")
    @Column(name = "full_name")
    private String fullName;

    @NotNull
    private int age;

    private String education;

    @JsonProperty("middle_grade")
    @Column(name = "middle_grade")
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    private double middlegrade;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User() {
    }

    public User(String login, String password, String fullName, Role role, int age) {
        this.login = login;
        this.password = password;
        this.age = age;
        this.fullName = fullName;
        this.role = role;
    }
}
