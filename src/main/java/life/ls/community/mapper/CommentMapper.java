package life.ls.community.mapper;

import life.ls.community.dto.CommentDTO;
import life.ls.community.model.Comment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface CommentMapper {

    //插入
    @Insert("insert into comment(parent_id,type,commentator,gmt_create,gmt_modified,content) " +
            "values(#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content})")
    void insert(Comment comment);

    //查询的是评论的父类是评论
    @Select("select * from comment where id=#{id}")
    Comment findCommentByParentId(Long parentId);

    //通过问题的id查询相关的评论
    @Select("select * from comment where type=1 and parent_id=#{QId}")
    @Results({
            @Result(id = true,property ="id",column ="id"),
            @Result(property ="parentId",column ="parent_id"),
            @Result(property ="type",column ="type"),
            @Result(property ="commentator",column ="commentator"),
            @Result(property ="gmtCreate",column ="gmt_create"),
            @Result(property ="gmtModified",column ="gmt_modified"),
            @Result(property ="likeCount",column ="like_count"),
            @Result(property ="content",column ="content"),
            @Result(property ="commentCount",column ="comment_count"),
            @Result(property ="user",column ="commentator",
                    one = @One(select = "life.ls.community.mapper.UserMapper.findUserById",fetchType = FetchType.EAGER))
    })
    List<CommentDTO> listByQId(Long QId);


    //查询所有的二级评论
    @Select("select * from comment where parent_id=#{id} and type=#{type}")
    @Results({
            @Result(id = true,property ="id",column ="id"),
            @Result(property ="parentId",column ="parent_id"),
            @Result(property ="type",column ="type"),
            @Result(property ="commentator",column ="commentator"),
            @Result(property ="gmtCreate",column ="gmt_create"),
            @Result(property ="gmtModified",column ="gmt_modified"),
            @Result(property ="likeCount",column ="like_count"),
            @Result(property ="content",column ="content"),
            @Result(property ="commentCount",column ="comment_count"),
            @Result(property ="user",column ="commentator",
                    one = @One(select = "life.ls.community.mapper.UserMapper.findUserById",fetchType = FetchType.EAGER))
    })
    List<CommentDTO> listById(@Param("id") Long id,@Param("type") Integer type);


    //更新评论数量
    @Update("update comment set comment_count=comment_count+1 where id=#{parentId}")
    void inrCommentCount(Long parentId);

    @Update("update comment set like_count=like_count+1 where id=#{commentId}")
    Integer addLikeCount(Long commentId);

    //查询的是评论的父类是问题
    @Select("select * from comment where parent_id=#{QueId} and type=1")
    List<Comment> findCommentByQueId(Long QueId);

    //删除一级评论
    @Delete("delete from comment where parent_id=#{id} and type=1")
    void deleteByQueId(Long id);

    //删除二级评论
    @Delete("delete from comment where parent_id=#{id} and type=2")
    void deleteSecondCommentByParentId(Long id);
    //查询类型为1的评论的id集合
    @Select("select * from comment where parent_id=#{parentId} and type=1")
    List<Long> findIdsByParentIdAndType1(Long parentId);
}
