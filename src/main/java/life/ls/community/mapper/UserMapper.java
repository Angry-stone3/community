package life.ls.community.mapper;

import life.ls.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 持久层：用户的mapper接口
 */
@Mapper
public interface UserMapper {
    /**
     * 保存用户
     */
    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,avatar_url) " +
            "values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void save(User user);

    /**
     * 通过token查询用户是否存在
     * @param token
     */
    @Select("select * from user where token=#{token}")
    User findUserByToken(String token);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    User findById(Long id);

    /**
     * 通过accountId查询用户
     * @param accountId
     * @return
     */
    @Select("select * from user where account_id=#{accountId}")
    User findByAccountId(String accountId);

    /**
     * 插入数据
     * @param user
     */
    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,avatar_url)" +
            "values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl}) ")
    void insert(User user);

    /**
     * 更新用户
     * @param dbUser
     */
    @Update("update user set name =#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl}" +
            " where id=#{id}")
    void update(User dbUser);
}
