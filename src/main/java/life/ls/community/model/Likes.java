package life.ls.community.model;

import lombok.Data;

@Data
public class Likes {
    private Long id;
    private Long commentId;
    private Long userId;
}
