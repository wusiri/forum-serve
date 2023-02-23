package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Comment;
import wlong.work.forumserve.domain.Reply;
import wlong.work.forumserve.dao.ReplyDao;
import wlong.work.forumserve.service.ReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyDao, Reply> implements ReplyService {

    @Resource
    private ReplyDao replyDao;
    @Override
    public List<Reply> getByCommentID(Integer commentId) {
        LambdaQueryWrapper<Reply> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Reply::getCommentId,commentId).eq(Reply::getAuditStatus,1);
        List<Reply> replies = replyDao.selectList(queryWrapper);
        return replies;
    }

    @Override
    public boolean updateGoodNum(int replyGoodNum, Integer replyId) {
        return this.update(Wrappers.lambdaUpdate(Reply.class).set(Reply::getReplyGood, replyGoodNum).eq(Reply::getReplyId, replyId));

    }

    @Override
    public Page<Reply> getPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Reply> pageInfo = new Page<>(page,pageSize);
        //过滤条件
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Reply::getReplyContent, name);
        //添加排序条件
        queryWrapper.orderByAsc(Reply::getReplyId);
        //执行查询
        replyDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }
}
