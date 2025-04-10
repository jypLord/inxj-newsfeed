package inxj.newsfeed.post.dto;

import inxj.newsfeed.common.annotation.ValidEnum;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.entity.Visibility;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateRequestDto {
  // TODO: List<Category> 검증 로직 추가
  @Size(min=0, max=1000, message = "게시글은 1000글자 이내여야 합니다.")
  private final java.lang.String content;

  private final List<java.lang.String> imgUrls;

  private final List<CategoryType> categoryTypes;

  @ValidEnum(target = Visibility.class)
  private final String visibility;
}
