package inxj.newsfeed.friend.repository;

import inxj.newsfeed.friend.entity.FriendRequest;
import inxj.newsfeed.friend.entity.Status;
import inxj.newsfeed.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendRequest, Long> {
    List<User> findByRequesterOrReceiverAndStatus(User user, User user1, Status status);

    @Query("select f from FriendRequest f where (f.receiver = :loginedUser and f.requester = :friend) or (f.receiver = :friend and f.requester = :loginedUser)")
    Optional<FriendRequest> findInteractiveRequest(@Param("loginedUser") User loginedUser, @Param("friend")User friend);

    Optional<FriendRequest> findByReceiverAndRequester(User receiver, User requester);
}
