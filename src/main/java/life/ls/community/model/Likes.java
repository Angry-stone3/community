package life.ls.community.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Likes implements Serializable {
    private Long id;
    private Long commentId;
    private Long userId;
}
