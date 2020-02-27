package life.ls.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Transfer Object(DTO)
 * 数据传输对象
 */
@Data
public class AccessTokenDTO implements Serializable {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
