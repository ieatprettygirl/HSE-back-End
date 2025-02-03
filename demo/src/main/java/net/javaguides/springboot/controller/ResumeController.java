package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.dto.ResumeDTO;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Resume;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.ResumeRepository;
import net.javaguides.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5432")
@RestController
// base URL
@RequestMapping("/api/")
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ResumeController(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    // create resume from yourself
    @PostMapping("/resume")
    public ResponseEntity<Map<String, Object>> createResume(@Valid @RequestBody Resume resume, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByLogin(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized"));
        }
        resume.setUser(currentUser);

        if (resume.getUser().getResume() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User already has a resume!"));
        }
        resume.setUser(resume.getUser());
        resume.setEducation(resume.getEducation());
        resume.setSkills(resume.getSkills());
        resume.setBirthday(resume.getBirthday());
        resume.setContact(resume.getContact());
        resume.setGender(resume.getGender());
        resume.setFull_name(resume.getFull_name());
        resume.setDescription(resume.getDescription());

        resumeRepository.save(resume);
        Map<String, Object> response = new HashMap<>();
        response.put("created", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // get resume
    @GetMapping("/resume/{id}")
    public ResponseEntity<ResumeDTO> getResume(@PathVariable Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found!"));
        ResumeDTO resumeDTO = getResumeDTO(resume);
        return ResponseEntity.ok(resumeDTO);
    }

    // update resume
    @PutMapping("/resume/{id}")
    public ResponseEntity<Map<String, Object>> updateResume(@PathVariable Long id, @Valid @RequestBody Resume resume, Authentication authentication) {
        User currentUser = getUserAuth(authentication);
        if (currentUser == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized"));
        }
        Resume existingResume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found!"));

        if (!existingResume.getUser().getUser_id().equals(currentUser.getResume().getResume_id())) {
            return ResponseEntity.status(403).body(Map.of("error", "You can only update your own resume!"));
        }

        existingResume.setSkills(resume.getSkills());
        existingResume.setBirthday(resume.getBirthday());
        existingResume.setContact(resume.getContact());
        existingResume.setGender(resume.getGender());
        existingResume.setFull_name(resume.getFull_name());
        existingResume.setDescription(resume.getDescription());
        existingResume.setEducation(resume.getEducation());

        resumeRepository.save(existingResume);

        ResumeDTO resumeDTO = getResumeDTO(existingResume);

        Map<String, Object> response = new HashMap<>();
        response.put("updated", Boolean.TRUE);
        response.put("resume", resumeDTO);
        return ResponseEntity.ok(response);
    }

    // delete
    @DeleteMapping("/resume/{id}")
    public ResponseEntity<Map<String, Object>> deleteMyResume(@PathVariable Long id, Authentication authentication) {
        User currentUser = getUserAuth(authentication);
        if (currentUser == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized"));
        }
        Resume resumeToDel = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found!"));

        if (!resumeToDel.getUser().getUser_id().equals(currentUser.getResume().getResume_id())) {
            return ResponseEntity.status(403).body(Map.of("error", "You can only delete your own resume!"));
        }

        // DELETE связь user с resume
        return getMapResponseEntity(resumeToDel, currentUser);
    }

    // delete resume by admin
    @DeleteMapping("/admin/resume/{id}")
    @PreAuthorize("hasRole('ROLE_1')")
    public ResponseEntity<Map<String, Object>> deleteResumeByAdmin(@PathVariable Long id, Authentication authentication) {
        Resume resumeToDel = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found!"));

        User user = resumeToDel.getUser();
        return getMapResponseEntity(resumeToDel, user);
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(Resume resumeToDel, User user) {
        user.setResume(null);
        userRepository.save(user);

        resumeRepository.delete(resumeToDel);

        Map<String, Object> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        response.put("resume", resumeToDel);
        response.put("message", "Resume deleted successfully!");
        return ResponseEntity.ok(response);
    }

    public ResumeDTO getResumeDTO(Resume resume) {
        return new ResumeDTO(
                resume.getEducation(),
                resume.getSkills(),
                resume.getBirthday(),
                resume.getGender(),
                resume.getFull_name(),
                resume.getContact(),
                resume.getDescription());
    }

    public User getUserAuth(Authentication authentication) {
        String currentUsername = authentication.getName();
        return userRepository.findByLogin(currentUsername).orElse(null);
    }
}
