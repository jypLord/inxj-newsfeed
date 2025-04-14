package inxj.newsfeed.domain.post.service;

import inxj.newsfeed.common.util.EntityFetcher;
import inxj.newsfeed.domain.friend.repository.FriendRepository;
import inxj.newsfeed.domain.post.dto.PostCreateRequestDto;
import inxj.newsfeed.domain.post.dto.PostResponseDto;
import inxj.newsfeed.domain.post.dto.PostUpdateRequestDto;
import inxj.newsfeed.domain.post.entity.Category;
import inxj.newsfeed.domain.post.entity.Post;
import inxj.newsfeed.domain.post.entity.Visibility;
import inxj.newsfeed.domain.post.repository.PostRepository;
import inxj.newsfeed.domain.user.entity.User;
import inxj.newsfeed.domain.user.repository.UserRepository;
import inxj.newsfeed.exception.customException.BaseException;
import inxj.newsfeed.exception.customException.ForbiddenPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static inxj.newsfeed.exception.ErrorCode.*;
import static inxj.newsfeed.domain.friend.entity.Status.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostCategoryService categoryService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final EntityFetcher entityFetcher;

    // 게시글 작성
    public void save(PostCreateRequestDto requestDTO, Long userId) {
        User user = entityFetcher.getUserOrThrow(userId); // 사용자 찾기
        List<Category> categoryList = categoryService.getCategoryByTypeString(requestDTO.getCategoryTypes()); // CategoryType --> Category 변환
        postRepository.save(new Post(requestDTO, user, categoryList));
    }

    // 게시글 단건 조회
    public PostResponseDto find(Long postId, Long loginId) {
        Post post = entityFetcher.getPostOrThrow(postId);

        // 다른 사용자의 친구 공개 게시글
        if (post.getVisibility() == Visibility.FRIENDS && !(post.getUser().getId().equals(loginId))) {
            User loginUser = entityFetcher.getUserOrThrow(loginId); // 사용자 찾기
            List<User> friendList = friendRepository.findByUserAndStatus(loginUser, ACCEPT).stream()
                .map(friendRequest ->
                    friendRequest.getReceiver().equals(loginUser) ? friendRequest.getRequester() : friendRequest.getReceiver())
                .toList();

            // 게시글 작성자와 친구가 아니라면
            if (!friendList.contains(post.getUser())) {
                throw new ForbiddenPostException();
            }
        }
        return new PostResponseDto(post);
    }

    // 모든 전체 공개 게시글 조회
    public List<PostResponseDto> findAllPublicPosts(int page, int size) {
        // 전체 공개 게시글에 대하여 작성일자 내림차순 조회
        List<Post> postList = postRepository.findAllByVisibility(Visibility.PUBLIC);
        return postList.stream()
            .map(PostResponseDto::new).toList();
    }

    // 모든 친구 공개 게시글 조회
    public List<PostResponseDto> findAllFriendPosts(Long loginId, int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());
        User user = entityFetcher.getUserOrThrow(loginId); // 사용자 찾기

        // 친구 목록 조회
        List<User> friendList = friendRepository.findByUserAndStatus(user, ACCEPT).stream()
            .map(friendRequest ->
                friendRequest.getReceiver().equals(user) ? friendRequest.getRequester() : friendRequest.getReceiver())
            .toList();

        // 친구인 사용자들의 모든 친구 공개 게시글 조회
        Page<Post> postPage= postRepository.findAllFriendVisible(Visibility.FRIENDS, friendList, pageable);
        return postPage.stream()
            .map(PostResponseDto::new).toList();
    }

    // 사용자의 모든 게시글 조회
    public List<PostResponseDto> findAllUserPosts(Long targetUserId, Long loginId, int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());
        Page<Post> postPage;
        User targetUser = entityFetcher.getUserOrThrow(targetUserId);

        // 자신의 모든 게시글을 조회
        if (targetUserId.equals(loginId)) {
            postPage = postRepository.findAllByUser(targetUser, pageable);
        }
        // 다른 유저의 모든 게시글을 조회
        else {
            User loginUser = entityFetcher.getUserOrThrow(loginId);
            List<User> friendList = friendRepository.findByUserAndStatus(loginUser, ACCEPT).stream()
                .map(friendRequest ->
                    friendRequest.getReceiver().equals(loginUser) ? friendRequest.getRequester() : friendRequest.getReceiver())
                .toList();

            // 친구라면 전체 공개와 친구 공개 게시글 조회
            if (friendList.contains(targetUser)) {
                postPage = postRepository.findAllByUser(targetUser, pageable);
            }
            // 전체 공개 게시글만 조회
            else {
                postPage = postRepository.findAllByUserAndVisibility(targetUser, Visibility.PUBLIC, pageable);
            }
        }

        return postPage.stream()
            .map(PostResponseDto::new).toList();
    }

    // 모든 전체 공개 게시글을 카테고리 별로 조회
    public List<PostResponseDto> findAllPublicPostsByCategories(List<String> categoryTypeList, int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());

        // CategoryType 중복값은 JPA IN 쿼리에서 자동으로 무시
        List<Category> categoryList = categoryService.getCategoryByTypeString(categoryTypeList); // CategoryType --> Category 변환

        // 모든 전체 공개 게시글을 카테고리 별로 조회
        Page<Post> postPage = postRepository.findAllByCategoryAndVisibility(Visibility.PUBLIC, categoryList, pageable);

        return postPage.stream()
            .map(PostResponseDto::new).toList();
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDto requestDTO, Long loginId) {
        Post targetPost = entityFetcher.getPostOrThrow(postId); // 게시글 찾기
        User targetUser = targetPost.getUser();

        // 수정 대상 게시글 작성자가 현재 로그인한 사용자와 동일하다면
        if (loginId.equals(targetUser.getId())) {
            List<Category> categoryList = categoryService.getCategoryByTypeString(requestDTO.getCategoryTypes()); // CategoryType --> Category 변환
            targetPost.update(requestDTO, categoryList);    // 업데이트
        }
        // 일치하지 않는 경우
        else {
            throw new ForbiddenPostException();
        }
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(Long postId, Long loginId) {
        Post targetPost = entityFetcher.getPostOrThrow(postId); // 게시글 찾기
        // 수정 대상 게시글 작성자가 현재 로그인한 사용자와 동일하다면
        if (loginId.equals(targetPost.getUser().getId())) {
            postRepository.delete(targetPost);
        }
        // 일치하지 않는 경우
        else {
            throw new ForbiddenPostException();
        }
    }
}
