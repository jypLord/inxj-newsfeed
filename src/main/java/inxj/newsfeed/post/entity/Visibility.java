package inxj.newsfeed.post.entity;

public enum Visibility {
  PUBLIC("전체 공개"), FRIENDS("친구 공개");

  private java.lang.String label;   // 라벨

  Visibility(java.lang.String label) {
    this.label = label;
  }
}