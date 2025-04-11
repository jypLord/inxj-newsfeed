package inxj.newsfeed.post.entity;

import inxj.newsfeed.common.entity.BaseEntity;
import inxj.newsfeed.post.converter.StringListConverter;
import inxj.newsfeed.post.dto.PostCreateRequestDto;
import inxj.newsfeed.post.dto.PostUpdateRequestDto;
import inxj.newsfeed.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
@Table(name="post")
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user; // 사용자와 N:1 연관관계 (사용자 id 맵핑)

  @Column(nullable = false)
  private String content;   // 내용

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "post_category",
      joinColumns = @JoinColumn(name="category_id"),
      inverseJoinColumns = @JoinColumn(name="post_id")
  )
  private List<Category> categoryIds;  // 카테고리

  @Convert(converter = StringListConverter.class)
  private List<String> imgUrls; // 이미지 URLs

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Visibility visibility;  // 공개 범위(Enum String 값 저장)

  public Post(PostCreateRequestDto requestDTO, User user, List<Category> categoryList) {
    this.user = user;
    this.content = requestDTO.getContent();
    this.categoryIds = categoryList;
    this.imgUrls = requestDTO.getImgUrls();
    this.visibility = Visibility.valueOf(requestDTO.getVisibility());
  }

  // 업데이트 포스트
  public void update(PostUpdateRequestDto requestDTO, List<Category> categoryList) {
    // 변경 사항이 있다면 업데이트
    if(requestDTO.getContent() != null) {
      this.content = requestDTO.getContent();
    }
    if(categoryList != null) {
      this.categoryIds = categoryList;
    }
    if(requestDTO.getImgUrls() != null) {
      this.imgUrls = requestDTO.getImgUrls();
    }
    if(requestDTO.getVisibility() != null) {
      this.visibility = Visibility.valueOf(requestDTO.getVisibility());
    }
  }
}
