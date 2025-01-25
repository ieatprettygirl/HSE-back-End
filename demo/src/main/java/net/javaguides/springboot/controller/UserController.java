
package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.model.ReviewCompany;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5432")
@RestController
// base URL
@RequestMapping("/api/auth/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody User user) {
        String token = userService.registerUser(user.getLogin(), user.getPassword(), user.getAge(), user.getFullName(), user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("created", Boolean.TRUE);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
