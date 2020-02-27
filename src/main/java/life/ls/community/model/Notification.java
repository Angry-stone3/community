package life.ls.community.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知的实体类
 */
@Data
public class Notification implements Serializable {
    private Long id;
    private Long notifier;
    private Long receiver;
    private Long outerId;
    private Integer type;
    private Date gmtCreate;
    private Integer status;
    private String notifierName;
    private String outerTitle;
    private Long questionId;
}
