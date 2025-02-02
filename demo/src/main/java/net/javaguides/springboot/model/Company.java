package net.javaguides.springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;

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
    private Date date_reg;

    private Boolean is_accepted = false;

    public Company() {
    }

    public Company(String inn, String kpp, String ogrn, String address, String director, Date dateRegister, Boolean is_accepted) {
        super();
        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.address = address;
        this.director = director;
        this.date_reg = dateRegister;
        this.is_accepted = is_accepted;
    }
}