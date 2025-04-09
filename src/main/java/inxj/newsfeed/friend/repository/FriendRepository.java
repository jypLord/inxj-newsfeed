package inxj.newsfeed.friend.repository;

import inxj.newsfeed.friend.entity.FriendRequest;
import inxj.newsfeed.friend.entity.Status;
import inxj.newsfeed.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendRequest, Long> {
    List<User> findByRequesterOrReceiverAndStatus(User user, User user1, Status status);

    @Query("select f from FriendRequest f where (f.receiver = :loginedUser and f.requester = :friend) or (f.receiver = :friend and f.requester = :loginedUser)")
    Optional<FriendRequest> findInteractiveRequest(@Param("loginedUser") User loginedUser, @Param("friend")User friend);

    Optional<FriendRequest> findByReceiverAndRequester(User receiver, User requester);

    List<FriendRequest> findByRequester(User requester);

    List<FriendRequest> findByReceiver(User receiver);
}
