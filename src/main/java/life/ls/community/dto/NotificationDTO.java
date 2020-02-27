package life.ls.community.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object(DTO)
 * 数据传输对象
 */
@Data
public class NotificationDTO implements Serializable {
    private Long id;
    private Long notifier;
    private String notifierName;
    private Long outerId;
    private String outerTitle;
    private Integer type;
    private Date gmtCreate;
    private Integer status;
    private String typeName;
}
