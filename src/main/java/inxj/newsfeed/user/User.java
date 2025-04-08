package inxj.newsfeed.user;

import inxj.newsfeed.common.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String username;

    private String name;

    private LocalDateTime birthday;

    private String gender;

    @Column(unique = true)
    private String phoneNumber;

    private String profileImageUrl;

    private LocalDateTime deletedAt;

}