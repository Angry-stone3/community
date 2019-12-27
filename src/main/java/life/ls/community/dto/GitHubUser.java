package life.ls.community.dto;

import lombok.Data;

/**
 * Data Transfer Object(DTO)
 * github的user信息
 */
@Data
public class GitHubUser {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
}
