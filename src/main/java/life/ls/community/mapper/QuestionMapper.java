package life.ls.community.mapper;
import	java.lang.ref.Reference;
import	java.sql.ResultSet;

import life.ls.community.dto.QuestionDTO;
import life.ls.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * 持久层：问题
 */
@Mapper
public interface QuestionMapper {
    //保存问题
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag)" +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    //查询所有问题
//    @Select("select * from question order by gmt_modified desc")
    @Select("<script>select * from question " +
            " <if test=\"search !=null \">where title like BINARY CONCAT('%',#{search},'%') </if> order by gmt_modified desc </script>")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "creator", column = "creator"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "tag", column = "tag"),
            @Result(property = "user", column = "creator",
                    one = @One(select = "life.ls.community.mapper.UserMapper.findUserById", fetchType = FetchType.EAGER))
    })
    List<QuestionDTO> list(String search);


    //查询所有问题
    @Select("select * from question where creator=#{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "creator", column = "creator"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "tag", column = "tag"),
            @Result(property = "user", column = "creator",
                    one = @One(select = "life.ls.community.mapper.UserMapper.findUserById", fetchType = FetchType.EAGER))
    })
    List<QuestionDTO> listByUserId(Long id);

    //通过id查询问题和相关联的用户
    @Select("select * from question where id=#{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "creator", column = "creator"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "tag", column = "tag"),
            @Result(property = "user", column = "creator",
                    one = @One(select = "life.ls.community.mapper.UserMapper.findUserById", fetchType = FetchType.EAGER)),
            @Result(property = "comments", column = "id",
                    many = @Many(select = "life.ls.community.mapper.CommentMapper.listByQId", fetchType = FetchType.EAGER))
    })
    QuestionDTO findById(Long id);

    //更新问题
    @Update("update question set title=#{title},description=#{description},tag=#{tag}" +
            "where id=#{id}")
    void update(Question question);

    //更新阅读数
    @Update("update question set view_count=view_count+1 where id=#{id}")
    Integer updateViewCount(Long id);

    //只查询问题
    @Select("select * from question where id=#{id}")
    Question findQuestionById(Long id);

    //增加问题的评论人数
    @Update("update question set comment_count=comment_count+1 where id =#{id}")
    void incCommentCount(Question dbQuestion);

    //查询和问题相关的
    @Select("select * from question where id!=#{id} and tag REGEXP #{tag}")
    List<Question> findRelated(@Param("tag") String replacedTag, @Param("id") Long id);

    //查询所有(不带查询的)
    @Select("select * from question")
    List<Question> findAllQuestion();

    //通过标签查询所有问题
    @Select("select * from question where tag like #{tag}")
    List<Question> findQuestionByTag(String tag);


    //根据id查询相关问题

    @Select("<script> " +
            "select * from question where id "+
            "in <foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'> #{id} </foreach> " +
            "</script>")
    @Results(id = "resultUser",value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "creator", column = "creator"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "tag", column = "tag"),
            @Result(property = "user", column = "creator",
                    one = @One(select = "life.ls.community.mapper.UserMapper.findUserById", fetchType = FetchType.EAGER))
    })
    List<QuestionDTO> listByTag(@Param("ids")List<Long> ids);


    //查询所有方法
    @Select("select * from question")
    @ResultMap(value = {"resultUser"})
    List<QuestionDTO> findAll();

    //根据id删除问题
    @Delete("delete from question where id=#{id}")
    void deleteById(Long id);
}
