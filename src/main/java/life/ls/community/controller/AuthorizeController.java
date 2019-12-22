package life.ls.community.controller;

import life.ls.community.dto.AccessTokenDTO;
import life.ls.community.dto.GitHubUser;
import life.ls.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 连接github的Controller
 */
@Controller
public class AuthorizeController {
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
                           @RequestParam("state") String state) {
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
        GitHubUser user = githubProvider.getUser(accessToken);
        System.out.println(user);

        return "index";
    }
}
