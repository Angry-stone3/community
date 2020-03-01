package life.ls.community.mapper;

import com.github.pagehelper.PageInfo;
import life.ls.community.dto.UserDTO;
import life.ls.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")//加入该注解可以保持对象后，查看对象插入id
    void save(User user);

    /**
     * 通过token查询用户是否存在
     *
     * @param token
     */
    @Select("select * from user where token=#{token}")
    User findUserByToken(String token);

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    @ResultMap("userMap")
    UserDTO findById(Long id);


    /**
     * 通过accountId查询用户
     *
     * @param accountId
     * @return
     */
    @Select("select * from user where account_id=#{accountId}")
    User findByAccountId(String accountId);

    /**
     * 插入数据
     *
     * @param user
     */
    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified,avatar_url)" +
            "values(#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl}) ")
    void insert(User user);

    /**
     * 更新用户
     *
     * @param dbUser
     */
    @Update("update user set name =#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl}" +
            " where id=#{id}")
    void update(User dbUser);


    /**
     * 只查询用户信息
     *
     * @param id
     */
    @Select("select * from user where id=#{id}")
    User findUserById(Long id);


    /**
     * 查询所有用户及其地位
     *
     * @return
     */
    @Select("select * from user")
    @Results(id = "userMap",value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "token", column ="token"),
            @Result(property ="gmtCreate", column ="gmt_create"),
            @Result(property ="gmtModified", column ="gmt_modified"),
            @Result(property ="avatarUrl", column ="avatar_url"),
            @Result(property = "status",column ="id",one = @One(select = "life.ls.community.mapper.StatusMapper.findByUId"))
    })
    List<UserDTO> findAllUser();

    //通过项目查询用户
    @Select("select * from user where name=#{username}")
    User findByName(String username);

    //更新token
    @Update("update user set token=#{token} where account_id=#{accountId}")
    void updateTokenByAccountId(User user);
}