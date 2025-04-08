package inxj.newsfeed.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity{
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

    private LocalDateTime birthday;

    private String gender;

    @Column(unique = true)
    private String phoneNumber;

    private String profileImageUrl;

    private LocalDateTime deletedAt;
}