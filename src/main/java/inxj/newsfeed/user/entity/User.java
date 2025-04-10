package inxj.newsfeed.user.entity;

import inxj.newsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String username;

    private String name;

    private LocalDate birthday;

    private String gender;

    @Column(unique = true)
    private String phoneNumber;

    private String profileImageUrl;

    private LocalDateTime deletedAt;

}