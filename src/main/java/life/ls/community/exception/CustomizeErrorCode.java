package life.ls.community.exception;

/**
 * 通用配置自定义异常
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "你找到问题不在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中问题或者评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录"),
    SYSTEM_ERROR(2004, "服务器冒烟了，要不然你稍后试试！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    COMMENT_IS_EMPTY(2007, "评论的内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"兄弟你这是别人的信息呢?"),
    NOTIFICATION_NOT_FOUND(2009,"哎呦！通知不存在了"),
    MESSAGE_NOT_FOUND(2010,"出错啦！消息莫非不翼而飞");

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
