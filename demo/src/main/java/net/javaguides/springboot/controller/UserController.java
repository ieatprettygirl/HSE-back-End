
package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.dto.UserGetOneDTO;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Company;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.EmailService;
import net.javaguides.springboot.service.UserService;
import net.javaguides.springboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5432")
@RestController
// base URL
@RequestMapping("/api/")
public class UserController {

    private final EmailService emailService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    // register
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody User user) {
        String token = userService.registerUser(user.getLogin(), user.getPassword(), user.getRole());

        emailService.sendVerificationEmail(user.getLogin(), token);

        Map<String, Object> response = new HashMap<>();
        response.put("created", Boolean.TRUE);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/auth/confirm")
    public ResponseEntity<Map<String, Object>> confirmEmail(@RequestParam String token) {
        System.out.println(token);
        Map<String, Object> response = new HashMap<>();
        try {
            String email = jwtUtil.extractUsername(token);

            Optional<User> userOpt = userRepository.findByLogin(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setEnabled(true);
                userRepository.save(user);

                response.put("success", true);
                response.put("message", "User confirmed successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // login and authenticate
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid Map<String, String> loginRequest) {
        String login = loginRequest.get("login");
        String password = loginRequest.get("password");
        try {
            String token = userService.authenticateUser(login, password);

            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", Boolean.TRUE);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("authenticated", Boolean.FALSE);
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // get all users
    // @PreAuthorize("hasRole('ROLE_1')")
    // @GetMapping("/users")
    // public List<User> getAllUsers(){
    //     return userRepository.findAll();
    // }

    // get information about my user
    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (!existingUser.getLogin().equals(currentUsername)) {
            throw new RuntimeException("You can only update your own profile!");
        }
        return ResponseEntity.ok(existingUser);
    }

    // change in your profile (not @Valid else drop)
    @PutMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> updateOwnProfile(@PathVariable Long id, @RequestBody User user) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (!existingUser.getLogin().equals(currentUsername)) {
            throw new RuntimeException("You can only update your own profile!");
        }

        if (user.getLogin() != null && !user.getLogin().equals(existingUser.getLogin())) {
            if (userRepository.existsByLogin(user.getLogin())) {
                throw new RuntimeException("Login is already taken!");
            }
            existingUser.setLogin(user.getLogin());
        }

        if (user.getPassword() != null) { existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); }
        userRepository.save(existingUser);

        String newToken = jwtUtil.generateToken(existingUser.getLogin(), existingUser.getRole().getRole_id());

        Map<String, Object> response = new HashMap<>();
        response.put("user", existingUser);
        response.put("token", newToken);

        return ResponseEntity.ok(response);
    }

    // delete user rest api
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ROLE_1')")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + id));

        if (user.getLogin().equals(currentUsername)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You cannot delete yourself!");
            return ResponseEntity.status(403).body(response);
        }

        userRepository.delete(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User deleted successfully!");
        return ResponseEntity.ok(response);
    }

    // delete profile
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> deleteMyProfile(@PathVariable Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (!existingUser.getLogin().equals(currentUsername)) {
            throw new RuntimeException("You can only delete your own profile!");
        }
        userRepository.delete(existingUser);

        SecurityContextHolder.clearContext();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "You profile deleted successfully!");
        return ResponseEntity.ok(response);
    }
}
