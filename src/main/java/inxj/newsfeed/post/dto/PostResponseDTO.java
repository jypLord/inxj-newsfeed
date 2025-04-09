package inxj.newsfeed.post.dto;

import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.entity.Post;
import inxj.newsfeed.post.entity.Visibility;
import java.util.List;
import lombok.Getter;

@Getter
public class PostResponseDTO {
  private final String content;
  private final List<String> imgUrls;
  private final List<CategoryType> categoryTypes;
  private final Visibility visibility;

  public PostResponseDTO (Post post) {
    this.content = post.getContent();
    this.imgUrls = post.getImgUrls();
    this.categoryTypes = post.getCategoryIds().stream()
        .map(Category::getCategoryType).toList();
    this.visibility = post.getVisibility();
  }
}
