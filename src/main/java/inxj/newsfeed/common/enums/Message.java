package inxj.newsfeed.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Message {

    NEED_LOGIN("로그인이 필요합니다."),
    NO_ELEMENT("해당하는 항목이 없습니다.");

    private final String msg;

    public String get() {
        return msg;
    }

}
