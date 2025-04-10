package inxj.newsfeed.post.dto;

import inxj.newsfeed.common.annotation.ValidEnum;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.entity.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequestDto {
  // TODO: List<Category> 검증 로직 추가
  // TODO: Enum Visibility 검증 로직 추가 (json 파싱 오류 발생 가능성, 커스텀 어노테이션 혹은 따로 검증 로직)
  @Size(min=1, max=1000, message = "게시글은 1000글자 이내여야 합니다.")
  @NotBlank(message = "게시글 내용은 필수값 입니다.")
  private final java.lang.String content;

  private final List<java.lang.String> imgUrls;

  @NotBlank(message = "카테고리는 필수값 입니다.")
  private final List<CategoryType> categoryTypes;

  @NotBlank(message = "공개범위는 필수값 입니다.")
  @ValidEnum(target = Visibility.class)
  private final Visibility string;
}
