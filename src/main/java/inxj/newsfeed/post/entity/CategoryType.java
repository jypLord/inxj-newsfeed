package inxj.newsfeed.post.entity;

public enum CategoryType {
  DAILY("일상"), TRAVEL("여행"), SPORTS("스포츠"), PET("반려동물"), IT("IT");

  private String label;

  CategoryType(String label) {
    this.label = label;
  }
}
