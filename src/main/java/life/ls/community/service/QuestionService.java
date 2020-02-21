package life.ls.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.ls.community.dto.HotTagDTO;
import life.ls.community.dto.QuestionDTO;
import life.ls.community.dto.TagRelatedDTO;
import life.ls.community.exception.CustomizeErrorCode;
import life.ls.community.exception.CustomizeException;
import life.ls.community.mapper.QuestionMapper;
import life.ls.community.mapper.UserMapper;
import life.ls.community.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 业务层：问题的实现类
 */
@Service
@Transactional
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentService commentService;


    /**
     * 查询出所有的问题，并且查询关联用户
     *
     * @param pageNum
     * @return
     */
    public PageInfo list(String search, Integer pageNum, Integer pageSize) {
        //进行分页操作
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        //数据的集合
        List<QuestionDTO> list = questionMapper.list(search);
        PageInfo<QuestionDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询出和标签相关的问题
     *
     * @param pageNum
     * @return
     */
    public PageInfo listByTag(List<Long> ids, Integer pageNum, Integer pageSize) {
        //进行分页操作
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        //数据的集合
        List<QuestionDTO> list = questionMapper.listByTag(ids);
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
        QuestionDTO questionDTO = questionMapper.findById(id);
        if (questionDTO == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return questionDTO;
    }

    //更新或者创建问题
    public void createOrUpdate(Question question) {
        //如果是问题已经存在那么就更新数据，否则就插入数据
        if (question.getId() != null) {
            //问题存在
            questionMapper.update(question);
        } else {
            //问题不存在
            questionMapper.create(question);
        }
    }

    /**
     * 对阅读数更新
     *
     * @param id
     */
    public void incView(Long id) {
        Integer result = questionMapper.updateViewCount(id);
        if (result == 0) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }

    //只查询问题
    public Question findQuestionById(Long id) {
        //获取问题
        Question question = questionMapper.findQuestionById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return question;
    }

    //查询和当前问题相关的问题
    public List<Question> findRelated(String tag, Long id) {
        String replacedTag = tag.replace(",", "|");
        List<Question> questions = questionMapper.findRelated(replacedTag, id);
        return questions;
    }

    //查询
    public Map<String, TagRelatedDTO> listHotTags(List<String> hots) {
        int questionCount = 0;
        int commentCount = 0;
        List<Question> questions = null;
        Map<String, TagRelatedDTO> tagRelateds = new HashMap<>();
        Question question = null;
        List<String> list = null;
        //存放标签对应的问题id
        List<Long> ids=null;

        //遍历
        for (String tag : hots) {
            TagRelatedDTO tagRelatedDTO = new TagRelatedDTO();
            ids=new ArrayList<>();
            questionCount = 0;
            commentCount = 0;

            questions = questionMapper.findQuestionByTag("%" + tag + "%");

            for (Iterator<Question> it = questions.iterator(); it.hasNext(); ) {
                //遍历标签
                question = it.next();
                String[] tags = question.getTag().split(",");
                list = Arrays.asList(tags);
                //如果不包含当前标签则移出集合
                if (list.contains(tag) == false) {
                    it.remove();
                } else {
                    //存放id
                    ids.add(question.getId());
                    commentCount += question.getCommentCount();
                }
            }

            //累加问题数
            questionCount += questions.size();
            //累加评论数
            tagRelatedDTO.setQuestionCount(questionCount);
            tagRelatedDTO.setCommentCount(commentCount);
            tagRelatedDTO.setIds(ids);
            //将标签和标签相关的问题封装在map中
            tagRelateds.put(tag, tagRelatedDTO);
        }
        return tagRelateds;
    }

    //查找所有商品
    public PageInfo findAll(Integer pageNum,Integer pageSize) {
        //进行分页操作
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionDTO> list = questionMapper.findAll();
        //分页的信息
        PageInfo<QuestionDTO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //根据id删除问题
    public void deleteById(Long id) {
        //先查找问题是否存在
        QuestionDTO questionDb = questionMapper.findById(id);
        if(questionDb==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //先删除一级评论和二级评论
        commentService.deleteById(id);
        //如果存在就可以进行删除
        questionMapper.deleteById(id);
    }
}
