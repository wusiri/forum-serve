package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.domain.Comment;
import wlong.work.forumserve.dao.CommentDao;
import wlong.work.forumserve.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {


    @Resource
    private CommentDao commentDao;

    @Override
    public List<Comment> getByArticleID(Integer articeId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentArticleId, articeId).eq(Comment::getAuditStatus,1);
        List<Comment> comments = commentDao.selectList(queryWrapper);
        return comments;
    }

    @Override
    public boolean updateGoodNum(int commentGoodNum, Integer commentId) {
        return this.update(Wrappers.lambdaUpdate(Comment.class).set(Comment::getCommentGood, commentGoodNum).eq(Comment::getCommentId, commentId));
    }

    @Override
    public Page<Comment> getPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Comment> pageInfo = new Page<>(page,pageSize);
        //过滤条件
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Comment::getCommentContent, name);
        //添加排序条件
        queryWrapper.orderByAsc(Comment::getCommentId);
        //执行查询
        commentDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }
}
