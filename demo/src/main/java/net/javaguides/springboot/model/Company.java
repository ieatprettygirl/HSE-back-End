package net.javaguides.springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;

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

    public Company() {
    }

    public Company(String inn, String kpp, String ogrn, String address, String director, Date dateRegister, Boolean status) {
        super();
        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.address = address;
        this.director = director;
        this.dateRegister = dateRegister;
        this.status = status;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getDateRegister() {
        return dateRegister;
    }

    public String getDirector() {
        return director;
    }

    public String getAddress() {
        return address;
    }

    public String getOgrn() {
        return ogrn;
    }

    public String getKpp() {
        return kpp;
    }

    public String getInn() {
        return inn;
    }
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    public void setInn(String inn) {
        this.inn = inn;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }
}