package life.ls.community.controller;
import	java.util.Date;

import life.ls.community.dto.AccessTokenDTO;
import life.ls.community.dto.GitHubUser;
import life.ls.community.mapper.UserMapper;
import life.ls.community.model.User;
import life.ls.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 连接github的Controller
 */
@Controller
public class AuthorizeController {
    @Autowired
    private UserMapper userMapper;

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
                           HttpServletRequest request) {
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

        //3.登录信息存储到session
        if (gitHubUser!=null) {

            //测试：保存数据到数据库
            User user=new User();
            user.setAccount_id(gitHubUser.getId());
            user.setName(gitHubUser.getName());
            user.setToken(UUID.randomUUID().toString());
            user.setGtm_create(new Date());
            user.setGtm_modified(new Date());
            userMapper.save(user);
            //登录成功保存信息到session
            request.getSession().setAttribute("user", gitHubUser);
            return "redirect:/";
        }else {
            return "redirect:/error";
        }

    }
}
