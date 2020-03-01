package life.ls.community.controller;

import life.ls.community.cache.TagCache;
import life.ls.community.dto.ResultDTO;
import life.ls.community.dto.UserDTO;
import life.ls.community.enums.UserStateEnum;
import life.ls.community.model.Question;
import life.ls.community.model.User;
import life.ls.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 表示层：发布
 */
@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    //保存发布的帖子的信息
    @PostMapping("/publish")
    public String publish(@RequestParam("title") String title,
                          @RequestParam("description") String description,
                          @RequestParam("tag") String tag,
                          @RequestParam("id") Long id,
                          HttpServletRequest request,
                          Model model) {
        //判断是否登录
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        //判断是否被禁言
        if (user.getStatus() != null) {
            //判断是否被禁言
            String state = user.getStatus().getState();
            if (UserStateEnum.PROHIBIT.getName().equals(state)) {
                //错误信息
                model.addAttribute("error","你已经被禁言请联系管理员");
                return "publish";
            }
        }

        //如果登录那么就保存问题，否则就返回到请求页面
        if (user != null) {
            //判断写入的标签是否和标签库的内容一致
            String invalid = TagCache.filterInvalid(tag);
            //如果不存在
            if(!StringUtils.isEmpty(invalid)){
                //回显数据
                model.addAttribute("title",title);
                model.addAttribute("tag",tag);
                model.addAttribute("description",description);
                //错误信息
                model.addAttribute("error","输入了非法标签"+invalid);
                return "publish";
            }
            //封装数据
            Question question = new Question();
            question.setTitle(title);
            question.setDescription(description);
            question.setGmtCreate(new Date());
            question.setGmtModified(question.getGmtCreate());
            question.setCreator(user.getId());
            question.setTag(tag);
            question.setId(id);
            questionService.createOrUpdate(question);
        } else {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        return "redirect:/";
    }


    //编辑功能，回显数据
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id,
                       Model model) {
        //通过id查询问题
        Question question = questionService.findQuestionById(id);
        //设置回显
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
}
