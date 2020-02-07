package life.ls.community.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
public class TestController {

    @RequestMapping("/get")
    public String getPage() {
        return "navgation2";
    }
}
