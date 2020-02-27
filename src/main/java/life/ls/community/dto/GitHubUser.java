package life.ls.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Transfer Object(DTO)
 * github的user信息
 */
@Data
public class GitHubUser implements Serializable {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
}
