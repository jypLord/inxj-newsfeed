package inxj.newsfeed.friend.repository;

import inxj.newsfeed.friend.entity.FriendRequest;
import inxj.newsfeed.friend.entity.Status;
import inxj.newsfeed.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<FriendRequest, Long> {
    List<User> findByRequesterOrReceiverAndStatus(User user, User user1, Status status);
}
