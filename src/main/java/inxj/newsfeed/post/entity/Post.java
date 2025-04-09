package inxj.newsfeed.post.entity;

import baseEntity.BaseEntity;
import inxj.newsfeed.post.converter.StringListConverter;
import inxj.newsfeed.post.dto.PostCreateRequestDTO;
import inxj.newsfeed.user.User;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
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

  @OneToMany(fetch = FetchType.LAZY)
  private List<Category> categories;  // 카테고리

  @Convert(converter = StringListConverter.class)
  private List<String> imgUrls; // 이미지 URLs

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Visibility visibility;  // 공개 범위(Enum String 값 저장)

  public Post(PostCreateRequestDTO requestDTO, User user) {
    this.user = user;
    this.content = requestDTO.getContent();
    this.categories = requestDTO.getCategories();
    this.imgUrls = requestDTO.getImgUrls();
    this.visibility = requestDTO.getVisibility();
  }
}
