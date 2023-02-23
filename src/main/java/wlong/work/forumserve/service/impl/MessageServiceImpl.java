package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wlong.work.forumserve.dao.MessageDao;
import wlong.work.forumserve.domain.Message;
import wlong.work.forumserve.dto.MessageUserDto;
import wlong.work.forumserve.service.MessageService;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {


    @Resource
    private MessageDao messageDao;

    @Override
    public Page<Message> getPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Message> pageInfo = new Page<>(page,pageSize);
        //过滤条件
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Message::getMessageInfo, name);
        //添加排序条件
        queryWrapper.orderByAsc(Message::getMessageId);
        //执行查询
        messageDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Transactional
    @Override
    public List<Message> getList(Integer type, Integer userId, Integer state) {
        return messageDao.selectList(Wrappers.lambdaQuery(Message.class).eq(Message::getMessageType, type).eq(Message::getMessageState, state).eq(Message::getMessageUser, userId).orderByDesc(Message::getMessageTime));
    }


}
