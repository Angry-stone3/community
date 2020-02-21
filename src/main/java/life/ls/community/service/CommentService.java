package life.ls.community.service;

import life.ls.community.dto.CommentDTO;
import life.ls.community.enums.CommentTypeEnum;
import life.ls.community.enums.NotificationStatusEnum;
import life.ls.community.enums.NotificationTypeEnum;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import life.ls.community.mapper.CommentMapper;
import life.ls.community.mapper.LikesMapper;
import life.ls.community.mapper.NotificationMapper;
import life.ls.community.mapper.QuestionMapper;
import life.ls.community.model.Comment;
import life.ls.community.model.Likes;
import life.ls.community.model.Notification;
import life.ls.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 业务层：评论的
 */
@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private LikesMapper likesMapper;

    /**
     * 创建用户
     *
     * @param comment
     */

    //创建回复
    @Transactional
    public void create(Comment comment, String userName) {
        //判断评论的对象是否存在
        if (comment.getParentId() == null || comment.getParentId() <= 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //判断评论的类型是否存在
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        //如果类型存在，进行相应的处理
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //是评论
            //判断评论的问题是否存在
            Comment dbComment = commentMapper.findCommentByParentId(comment.getParentId());
            if (comment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //插入评论
            commentMapper.insert(comment);
            //增加评论数
            commentMapper.inrCommentCount(comment.getParentId());
            //对通知进行添加
            Question dbQuestion = questionMapper.findQuestionById(dbComment.getParentId());
            createNotify(comment, dbComment.getCommentator(),
                    NotificationTypeEnum.REPLY_COMMENT,
                    userName, dbQuestion);
        } else {

            //是问题
            Question dbQuestion = questionMapper.findQuestionById(comment.getParentId());
            //判断问题是否存在
            if (dbQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //插入数据
            commentMapper.insert(comment);
            //增加评论人数
            questionMapper.incCommentCount(dbQuestion);
            //对通知进行添加
            createNotify(comment, dbQuestion.getCreator(), NotificationTypeEnum.REPLY_QUESTION,
                    userName, dbQuestion);
        }

    }

    //对通知进行封装
    private void createNotify(Comment comment, Long receiverId,
                              NotificationTypeEnum notificationTypeEnum,
                              String notifierName, Question question) {
        //判断发送者是否是本人，如果是本人则不更新任何数据
        if (receiverId.equals(comment.getCommentator())) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(new Date());
        notification.setNotifier(comment.getCommentator());
        notification.setOuterId(comment.getParentId());
        notification.setReceiver(receiverId);
        notification.setType(notificationTypeEnum.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(question.getTitle());
        notification.setQuestionId(question.getId());
        //插入数据
        notificationMapper.insert(notification);
    }


    //查询所有的二级评论
    public List<CommentDTO> listById(Long id, CommentTypeEnum commentType) {
        List<CommentDTO> comments = commentMapper.listById(id, commentType.getType());
        return comments;
    }

    //点赞
    public Integer like(Likes likes) {
        //检查该评论是否被当前用户评论过
        Likes likesdb = likesMapper.findByCommentAndUser(likes);
        //如果没有点过赞,那么可以点赞
        if (likesdb == null) {
            //更新点赞数目
            commentMapper.addLikeCount(likes.getCommentId());
            likesMapper.insert(likes);
            Integer likeCount = commentMapper.findCommentByParentId(likes.getCommentId()).getLikeCount();
            return likeCount;
        } else {
            //否则不能点赞
            return -1;
        }
    }
    
    //删除评论和二级评论
    public void deleteById(Long parentId) {
        //先判断是否存在
        List<Comment> commentsDb = commentMapper.findCommentByQueId(parentId);
        if(commentsDb!=null) {
            //先删除二级评论和点赞
            for (Comment comment : commentsDb) {
                commentMapper.deleteSecondCommentByParentId(comment.getId());;
            }
            //删除点赞关系和一级评论
            //通过父类的id和类型查询ids
            for(Long commentId :commentMapper.findIdsByParentIdAndType1(parentId)){
                //通过评论的id删除点赞关系
                likesMapper.deleteByCommentId(commentId);
            }
            commentMapper.deleteByQueId(parentId);
        }
    }
}
