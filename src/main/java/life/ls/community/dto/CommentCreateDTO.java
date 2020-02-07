package life.ls.community.dto;

import lombok.Data;

/**
 * Data Transfer Object(DTO)
 * 数据传输对象
 */
@Data
public class CommentCreateDTO {
    private String content;
    private Integer type;
    private Long parentId;
}
