package life.ls.community.controller;

import life.ls.community.dto.QuestionDTO;
import life.ls.community.model.Question;
import life.ls.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 表示层：处理问题的展示页面
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model){
        questionService.incView(id);
        //查询问题
        QuestionDTO qd=questionService.findById(id);
        //查询和问题相关的问题
        List<Question> relatedQuestions =questionService.findRelated(qd.getTag(),qd.getId());
        model.addAttribute("showQue",qd);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
