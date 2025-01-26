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


    public String registerUser(String login, String password,int age,  String fullName, Role role) {
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(login, hashedPassword, fullName, role, age);
        Optional<Role> optionalRole = roleRepository.findById(2L);

        if (optionalRole.isPresent()) {
            user.setRole(optionalRole.get());
        } else {
            throw new RuntimeException("Role not found");
        }

        userRepository.save(user);

        return jwtUtil.generateToken(user.getLogin(), user.getRole().getId());
    }

    /* Метод для генерации токена (пример)
    private String generateToken(String login, Long roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("login", login); // Добавляем логин
        claims.put("role_id", roleId); // Добавляем ID роли

        return Jwts.builder()
                .setClaims(claims) // Устанавливаем данные в токен
                .setSubject(login) // Устанавливаем subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // Устанавливаем дату выдачи
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // время жизни токена (неделя)
                .signWith(SECRET_KEY) // Подписываем токен
                .compact(); // Создаем токен
    }
     */
}