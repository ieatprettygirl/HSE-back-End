package net.javaguides.springboot.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
public class Resume {
    @Id
    private Long resume_id;

    @OneToOne
    @MapsId // Используем user_id как resume_id
    @JoinColumn(name = "resume_id")
    private User user;

    @NotNull
    @Column(nullable = false)
    private String education;

    @NotNull
    @Column(nullable = false)
    private String skills;

    @NotNull
    @Column(nullable = false)
    private Date birthday;

    private String gender;

    @NotNull
    @Column(nullable = false)
    private String full_name;

    @NotNull
    @Column(nullable = false)
    private String contact;

    @Column(length = 1000)
    private String description;
}
