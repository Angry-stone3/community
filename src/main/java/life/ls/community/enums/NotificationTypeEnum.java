package life.ls.community.enums;

/**
 * 通知的类型
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");

    private String name;
    private Integer type;

    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    NotificationTypeEnum(Integer type, String name) {
        this.name = name;
        this.type = type;
    }

    //获取字符串
    public static String getNameByType(Integer type) {
        if (type == 1) {
            return REPLY_QUESTION.getName();
        } else {
            return REPLY_COMMENT.getName();
        }
    }
}
