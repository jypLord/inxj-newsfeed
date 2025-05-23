package inxj.newsfeed.domain.friend.repository;

import inxj.newsfeed.domain.friend.entity.FriendRequest;
import inxj.newsfeed.domain.friend.entity.Status;
import inxj.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendRequest, Long> {
    @Query("select f from FriendRequest f where (f.requester = :user or f.receiver = :user) and f.status = :status")
    List<FriendRequest> findByUserAndStatus(User user, Status status);

    @Query("select f from FriendRequest f where (f.receiver = :user1 and f.requester = :user2) " +
            "or (f.receiver = :user2 and f.requester = :user1)")
    Optional<FriendRequest> findInteractiveRequest(@Param("user1") User user1, @Param("user2")User user2);

    Optional<FriendRequest> findByReceiverAndRequester(User receiver, User requester);

    List<FriendRequest> findByRequesterAndStatusIn(User requester, Collection<Status> statuses);

    List<FriendRequest> findByReceiverAndStatus(User user, Status status);
}
