package life.ls.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam("username") String name, Model model) {
        //将数据添加搭配request域中
        model.addAttribute("msg",name);
        return "hello";
    }
}
