package life.ls.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Transfer Object(DTO)
 */
@Data
public class FileDTO implements Serializable {
    private int success;
    private String message;
    private String url;
}
