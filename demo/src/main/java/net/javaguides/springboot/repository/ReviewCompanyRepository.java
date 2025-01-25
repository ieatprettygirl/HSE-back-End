package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springboot.model.ReviewCompany;

public interface ReviewCompanyRepository extends JpaRepository<ReviewCompany, Long>{
}
