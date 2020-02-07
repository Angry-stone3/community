package life.ls.community.mapper;

import life.ls.community.dto.NotificationDTO;
import life.ls.community.model.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 持久层：通知
 */
@Mapper
public interface NotificationMapper {

    //查询用户所有相关的通知
    @Select("select * from notification where receiver=#{receiver} order by id desc")
    List<NotificationDTO> listByReceiver(Long receiver);

    //查询和用户相关的所有状态
    @Select("select status from notification where receiver=#{receiver}")
    List<Integer> findStatusByReceiver(Long receiver);

    //插入通知
    @Insert("insert into notification(notifier,receiver,outer_id,type,gmt_create,status,notifier_name,outer_title,question_id) " +
            "values(#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle},#{questionId})")
    void insert(Notification notification);

    //根据id查询通知
    @Select("select * from notification where id=#{id}")
    Notification findNotificationById(Long nId);

    //更新状态
    @Update("update notification set status=#{status} where id=#{id}" )
    void updateStatus(Notification notification);
}
