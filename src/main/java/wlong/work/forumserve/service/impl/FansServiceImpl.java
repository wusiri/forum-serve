package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.dao.UserDao;
import wlong.work.forumserve.domain.Fans;
import wlong.work.forumserve.dao.FansDao;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.service.FansService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
@Slf4j
public class FansServiceImpl extends ServiceImpl<FansDao, Fans> implements FansService {

    @Resource
    private FansDao fansDao;


    @Resource
    private UserDao userDao;


    /**
     * 从列表中拿到用户Id
     *
     * @param fans
     * @return
     */
    private List<Integer> getId(List<Fans> fans) {
        List<Integer> ids = new ArrayList<>();
        for (Fans fan : fans) {
            //关注用户为空，则为粉丝id
            if (fan.getAttentionId() == null) {
                ids.add(fan.getUserId());
            }
            //粉丝用户为空，则为关注id
            if (fan.getUserId() == null) {
                ids.add(fan.getAttentionId());
            }
        }
        return ids;
    }

    /**
     * 通过id获取用户信息列表
     * @param ids
     * @return
     */
    private List<User> getUsers(List<Integer> ids){
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId,ids);
        List<User> users = userDao.selectList(queryWrapper);
        return users;
    }

    @Override
    public List<User> getAttentionId(Integer attentionId) {
        //查询当前用户所对应的粉丝用户Id
        LambdaQueryWrapper<Fans> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Fans::getUserId).eq(Fans::getAttentionId, attentionId).eq(Fans::getState,1);
        //获取到用户id
        List<Fans> fans = fansDao.selectList(queryWrapper);
        if(fans.isEmpty()){
            return null;
        }
        log.info(Arrays.toString(new List[]{fans}));
        List<Integer> list = getId(fans);
        System.out.println(list);
        //查询对应的用户信息
        List<User> users = getUsers(list);
        return users;
    }

    @Override
    public List<User> getUserId(Integer userId) {
        //查询当前用户所对应的关注用户Id
        LambdaQueryWrapper<Fans> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Fans::getAttentionId).eq(Fans::getUserId, userId).eq(Fans::getState,1);
        //获取到用户id
        List<Fans> fans = fansDao.selectList(queryWrapper);
        if(fans.isEmpty()){
            return null;
        }
        log.info(Arrays.toString(new List[]{fans}));
        List<Integer> list = getId(fans);
        //查询对应的用户信息
        List<User> users = getUsers(list);
        return users;
    }


}
