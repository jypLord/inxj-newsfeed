package inxj.newsfeed.domain.post.dto;

import inxj.newsfeed.common.annotation.ValidEnum;
import inxj.newsfeed.domain.post.entity.Visibility;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Valid
@AllArgsConstructor
public class PostCreateRequestDto {
  @Size(min=1, max=1000, message = "게시글은 1000글자 이내여야 합니다.")
  @NotBlank(message = "게시글 내용은 필수값 입니다.")
  private final String content;

  private final List<String> imgUrls;

  @NotBlank(message = "카테고리는 필수값 입니다.")
  private final List<String> categoryTypes;

  @NotBlank(message = "공개범위는 필수값 입니다.")
  @ValidEnum(target = Visibility.class)
  private final String visibility;
}
