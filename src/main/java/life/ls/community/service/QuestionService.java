package life.ls.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.ls.community.dto.QuestionDTO;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import life.ls.community.mapper.QuestionMapper;
import life.ls.community.mapper.UserMapper;
import life.ls.community.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 业务层：问题的实现类
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询出所有的问题，并且查询关联用户
     *
     * @return
     * @param pageNum
     */
    public PageInfo list(String search,Integer pageNum, Integer pageSize) {
        //进行分页操作
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        //数据的集合
        List<QuestionDTO> list = questionMapper.list(search);
        PageInfo<QuestionDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //根据id查询数据，并且对查询的数据进行分页
    public PageInfo listById(Long id, Integer pageNum, Integer pageSize) {
        //进行分页操作
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        //数据的集合
        List<QuestionDTO> list = questionMapper.listByUserId(id);
        //分页的信息
        PageInfo<QuestionDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //根据id获取问题信息,以及相关的用户和评论
    public QuestionDTO findById(Long id) {

        //获取问题以及相关的用户和评论
        QuestionDTO questionDTO=questionMapper.findById(id);
        if(questionDTO==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return questionDTO;
    }

    //更新或者创建问题
    public void createOrUpdate(Question question) {
        //如果是问题已经存在那么就更新数据，否则就插入数据
        if(question.getId()!=null){
            //问题存在
            questionMapper.update(question);
        }else {
            //问题不存在
            questionMapper.create(question);
        }
    }

    /**
     * 对阅读数更新
     * @param id
     */
    public void incView(Long id) {
        Integer result = questionMapper.updateViewCount(id);
        if(result==0){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }

    //只查询问题
    public Question findQuestionById(Long id) {
        //获取问题
        Question question=questionMapper.findQuestionById(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return question;
    }
    //查询和当前问题相关的问题
    public List<Question> findRelated(String tag, Long id) {
        String replacedTag = tag.replace(",", "|");
        List<Question>  questions=questionMapper.findRelated(replacedTag,id);
        return questions;
    }
}
