package life.ls.community.provider;

import com.alibaba.fastjson.JSON;
import life.ls.community.dto.AccessTokenDTO;
import life.ls.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 模拟post和get请求的工具类提供者
 */
@Component
public class GithubProvider {

    /**
     * post请求
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            String access_token = str.split("&")[0].split("=")[1];
            return access_token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用get请求获取user
     */
    public GitHubUser getUser(String access_token) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            GitHubUser gitHubUser = JSON.parseObject(response.body().string(), GitHubUser.class);
            return gitHubUser;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
