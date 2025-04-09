package inxj.newsfeed.post.repository;

import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  // 이미 저장된 속성인지 확인
  boolean existsByCategoryType(CategoryType type);

  Category findCategoryByCategoryType(CategoryType type);
}
