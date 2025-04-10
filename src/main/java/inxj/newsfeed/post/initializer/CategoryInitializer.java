package inxj.newsfeed.post.initializer;

import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.repository.PostCategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CategoryInitializer {

  private final PostCategoryRepository postCategoryRepository;

  // 스프링 어플리케이션 실행 시 단 한번 실행하여 카테고리 DB에 초기값 생성
  @PostConstruct
  public void initCategories() {
    if(postCategoryRepository.count() == 0) {
      for(CategoryType type:CategoryType.values()) {
        // 중복 저장이 아니라면
        if(!postCategoryRepository.existsByCategoryType(type)) {
          postCategoryRepository.save(new Category(type));
        }
      }
    }
  }
}
