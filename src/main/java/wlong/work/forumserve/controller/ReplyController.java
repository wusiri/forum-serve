package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.*;
import wlong.work.forumserve.service.CommentService;
import wlong.work.forumserve.service.LikeService;
import wlong.work.forumserve.service.MessageService;
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
@RequestMapping("/reply")
@Api(value = "回复Controller",tags = {"回复访问接口"})
@Slf4j
public class ReplyController {

    @Resource
    private ReplyService replyService;


    @Resource
    private LikeService likeService;

    @Resource
    private MessageService messageService;

    @Resource
    private CommentService commentService;

    @ApiOperation("审核")
    @PostMapping("/change")
    public R<String> change(@RequestBody Reply reply){
        boolean update = replyService.update(Wrappers.lambdaUpdate(Reply.class).set(Reply::getAuditStatus,reply.getAuditStatus()).eq(Reply::getReplyId,reply.getReplyId()));
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @PutMapping("/saveReply")
    @ApiOperation("保存回复")
    public R<String> saveReply(@RequestBody Reply reply){
        boolean save = replyService.save(reply);
        if(save){
            Message message=new Message();
            message.setMessageFromUser(reply.getReplyUserId());
            message.setMessageUser(reply.getReplyToUserId());
            Integer commentId = reply.getCommentId();
            Comment comment = commentService.getById(commentId);
            String str="回复了你:<strong>"+reply.getReplyContent()+"</strong>,"+comment.getCommentContent();
            message.setMessageInfo(str);
            message.setMessageType(3);
            messageService.save(message);
            return R.success("保存成功");
        }else {
            return R.error("保存失败");
        }
    }


    @ApiOperation("更新点赞数")
    @PutMapping("/addGood")
    public R<String> addGood(@RequestParam("type") Boolean isDisplay, @RequestParam("replyId") Integer replyId, @RequestParam("userId") Integer userId) {
        //获取点赞数量
        Integer replyGood = replyService.getById(replyId).getReplyGood();
        log.info("点赞数:{}",replyGood);
        //isDisplay true表示点赞,false表示取消
        if (isDisplay) {
            //获取状态码
            Like like = likeService.getReplyLikeState(replyId, userId);
            if (like == null) {
                Like replyLike = new Like();
                replyLike.setReplyId(replyId);
                replyLike.setReplyLikeNum(1);
                replyLike.setUserId(userId);
                likeService.save(replyLike);
            } else {
                likeService.setReplyLikeState(replyId, userId, 1);
            }
            replyService.updateGoodNum(replyGood + 1, replyId);

        } else {
            likeService.setReplyLikeState(replyId, userId, 0);
            replyService.updateGoodNum(replyGood - 1, replyId);
        }
        return R.success("操作成功");
    }

    @ApiOperation("获取全部回复")
    @GetMapping("getReply/{commentId}")
    public R<List<Reply>> getReply(@PathVariable("commentId") Integer commentId){
        List<Reply> comments = replyService.getByCommentID(commentId);
        return R.success(comments);
    }

    @ApiOperation("获取回复信息")
    @GetMapping("/getPage")
    public R<Page<Reply>> getPage(Integer page, Integer pageSize, String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<Reply> page1 = replyService.getPage(page, pageSize, name);
        return R.success(page1);
    }

    @ApiOperation("编辑回复")
    @PostMapping("/update")
    public R<String> update(@RequestBody Reply reply){
        boolean update = replyService.updateById(reply);
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("根据id删除消息")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = replyService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }



    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = replyService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }

}

