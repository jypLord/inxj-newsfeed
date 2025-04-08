package inxj.newsfeed.friend.entity;

public enum Status {
    ACCEPT("요청 수락"),
    REJECT("요청 거절"),
    PENDING("요청 보류");

    private final String label;

    Status(String label){
        this.label = label;
    }
}
