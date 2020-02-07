package life.ls.community.enums;

/**
 * 评论的状态
 */
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1);

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    NotificationStatusEnum(Integer status) {
        this.status = status;
    }

}
