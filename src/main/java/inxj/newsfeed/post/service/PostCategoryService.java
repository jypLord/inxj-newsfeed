package inxj.newsfeed.post.service;

import static java.util.stream.Collectors.toList;

import inxj.newsfeed.post.entity.Category;
import inxj.newsfeed.post.entity.CategoryType;
import inxj.newsfeed.post.repository.PostCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCategoryService {
  private final PostCategoryRepository categoryRepository;

  // 카테고리 타입으로 카테고리 조회
  public List<Category> getCategoryByType(List<CategoryType> categoryTypeList) {
    return categoryRepository.findByCategoryTypeIn(categoryTypeList);
  }

  // 카테고리 타입으로 카테고리 조회
  public List<Category> getCategoryByTypeString(List<String> categoryTypeSTringList) {
    List<CategoryType> categoryTypeList = categoryTypeSTringList.stream()
        .map(CategoryType::valueOf).toList();
    return categoryRepository.findByCategoryTypeIn(categoryTypeList);
  }
}
