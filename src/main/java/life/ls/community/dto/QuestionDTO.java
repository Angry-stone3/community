package life.ls.community.dto;

import life.ls.community.model.Comment;
import life.ls.community.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object(DTO)
 */
@Data
public class QuestionDTO implements Serializable {
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
    private User user;
    //对问题的评论
    private List<CommentDTO> comments;

}
