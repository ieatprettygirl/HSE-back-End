package net.javaguides.springboot.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil; // Добавьте JwtUtil

    // Конструктор для инъекции зависимостей
    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }


    public String registerUser(String login, String password, Role role) {

        if (userRepository.existsByLogin(login)) {
            throw new RuntimeException("A user with this login already exists!");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(login, hashedPassword, role);
        Optional<Role> optionalRole = roleRepository.findById(2L);

        if (optionalRole.isPresent()) {
            user.setRole(optionalRole.get());
        } else {
            throw new RuntimeException("Role not found");
        }

        userRepository.save(user);

        return jwtUtil.generateToken(user.getLogin(), user.getRole().getRole_id());
    }

    public String authenticateUser(String login, String password) {
        User user = userRepository.findByLogin(login)
               .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getLogin(), user.getRole().getRole_id());
    }
}