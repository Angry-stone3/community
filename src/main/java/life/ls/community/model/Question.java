package life.ls.community.model;

import lombok.Data;

import java.util.Date;

/**
 * 问题的实体类
 * 发帖子时保存的信息
 */
@Data
public class Question {
    private Long id;
    private String title;
    private String description;
    private Date gmtCreate;
    private Date gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
}
