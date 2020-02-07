package life.ls.community.dto;

import life.ls.community.model.User;
import lombok.Data;

import java.util.Date;
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Date gmtCreate;
    private Date gmtModified;
    private Integer likeCount;
    private String content;
    private Integer commentCount;
    //评论人
    private User user;
}
