package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springboot.model.Company_review;

public interface UserReviewRepository extends JpaRepository<Company_review, Long>{
}
