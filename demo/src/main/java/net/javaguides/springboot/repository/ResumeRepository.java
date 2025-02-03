package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Company_review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Company_review, Long> {
}
