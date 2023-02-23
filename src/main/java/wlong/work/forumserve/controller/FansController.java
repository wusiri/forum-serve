package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.*;
import wlong.work.forumserve.service.FansService;
import wlong.work.forumserve.service.MessageService;
import wlong.work.forumserve.service.UserService;

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
@RequestMapping("/fans")
@Api(value = "关注Controller",tags = {"关注访问接口"})
@Slf4j
public class FansController {


    @Resource
    private FansService fansService;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;
    /*userId:当前用户
    attentionId:当前用户关注的用户*/

    @GetMapping("/getState")
    @ApiOperation("获取关注状态")
    public R<Integer> getState(@RequestParam("attentionId") Integer attentionId,@RequestParam("userId") Integer userId){
        Fans one = fansService.getOne(Wrappers.lambdaQuery(Fans.class).eq(Fans::getAttentionId, attentionId).eq(Fans::getUserId, userId));
        if(one==null){
            return R.success(0);
        }
        return R.success(one.getState());
    }

    @ApiOperation("增加关注和取消关注")
    @PutMapping("/add")
    public R<String> getByUserId(@RequestParam("attentionId") Integer attentionId,@RequestParam("userId") Integer userId,@RequestParam("state") Boolean state) {
        //获取关注数量
        Integer fans = userService.getById(attentionId).getFans();
        log.info("点赞数:{}",fans);
        Message message=new Message();
        message.setMessageUser(attentionId);
        message.setMessageFromUser(userId);
        message.setMessageType(1);
        //state true表示点赞,false表示取消
        if (state) {
            //获取状态码
            Fans one = fansService.getOne(Wrappers.lambdaQuery(Fans.class).eq(Fans::getAttentionId, attentionId).eq(Fans::getUserId, userId));
            if (one == null) {
                Fans  info = new Fans();
                info.setAttentionId(attentionId);
                info.setState(1);
                info.setUserId(userId);
                fansService.save(info);
                messageService.save(message);
            } else {
                fansService.update(Wrappers.lambdaUpdate(Fans.class).set(Fans::getState,1).eq(Fans::getAttentionId,attentionId).eq(Fans::getUserId,userId));
            }
            userService.update(Wrappers.lambdaUpdate(User.class).set(User::getFans, fans+1).eq(User::getId, userId));
            messageService.update(Wrappers.lambdaUpdate(Message.class).set(Message::getMessageInfo,"关注了你,你的粉丝增加了").eq(Message::getMessageUser,attentionId).eq(Message::getMessageFromUser,userId).eq(Message::getMessageType,1));

        } else {
            fansService.update(Wrappers.lambdaUpdate(Fans.class).set(Fans::getState,0).eq(Fans::getAttentionId,attentionId).eq(Fans::getUserId,userId));
            userService.update(Wrappers.lambdaUpdate(User.class).set(User::getFans, fans-1).eq(User::getId, userId));
            messageService.update(Wrappers.lambdaUpdate(Message.class).set(Message::getMessageInfo,"取关了你,你的粉丝减少了").eq(Message::getMessageUser,attentionId).eq(Message::getMessageFromUser,userId).eq(Message::getMessageType,1));
        }
        return R.success("操作成功");
    }

    @ApiOperation("获取当前用户所有的关注")
    @GetMapping("/getByUserId/{userId}")
    public R<List<User>> getByUserId(@PathVariable("userId") Integer userId){
        List<User> users = fansService.getUserId(userId);
        return R.success(users);
    }

    @ApiOperation("获取当前用户所有的粉丝")
    @GetMapping("/getByAttentionId/{attentionId}")
    public R<List<User>> getByAttentionId(@PathVariable("attentionId") Integer attentionId){
        List<User> users = fansService.getAttentionId(attentionId);
        return R.success(users);
    }



}

