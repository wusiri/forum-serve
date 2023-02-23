package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wlong.work.forumserve.dao.LikeDao;
import wlong.work.forumserve.domain.Like;
import wlong.work.forumserve.service.LikeService;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeDao, Like> implements LikeService {


    @Resource
    private LikeDao likeDao;

    @Override
    public Like getArticleLikeState(Integer articleId,Integer userId) {
        LambdaQueryWrapper<Like> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getArticleId,articleId).eq(Like::getUserId,userId);
        return likeDao.selectOne(queryWrapper);
    }

    @Override
    public Like getCommentLikeState(Integer commentId,Integer userId) {
        LambdaQueryWrapper<Like> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getCommentId,commentId).eq(Like::getUserId,userId);
        return likeDao.selectOne(queryWrapper);
    }

    @Override
    public Like getReplyLikeState(Integer replyId,Integer userId) {
        LambdaQueryWrapper<Like> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getReplyId,replyId).eq(Like::getUserId,userId);
        return likeDao.selectOne(queryWrapper);
    }

    @Override
    public Like getCommunityLikeState(Integer communityId, Integer userId) {
        LambdaQueryWrapper<Like> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getCommunityId,communityId).eq(Like::getUserId,userId);
        return likeDao.selectOne(queryWrapper);
    }

    @Override
    public boolean setArticleLikeState(Integer articleId, Integer userId, Integer state) {
        return this.update(Wrappers.lambdaUpdate(Like.class).set(Like::getArticleLikeNum,state).eq(Like::getArticleId,articleId).eq(Like::getUserId,userId));
    }

    @Override
    public boolean setCommentLikeState(Integer commentId, Integer userId, Integer state) {
        return this.update(Wrappers.lambdaUpdate(Like.class).set(Like::getCommentLikeNum,state).eq(Like::getCommentId,commentId).eq(Like::getUserId,userId));
    }

    @Override
    public boolean setReplyLikeState(Integer replyId, Integer userId, Integer state) {
        return this.update(Wrappers.lambdaUpdate(Like.class).set(Like::getReplyLikeNum,state).eq(Like::getReplyId,replyId).eq(Like::getUserId,userId));
    }

    @Override
    public boolean setCommunityLikeState(Integer communityId, Integer userId, Integer state) {
        return this.update(Wrappers.lambdaUpdate(Like.class).set(Like::getCommunityLikeNum,state).eq(Like::getCommunityId,communityId).eq(Like::getUserId,userId));
    }
}
