package life.ls.community.exception;

public class CustomizeException extends RuntimeException {
    private Integer code;
    private String message;

    public CustomizeException(CustomizeErrorCode customizeErrorCode) {
        this.code = customizeErrorCode.getCode();
        this.message = customizeErrorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
