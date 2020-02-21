package life.ls.community.controller;

import com.github.pagehelper.PageInfo;
import life.ls.community.cache.HotTagCache;
import life.ls.community.dto.HotTagDTO;
import life.ls.community.dto.TagRelatedDTO;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import life.ls.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(Model model,HttpServletRequest request,
                        @RequestParam(defaultValue = "1", value = "pageNum", required = false) Integer pageNum,
                        @RequestParam(value = "search", required = false) String search) {
        //返回结果集到页面
        PageInfo pageInfo = questionService.list(search, pageNum, 5);
        //热门标签处理
        List<String> hots = hotTagCache.getHots();
        //查询问题
        //热门标签
        Map<String, TagRelatedDTO> map = questionService.listHotTags(hots);
        //存放数据到session
        request.getSession().setAttribute("hotTags",map);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);
        model.addAttribute("hotTags", map);
        model.addAttribute("tag",null);
        return "index";
    }

    @GetMapping("/tag/")
    public String tagRelated(Model model, @RequestParam(value = "tag",required = false)  String tag,
                             @RequestParam(defaultValue = "1",value = "pageNum", required = false) Integer pageNum,
                             HttpServletRequest request) {
        //获取session中的数据
        Map<String,TagRelatedDTO> hotTags = (Map<String, TagRelatedDTO>) request.getSession().getAttribute("hotTags");
        TagRelatedDTO tagRelatedDTO = hotTags.get(tag);
        //如果标签不存在抛出异常
        if(tagRelatedDTO==null){
            throw new CustomizeException(CustomizeErrorCode.TAG_NOT_FOUND);
        }
        //返回结果集到页面
        PageInfo pageInfo = questionService.listByTag(tagRelatedDTO.getIds(), pageNum, 5);
        //热门标签处理
        List<String> hots = hotTagCache.getHots();
        //查询问题
        //热门标签
        //先获取id对应的id
        Map<String, TagRelatedDTO> map = questionService.listHotTags(hots);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("tag", tag);
        model.addAttribute("hotTags", map);
        return "index";
    }
}
