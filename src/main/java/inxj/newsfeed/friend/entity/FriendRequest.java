package inxj.newsfeed.friend.entity;

import baseEntity.BaseEntity;
import inxj.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static inxj.newsfeed.friend.entity.Status.PENDING;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friend_request")
public class FriendRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = PENDING;
}
