package life.ls.community.controller;

import com.github.pagehelper.PageInfo;
import life.ls.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(defaultValue = "1", value = "pageNum", required = false) Integer pageNum,
                        @RequestParam(value = "search", required = false) String search) {
        //返回结果集到页面
        PageInfo pageInfo = questionService.list(search, pageNum, 3);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);
        return "index";
    }
}
