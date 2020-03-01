package life.ls.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.ls.community.dto.UserDTO;
import life.ls.community.enums.UserStateEnum;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import life.ls.community.mapper.StatusMapper;
import life.ls.community.mapper.UserMapper;
import life.ls.community.model.Status;
import life.ls.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 业务层：用户的实现类
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StatusMapper statusMapper;

    /**
     * 登录操作
     * 如果用户存在就更新
     * 如果不存在就创建
     *
     * @param user
     */
    public void createOrUpdate(User user) {
        //1.从数据库里查询用户
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        //2.判断是否存在
        if (dbUser == null) {
            //不存在,创建新的用户
            user.setGmtCreate(new Date());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //插入用户身份的关联数据
            Status status = new Status();
            status.setUserId(user.getId());//对应的用户
            status.setState(UserStateEnum.NORMAL.getName());//对应的状态
            status.setThird(UserStateEnum.IS_THRID.getName());//是否是第三方
            statusMapper.insert(status);
        } else {
            //存在，那么就更新状态
            dbUser.setName(user.getName());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            dbUser.setGmtModified(new Date());
            userMapper.update(dbUser);
        }
    }

    //查找所有的用户
    public PageInfo findAllUser(Integer pageNum, Integer pageSize) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        //查询所有用户
        List<UserDTO> list = userMapper.findAllUser();
        //封装数据到pageInfo
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    //查找用户和其状态
    public UserDTO findUserByToken(String token) {
        User user = userMapper.findUserByToken(token);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        //查询对应的状态
        Status statusDb = statusMapper.findByUId(user.getId());
        userDTO.setStatus(statusDb);
        return userDTO;
    }

    //更改用户状态
    public void stopOrActiveUserByUserId(Long id) {
        //判断用户是否存在
        UserDTO userDb = userMapper.findById(id);
        if (userDb == null) {
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        //根据状态更改
        String state = userDb.getStatus().getState();
        if (state.equals(UserStateEnum.NORMAL.getName())) {
            state = UserStateEnum.PROHIBIT.getName();
        } else {
            state = UserStateEnum.NORMAL.getName();
        }
        //更改状态
        statusMapper.changStateByUserId(id, state);
    }


    //更新token(非第三方用户登录时的操作)
    public void updateTokenByAccountId(User user) {
        userMapper.updateTokenByAccountId(user);
    }
}
