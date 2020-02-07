package life.ls.community.service;

import life.ls.community.mapper.UserMapper;
import life.ls.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 业务层：用户的实现类
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 登录操作
     * 如果用户存在就更新
     * 如果不存在就创建
     * @param user
     */
    public void createOrUpdate(User user) {
        //1.从数据库里查询用户
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        //2.判断是否存在
        if(dbUser == null){
            //不存在,创建新的用户
            user.setGmtCreate(new Date());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //存在，那么就更新状态
            dbUser.setName(user.getName());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setGmtModified(new Date());
            userMapper.update(dbUser);
        }
    }
}
