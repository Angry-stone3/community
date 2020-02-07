package life.ls.community.controller;

import com.github.pagehelper.PageInfo;
import life.ls.community.dto.NotificationDTO;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import life.ls.community.mapper.UserMapper;
import life.ls.community.model.User;
import life.ls.community.service.NotificationService;
import life.ls.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 面板的控制层
 */
@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action, HttpServletRequest request,
                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                          Model model) {
        //判断是否登录
        User user = (User) request.getSession().getAttribute("user");
        //为空返回到首页
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        //不为空继续执行
        //判断查询的是哪种类型
        if ("questions".equals(action)) {

            //设置返回的数据，并且返回的数据为分页
            PageInfo pageInfo = questionService.listById(user.getId(), pageNum, pageSize);
            //将数据存入request域中
            model.addAttribute("pageInfo", pageInfo);

            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("replies".equals(action)) {
            PageInfo pageInfo = notificationService.listByReceiver(user.getId(), pageNum, pageSize);
            //将数据存入request域中
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        Integer unReadCount = notificationService.unRead(user.getId());
        model.addAttribute("unReadCount", unReadCount);
        return "profile";
    }
}
