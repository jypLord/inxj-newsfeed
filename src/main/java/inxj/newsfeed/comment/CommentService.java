package comment;

import inxj.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
    //생성
    public void saveComment(Long id,Long postId,RequestDto requestDto){
//        User user= userRepository.findUserById(id)
//        .orElseThrow(()->new RuntimeException("나중에 custom으로해요"));
//
//        Post post= postRepository.findPostById(postId)
//        .orElseThrow(()->new RuntimeException("나중에 custom으로해요"));

        CommentEntity commentEntity=new CommentEntity(user,post,requestDto.comment);

        commentRepository.save(commentEntity);
    }
    //전체조회 paging 할까요?
    @Transactional(readOnly = true)
    public List<ResponseDto> findComment(){
        return commentRepository.findAll().stream()
                .map(commentEntity ->new ResponseDto(commentEntity.getId(),
                        commentEntity.getContent(),
                        commentEntity.getCreatedAt(),
                        commentEntity.getUpdatedAt()))
                .toList();
    }
    //수정
    @Transactional
    public void updateComment(Long userId,Long commentId,ResponseDto responseDto){

        CommentEntity commentEntity=commentRepository.findById(commentId).
                orElseThrow(()->new RuntimeException("나중에 custom으로해요"));

        if (userId != commentEntity.getUser().getId()){
            throw new RuntimeException("나중에 custom으로해요");
        }

        commentEntity.updateContent(responseDto.content);

    }
    //삭제
    public void deleteComment(Long userId,Long commentId){
        commentRepository.deleteById(commentId);
    }


}
