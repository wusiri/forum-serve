package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Like;
import wlong.work.forumserve.domain.Reply;
import wlong.work.forumserve.service.LikeService;
import wlong.work.forumserve.service.ReplyService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/like")
@Api(value = "点赞Controller",tags = {"点赞状态访问接口"})
public class LikeController {

    @Resource
    private LikeService likeService;

    @GetMapping("/getArticle")
    @ApiOperation("获取文章点赞状态")
    public R<Integer> getArticle(@RequestParam("articleId") Integer articleId,@RequestParam("userId") Integer userId){
        Like like = likeService.getArticleLikeState(articleId, userId);
        if(like==null){
            return R.success(0);
        }
        return R.success(like.getArticleLikeNum());
    }

    @GetMapping("/getComment")
    @ApiOperation("获取评论点赞状态")
    public R<Integer> getComment(@RequestParam("commentId") Integer commentId,@RequestParam("userId") Integer userId){
        Like like = likeService.getCommentLikeState(commentId, userId);
        if(like==null){
            return R.success(0);
        }
        return R.success(like.getCommentLikeNum());
    }

    @GetMapping("/getReply")
    @ApiOperation("获取回复点赞状态")
    public R<Integer> getReply(@RequestParam("replyId") Integer replyId,@RequestParam("userId") Integer userId){
        Like like = likeService.getReplyLikeState(replyId, userId);
        if(like==null){
            return R.success(0);
        }
        return R.success(like.getReplyLikeNum());
    }

    @GetMapping("/getCommunity")
    @ApiOperation("获取社区点赞状态")
    public R<Integer> getCommunity(@RequestParam("communityId") Integer communityId,@RequestParam("userId") Integer userId){
        Like like = likeService.getCommunityLikeState(communityId, userId);
        if(like==null){
            return R.success(0);
        }
        return R.success(like.getCommunityLikeNum());
    }



}

