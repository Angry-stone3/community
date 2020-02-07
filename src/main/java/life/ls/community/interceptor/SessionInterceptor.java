package life.ls.community.interceptor;

import life.ls.community.mapper.UserMapper;
import life.ls.community.model.User;
import life.ls.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录的拦截器
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
    //方法执行前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //功能：第一次登录成功后，以后不关闭浏览器，就不需要重新登录
        //1.获取Cookie
        Cookie[] cookies = request.getCookies();
        //2.遍历获取token
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    //2.1获取token
                    String token = cookie.getValue();
//                    System.out.println("当前账户的token"+token);
                    //2.2根据token去数据库红查询
                    User user = userMapper.findUserByToken(token);
                    //2.3判断用户是否存在
                    if (user != null) {
                        //2.4保存数据到session
                        request.getSession().setAttribute("user", user);
                        //查询用户通知数
                        Integer unReadCount = notificationService.unRead(user.getId());
                        request.getSession().setAttribute("unReadCount", unReadCount);
                        break;
                    }
                }
            }
        }
        return true;
    }

    //方法的结束时
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    //方法的结束后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}