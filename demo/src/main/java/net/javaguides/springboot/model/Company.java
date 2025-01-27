package net.javaguides.springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String inn;

    @NotNull
    private String kpp;

    @NotNull
    private String ogrn;

    @NotNull
    private String address;

    @NotNull
    private String director;

    @NotNull
    private Date dateRegister;

    private Boolean status = false;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public Company() {
    }

    public Company(String inn, String kpp, String ogrn, String address, String director, Date dateRegister, Boolean status, Role role) {
        super();
        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.address = address;
        this.director = director;
        this.dateRegister = dateRegister;
        this.status = status;
        this.role = role;
    }
}