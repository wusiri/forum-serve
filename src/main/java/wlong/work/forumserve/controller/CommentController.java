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
import wlong.work.forumserve.dto.CommentReplyDto;
import wlong.work.forumserve.dto.ReplyDto;
import wlong.work.forumserve.service.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/comment")
@Api(value = "评论Controller", tags = {"评论访问接口"})
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    @Resource
    private ReplyService replyService;

    @Resource
    private MessageService messageService;

    @Resource
    private ArticleService articleService;


    @ApiOperation("获取评论信息")
    @GetMapping("/getPage")
    public R<Page<Comment>> getPage(Integer page, Integer pageSize, String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<Comment> page1 = commentService.getPage(page, pageSize, name);
        return R.success(page1);
    }

    @PutMapping("/saveComment")
    public R<String> saveComment(@RequestBody Comment comment) {
        boolean save = commentService.save(comment);
        if (save) {
            Message message=new Message();
            message.setMessageFromUser(comment.getCommentUserId());
            Integer articleId = comment.getCommentArticleId();
            Article article = articleService.getById(articleId);
            message.setMessageUser(article.getUserId());
            String str="评论了你:<strong>"+comment.getCommentContent()+"</strong>,"+article.getArticleSummary();
            message.setMessageInfo(str);
            message.setMessageType(2);
            messageService.save(message);
            return R.success("评论成功");
        } else {
            return R.success("评论失败");
        }
    }

    @ApiOperation("更新点赞数")
    @PutMapping("/addGood")
    public R<String> addGood(@RequestParam("type") Boolean isDisplay, @RequestParam("commentId") Integer commentId, @RequestParam("userId") Integer userId) {
        //获取点赞数量
        Integer commentGood = commentService.getById(commentId).getCommentGood();
        log.info("点赞数:{}",commentGood);
        //isDisplay true表示点赞,false表示取消
        if (isDisplay) {
            //获取状态码
            Like like = likeService.getCommentLikeState(commentId, userId);
            if (like == null) {
                Like commentLike = new Like();
                commentLike.setCommentId(commentId);
                commentLike.setCommentLikeNum(1);
                commentLike.setUserId(userId);
                likeService.save(commentLike);
            } else {
                likeService.setCommentLikeState(commentId, userId, 1);
            }
            commentService.updateGoodNum(commentGood + 1, commentId);

        } else {
            likeService.setCommentLikeState(commentId, userId, 0);
            commentService.updateGoodNum(commentGood - 1, commentId);
        }
        return R.success("操作成功");
    }


    @ApiOperation("通过文章ID获取评论")
    @GetMapping("/getComment/{articleId}")
    public R<List<Comment>> getComment(@PathVariable("articleId") Integer articleId) {
        List<Comment> comments = commentService.getByArticleID(articleId);
        return R.success(comments);
    }


    @ApiOperation(value = "通过文章ID获取评论和回复")
    @GetMapping("getCommentReply/{articleId}")
    public R<List<CommentReplyDto>> getCommentReply(@PathVariable("articleId") Integer articleId) {
        List<Comment> commentList = commentService.getByArticleID(articleId);
        List<CommentReplyDto> commentReplyList = new ArrayList<>();
        //填充评论
        if (!commentList.isEmpty()) {
            for (int i = 0; i < commentList.size(); i++) {

                CommentReplyDto commentReply = new CommentReplyDto();

                Comment comment = commentList.get(i);
                Integer commentId = comment.getCommentId();

                commentReply.setCommentId(commentId);
                commentReply.setCommentTime(comment.getCommentTime());
                commentReply.setCommentContent(comment.getCommentContent());
                commentReply.setCommentGood(comment.getCommentGood());
                commentReply.setInputShow(false);

                //得到回复用户的id，通过id查询用户信息
                Integer userId = comment.getCommentUserId();
                User user = userService.getById(userId);

                commentReply.setUserId(userId);
                commentReply.setPortrait(user.getPortrait());
                commentReply.setNickname(user.getNickname());

                //通过回复的Id去获取回复内容

                List<Reply> replyList = replyService.getByCommentID(commentId);

                List<ReplyDto> replyDtoList = new ArrayList<>();

                //填充回复
                if (!replyList.isEmpty()) {

                    for (int j = 0; j < replyList.size(); j++) {

                        ReplyDto replyDto = new ReplyDto();
                        Reply reply = replyList.get(j);

                        replyDto.setReplyId(reply.getReplyId());
                        replyDto.setReplyContent(reply.getReplyContent());
                        replyDto.setReplyTime(reply.getReplyTime());
                        replyDto.setInputShow(false);
                        replyDto.setReplyGood(reply.getReplyGood());

                        Integer fromUserId = reply.getReplyUserId();
                        User user1 = userService.getById(fromUserId);

                        replyDto.setPortrait(user1.getPortrait());
                        replyDto.setNickname(user1.getNickname());

                        Integer toUserId = reply.getReplyToUserId();
                        User user2 = userService.getById(toUserId);

                        replyDto.setReplyToUserId(user2.getId());
                        replyDto.setReplyToNickname(user2.getNickname());


                        replyDtoList.add(replyDto);

                    }
                }

                commentReply.setReply(replyDtoList);

                commentReplyList.add(commentReply);
            }

        }


        return R.success(commentReplyList);

    }




    @ApiOperation("编辑评论")
    @PostMapping("/update")
    public R<String> update(@RequestBody Comment comment){
        boolean update = commentService.updateById(comment);
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("审核")
    @PostMapping("/change")
    public R<String> change(@RequestBody Comment comment){
        boolean update = commentService.update(Wrappers.lambdaUpdate(Comment.class).set(Comment::getAuditStatus,comment.getAuditStatus()).eq(Comment::getCommentId,comment.getCommentId()));
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }


    @ApiOperation("获取评论列表")
    @PostMapping("/getList")
    public R<List<Comment>> getList(){
        List<Comment> list = commentService.list();
        return R.success(list);
    }

    @ApiOperation("获取评论列表")
    @GetMapping("/getById/{commentId}")
    public R<Comment> getByIdComment(@PathVariable("commentId") Integer commentId){
        Comment comment = commentService.getById(commentId);
        return R.success(comment);
    }

    @ApiOperation("根据id删除评论")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = commentService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }

    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = commentService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }

    @ApiOperation("获取当天评论数量")
    @GetMapping("/getNowTime")
    public R<Integer> getNowTime(){
        String now = String.valueOf(LocalDateTime.now()).split("T")[0];
        log.info("{}",now);
        int size = commentService.list(Wrappers.lambdaQuery(Comment.class).like(Comment::getCommentTime, now)).size();
        return R.success(size);
    }


}

