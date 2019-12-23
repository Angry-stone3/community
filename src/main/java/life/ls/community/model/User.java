package life.ls.community.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户的实体类
 */
@Data
public class User {
    private String id;
    private String account_id;
    private String name;
    private String token;
    private Date gtm_create;
    private Date gtm_modified;
}
