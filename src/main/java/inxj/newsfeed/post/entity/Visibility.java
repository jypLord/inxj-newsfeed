package inxj.newsfeed.post.entity;

public enum Visibility {
  PUBLIC("전체 공개"), FRIENDS("친구 공개");

  private
  String label;   // 라벨

  Visibility(String label) {
    this.label = label;
  }
}