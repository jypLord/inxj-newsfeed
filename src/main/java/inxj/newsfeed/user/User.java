package inxj.newsfeed.user;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
public class User extends BaseEntity{
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

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL)
    private List<Friend> fridends = new ArrayList<>();
}
