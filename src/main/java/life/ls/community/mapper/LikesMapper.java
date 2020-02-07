package life.ls.community.mapper;
import	java.util.Map;

import life.ls.community.model.Likes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//点赞功能
@Mapper
public interface LikesMapper {

    //根据user和comment查询
    @Select("select * from likes where comment_id=#{commentId} and user_id=#{userId}")
    Likes findByCommentAndUser(Likes likes);

    //插入数据
    @Insert("insert into likes(comment_id,user_id) values(#{commentId},#{userId})")
    void insert(Likes likes);
}
