package inxj.newsfeed.domain.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "category")
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
