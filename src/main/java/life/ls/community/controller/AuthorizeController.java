package life.ls.community.controller;


import life.ls.community.dto.AccessTokenDTO;
import life.ls.community.dto.GitHubUser;
import life.ls.community.model.User;
import life.ls.community.provider.GithubProvider;
import life.ls.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 连接github的Controller
 */
@Controller
public class AuthorizeController {
    @Autowired
    private UserService userService;

    @Autowired
    private GithubProvider githubProvider;

    //从配置文件中注入
    @Value("${github.client_id}")
    private String client_id;
    @Value("${github.client_secret}")
    private String client_secret;
    @Value("${github.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        //1.获取access_token
        //1.1将参数装配到实体类
        AccessTokenDTO atd = new AccessTokenDTO();
        atd.setClient_id(client_id);
        atd.setClient_secret(client_secret);
        atd.setCode(code);
        atd.setRedirect_uri(redirect_uri);
        atd.setState(state);
        //1.2向github发起请求
        String accessToken = githubProvider.getAccessToken(atd);

        //2.获取user
        GitHubUser gitHubUser = githubProvider.getUser(accessToken);

        //3.登录信息存储到数据库
        if (gitHubUser != null && gitHubUser.getId() != null) {
            //封装数据
            User user = new User();
            //登录的状态码
            String token = UUID.randomUUID().toString();
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setName(gitHubUser.getName());
            user.setToken(token);
            user.setAvatarUrl(gitHubUser.getAvatarUrl());
            //更新或创建用户
            userService.createOrUpdate(user);
            //保存token到cookie
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            return "redirect:/error";
        }
    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //清空cookie以及去除session中user
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("unReadCount");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        return "redirect:/";
    }
}
