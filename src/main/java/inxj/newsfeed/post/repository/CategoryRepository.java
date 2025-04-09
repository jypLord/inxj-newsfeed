package inxj.newsfeed.post.repository;

import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  // 이미 저장된 속성인지 확인
  boolean existsByCategoryType(CategoryType type);

  // 카테고리 타입으로 카테고리 조회
  Category findCategoryByCategoryType(CategoryType type);

  List<Category> findByCategoryTypeIn(List<CategoryType> categoryTypeList);
}
