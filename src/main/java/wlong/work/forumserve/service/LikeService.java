package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wlong.work.forumserve.domain.Like;

public interface LikeService extends IService<Like> {
    Like getArticleLikeState(Integer articleId,Integer userId);


    Like getCommentLikeState(Integer commentId,Integer userId);


    Like getReplyLikeState(Integer replyId,Integer userId);


    Like getCommunityLikeState(Integer communityId, Integer userId);


    boolean setArticleLikeState(Integer articleId, Integer userId, Integer state);


    boolean setCommentLikeState(Integer commentId, Integer userId, Integer state);


    boolean setReplyLikeState(Integer replyId, Integer userId, Integer state);


    boolean setCommunityLikeState(Integer communityId, Integer userId, Integer state);


}

