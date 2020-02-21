package life.ls.community.dto;

import life.ls.community.exception.CustomizeException;
import life.ls.community.exception.ICustomizeErrorCode;
import lombok.Data;

/**
 * 返回到页面的信息封装
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(ICustomizeErrorCode customizeErrorCode) {
        return errorOf(customizeErrorCode.getCode(), customizeErrorCode.getMessage());
    }

    public static <T>ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

    public static ResultDTO okOf() {
        return errorOf(200,"请求成功");
    }

    public static ResultDTO errorOf(CustomizeException e) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(e.getCode());
        resultDTO.setMessage(e.getMessage());
        return resultDTO;
    }

    public static ResultDTO stopOf() {
        return errorOf(400,"你已经被禁言,请联系管理员");
    }


}
