package life.ls.community.controller;

import java.util.HashMap;

import life.ls.community.dto.CommentCreateDTO;
import life.ls.community.dto.CommentDTO;
import life.ls.community.dto.ResultDTO;
import life.ls.community.enums.CommentTypeEnum;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.model.Comment;
import life.ls.community.model.Likes;
import life.ls.community.model.User;
import life.ls.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 表示层：评论
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //回复评论
    @PostMapping("/comment")
    @ResponseBody
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        //判断用户是否登录
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //评论的内容不能为空
        if (commentCreateDTO.equals(null) || StringUtils.isEmpty(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        //数据封装
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtCreate(new Date());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        //调用
        commentService.create(comment, user.getName());
        return ResultDTO.okOf();
    }

    //查询回复评论
    @ResponseBody
    @GetMapping(value = "/comment/{id}")
    public ResultDTO<List> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listById(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }

    //实现点赞功能
    @ResponseBody
    @GetMapping("/comment")
    public Integer like(@RequestBody @RequestParam("commentId") Long commentId,
                        HttpServletRequest request) {
        //获取session
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return -2;
        } else {
            //封装数据到Likes
            Likes likes = new Likes();
            likes.setCommentId(commentId);
            likes.setUserId(user.getId());
            Integer likeCount = commentService.like(likes);
            return likeCount;
        }
    }
}
