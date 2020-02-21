package life.ls.community.controller;

import life.ls.community.dto.QuestionDTO;
import life.ls.community.dto.UserDTO;
import life.ls.community.mapper.NotificationMapper;
import life.ls.community.model.User;
import life.ls.community.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 表示层：通知
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String findQuestionByNId(@PathVariable("id") Long nId, Model model,
                                    HttpServletRequest request) {
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");
        User user=new User();
        BeanUtils.copyProperties(userDTO,user);
        QuestionDTO questionDTO = notificationService.read(nId,user);
        model.addAttribute("showQue", questionDTO);
        return "question";
    }
}
