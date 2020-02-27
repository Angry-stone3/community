package life.ls.community.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论的实体类
 */
@Data
public class Comment implements Serializable {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Date gmtCreate;
    private Date gmtModified;
    private Integer likeCount;
    private String content;
    private Integer commentCount;
}