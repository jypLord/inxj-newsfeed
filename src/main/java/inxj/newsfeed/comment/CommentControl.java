package comment;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentControl {
    private final CommentService commentService;
    //생성
    @PostMapping()
    public ResponseEntity<>saveComment(@RequestBody RequestDto requestDto,
                                       HttpServletRequest httpServletRequest,
                                       @PathVariable Long postId){
        Long id= 1L;//임시
//        Long id=(Long)httpServletRequest.getSession().getAttribute("수정필요");
        commentService.saveComment(id,postId,requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    //조회
    @GetMapping()
    public ResponseEntity<List<ResponseDto>> findComment(){

        return new ResponseEntity<>(commentService.findComment(),HttpStatus.OK);
    }
    //수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<> updateComment(@PathVariable Long commentId,
                                          HttpServletRequest httpServletRequest,
                                          @PathVariable Long postId) {
//        Long userId=(Long)httpServletRequest.getSession().getAttribute("수정필요");

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<> deleteComment(@PathVariable Long commentId,
                                          HttpServletRequest httpServletRequest) {
//        Long userId=(Long)httpServletRequest.getSession().getAttribute("수정필요");
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
