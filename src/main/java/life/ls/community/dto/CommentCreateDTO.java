package life.ls.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Transfer Object(DTO)
 * 数据传输对象
 */
@Data
public class CommentCreateDTO implements Serializable {
    private String content;
    private Integer type;
    private Long parentId;
}
