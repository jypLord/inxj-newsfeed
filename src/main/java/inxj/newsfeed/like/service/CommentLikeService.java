package inxj.newsfeed.like.service;

import inxj.newsfeed.like.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentLikeService {

    private CommentLikeRepository commentLikeRepository;

    @Autowired
    public CommentLikeService(CommentLikeRepository commentLikeRepository) {
        this.commentLikeRepository = commentLikeRepository;
    }

}
