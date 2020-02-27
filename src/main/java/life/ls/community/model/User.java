package life.ls.community.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户的实体类
 */
@Data
public class User implements Serializable {
    private Long id;
    private String accountId;
    private String name;
    private String token;
    private Date gmtCreate;
    private Date gmtModified;
    private String avatarUrl;
}
