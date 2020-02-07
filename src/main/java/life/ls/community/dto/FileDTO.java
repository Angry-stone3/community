package life.ls.community.dto;

import lombok.Data;

/**
 * Data Transfer Object(DTO)
 */
@Data
public class FileDTO {
    private int success;
    private String message;
    private String url;
}
