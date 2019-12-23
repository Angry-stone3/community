package life.ls.community.dto;

import lombok.Data;

/**
 * Data Transfer Object(DTO)
 * 数据传输对象
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
