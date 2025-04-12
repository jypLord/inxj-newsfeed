package inxj.newsfeed.domain.post.dto;

import inxj.newsfeed.domain.post.entity.Category;
import inxj.newsfeed.domain.post.entity.CategoryType;
import inxj.newsfeed.domain.post.entity.Post;
import inxj.newsfeed.domain.post.entity.Visibility;
import java.util.List;
import lombok.Getter;

@Getter
public class PostResponseDto {
  private final String content;
  private final List<String> imgUrls;
  private final List<CategoryType> categoryTypes;
  private final Visibility visibility;

  public PostResponseDto(Post post) {
    this.content = post.getContent();
    this.imgUrls = post.getImgUrls();
    this.categoryTypes = post.getCategoryIds().stream()
        .map(Category::getCategoryType).toList();
    this.visibility = post.getVisibility();
  }
}
