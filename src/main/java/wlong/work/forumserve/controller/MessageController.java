package wlong.work.forumserve.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.ArticleLabel;
import wlong.work.forumserve.domain.Message;
import wlong.work.forumserve.dto.MessageUserDto;
import wlong.work.forumserve.service.MessageService;
import wlong.work.forumserve.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Slf4j
@RestController
@RequestMapping("/message")
@Api(value = "消息Controller",tags = {"消息访问接口"})
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;



    @ApiOperation("获取消息通过类型")
    @GetMapping("/getMessage")
    public R<List<MessageUserDto>> getIssueByType(@RequestParam("type") Integer type, @RequestParam("userId") Integer userId,@RequestParam("state") Integer state) {
        log.info("类型:{}",type);
        log.info("状态:{}",state);
        List<MessageUserDto> messageUsers=new ArrayList<>();
        List<Message> list=messageService.getList(type,userId,state);
        //遍历添加
        for (Message message:list) {
            MessageUserDto messageUser=new MessageUserDto();
            messageUser.setMessageId(message.getMessageId());
            messageUser.setUserId(message.getMessageFromUser());
            messageUser.setMessageInfo(message.getMessageInfo());
            messageUser.setType(message.getMessageType());
            String nickname = userService.getById(message.getMessageFromUser()).getNickname();
            messageUser.setNickname(nickname);
            messageUser.setState(message.getMessageState());
            messageUser.setMessageTime(message.getMessageTime());
            messageUsers.add(messageUser);
        }
        return R.success(messageUsers);
    }

    @ApiOperation("获取消息条目")
    @GetMapping("/getMessageNum")
    public R<Integer> getIssueByTypeNum(@RequestParam("type") Integer type, @RequestParam("userId") Integer userId,@RequestParam("state") Integer state) {
        log.info("类型:{}",type);
        log.info("状态:{}",state);
        List<Message> list = messageService.list(Wrappers.lambdaQuery(Message.class).eq(Message::getMessageType, type).eq(Message::getMessageState, state).eq(Message::getMessageUser, userId));
        Integer size = list.size();
        return R.success(size);
    }


    @ApiOperation("获取消息通过id")
    @GetMapping("/getMessage/{messageId}")
    public R<Message> getMessageId(@PathVariable("messageId") Integer messageId){
        Message message = messageService.getById(messageId);
        return R.success(message);
    }

    @ApiOperation("获取消息信息")
    @GetMapping("/getPage")
    public R<Page<Message>> getPage(Integer page, Integer pageSize, String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<Message> page1 = messageService.getPage(page, pageSize, name);
        return R.success(page1);
    }

    @ApiOperation("更改状态")
    @PutMapping("/updateState/{messageId}")
    public R<String> updateState(@PathVariable("messageId") Integer messageId) {
        log.info("id:{}", messageId);
        boolean update = messageService.update(Wrappers.lambdaUpdate(Message.class).set(Message::getMessageState, 1).eq(Message::getMessageId, messageId));
        if(update){
            return R.success("操作成功");
        }else {
            return R.error("操作失败");
        }
    }

    @ApiOperation("添加消息")
    @PostMapping("/save")
    public R<String> save(@RequestBody Message message){
        message.setMessageType(1);
        message.setMessageFromUser(0);
        boolean save = messageService.save(message);
        if(save){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }


    @ApiOperation("编辑消息")
    @PostMapping("/update")
    public R<String> update(@RequestBody Message message){
        boolean update = messageService.updateById(message);
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("根据id删除消息")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = messageService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }



    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        log.info("{}",ids);
        boolean remove = messageService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }

    @ApiOperation("删除选中内容")
    @DeleteMapping("/deletes")
    public R<String> deleteByMessages(@RequestParam List<Message> messages) {
        log.info("{}",messages);
        ArrayList<Integer> ids = new ArrayList<>();
        for (Message message:messages) {
            ids.add(message.getMessageId());
        }
        boolean remove = messageService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }
}
