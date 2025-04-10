package inxj.newsfeed.post.dto;

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
  // TODO: Enum Visibility 검증 로직 추가 (json 파싱 오류 발생 가능성, 커스텀 어노테이션 혹은 따로 검증 로직)
  @Size(min=0, max=1000, message = "게시글은 1000글자 이내여야 합니다.")
  private final String content;

  private final List<String> imgUrls;

  private final List<CategoryType> categoryTypes;

  private final Visibility visibility;
}
