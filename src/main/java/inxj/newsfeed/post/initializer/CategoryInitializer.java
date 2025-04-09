package inxj.newsfeed.post.initializer;

import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CategoryInitializer {

  private final CategoryRepository categoryRepository;

  // 스프링 어플리케이션 실행 시 단 한번 실행하여 카테고리 DB에 초기값 생성
  @PostConstruct
  public void initCategories() {
    if(categoryRepository.count() == 0) {
      for(CategoryType type:CategoryType.values()) {
        // 중복 저장이 아니라면
        if(!categoryRepository.existsByCategoryType(type)) {
          categoryRepository.save(new Category(type));
        }
      }
    }
  }
}
