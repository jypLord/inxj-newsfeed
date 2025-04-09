package inxj.newsfeed.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;    // 카테고리 식별자

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  private CategoryType categoryType;      // 카테고리 이름

  public Category(CategoryType categoryType) {
    this.categoryType = categoryType;
  }
}
