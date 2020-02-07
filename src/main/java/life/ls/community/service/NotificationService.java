package life.ls.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.ls.community.dto.NotificationDTO;
import life.ls.community.dto.QuestionDTO;
import life.ls.community.enums.NotificationStatusEnum;
import life.ls.community.enums.NotificationTypeEnum;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import life.ls.community.mapper.NotificationMapper;
import life.ls.community.mapper.QuestionMapper;
import life.ls.community.model.Notification;
import life.ls.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层：通知
 */
@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 通过outerId查询所有评论及其相关
     *
     * @param receiver
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo listByReceiver(Long receiver, Integer pageNum, Integer pageSize) {
        //进行分页操作
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        //数据的集合
        List<NotificationDTO> list = notificationMapper.listByReceiver(receiver);

        for (NotificationDTO notificationDTO : list) {
            //将类型转换成类型名称
            String typeName = NotificationTypeEnum.getNameByType(notificationDTO.getType());
            notificationDTO.setTypeName(typeName);
        }
        //分页的信息
        PageInfo<NotificationDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //查询未读的状态
    public Integer unRead(Long receiver) {
        //计数器
        Integer count = 0;
        List<Integer> statusList = notificationMapper.findStatusByReceiver(receiver);
        //遍历并且记录数量
        for (Integer status : statusList) {
            if (status == 0) {
                count++;
            }
        }
        return count;
    }

    //查询问题
    public QuestionDTO read(Long nId, User user) {
        Notification notification = notificationMapper.findNotificationById(nId);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.MESSAGE_NOT_FOUND);
        }
        if (notification.getReceiver() != user.getId()) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        //更新状态为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateStatus(notification);
        //
        QuestionDTO questionDTO = questionMapper.findById(notification.getQuestionId());
        if (questionDTO == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return questionDTO;
    }

}
