package life.ls.community.controller;

import com.github.pagehelper.PageInfo;
import life.ls.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {

        //返回结果集到页面
        PageInfo pageInfo = questionService.list(pageNum, 3);
        request.setAttribute("pageInfo", pageInfo);
        return "index";
    }
}
