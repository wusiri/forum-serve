package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Article;
import wlong.work.forumserve.domain.Comment;
import wlong.work.forumserve.domain.Reply;
import wlong.work.forumserve.service.ReplyService;

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
@RequestMapping("/reply")
@Api(value = "回复Controller",tags = {"回复访问接口"})
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PutMapping("/saveReply")
    @ApiOperation("保存回复")
    public R<String> saveReply(@RequestBody Reply reply){
        boolean save = replyService.save(reply);
        if(save){
            return R.success("保存成功");
        }else {
            return R.error("保存失败");
        }
    }

    @ApiOperation("更新点赞数")
    @PutMapping("/addGood")
    public R<String> addGood(@RequestParam("replyGood") Integer goodNum,@RequestParam("replyId") Integer replyId){
        LambdaUpdateWrapper<Reply> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.set(Reply::getReplyGood,goodNum).eq(Reply::getReplyId,replyId);
        boolean update = replyService.update(updateWrapper);
        if (update){
            return R.success("更新成功");
        }else {
            return R.success("更新失败");
        }
    }

    @ApiOperation("获取全部回复")
    @GetMapping("getReply/{commentId}")
    public R<List<Reply>> getReply(@PathVariable("commentId") Integer commentId){
        List<Reply> comments = replyService.getByCommentID(commentId);
        return R.success(comments);
    }

}

