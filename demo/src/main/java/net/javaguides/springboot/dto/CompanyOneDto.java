package net.javaguides.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.javaguides.springboot.model.Vacancy;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CompanyOneDto {
    private String inn;
    private String kpp;
    private String ogrn;
    private String address;
    private String director;
    private Date date_reg;
    @JsonIgnoreProperties({"company"})
    private List<Vacancy> vacancies;

    public CompanyOneDto(String inn, String kpp, String ogrn, String address, String director, Date date_reg, List<Vacancy> vacancies) {
        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.address = address;
        this.director = director;
        this.date_reg = date_reg;
        this.vacancies = vacancies;
    }
}
