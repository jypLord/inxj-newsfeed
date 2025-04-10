package inxj.newsfeed.friend.entity;

import inxj.newsfeed.common.entity.BaseEntity;
import inxj.newsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static inxj.newsfeed.friend.entity.Status.PENDING;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friend_request")
public class FriendRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = PENDING;

    public FriendRequest(User requester, User receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }
}
