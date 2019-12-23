package life.ls.community.mapper;

import life.ls.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 持久层：用户的mapper接口
 */
@Mapper
public interface UserMapper {
    /**
     *保存用户
     */
    @Insert("insert into user(account_id,name,token,gtm_create,gtm_modified) " +
            "values(#{account_id},#{name},#{token},#{gtm_create},#{gtm_modified})")
    void save(User user);

}
