package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import wlong.work.forumserve.dao.UserDao;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.vo.WhereVo;
import wlong.work.forumserve.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {


    @Resource
    private UserDao userDao;


    @Override
    public User login(String email, String password) {
        //条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email).eq(User::getPassword, password).eq(User::getEnabled, 1);
        return userDao.selectOne(queryWrapper);
    }

    @Override
    public boolean getByUsername(String username) {
        //条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userDao.selectOne(queryWrapper) == null;
    }

    @Override
    public User getByName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userDao.selectOne(queryWrapper);
    }

    @Override
    public boolean updatePortrait(Integer id, String pathDB) {
        return this.update(Wrappers.lambdaUpdate(User.class).set(User::getPortrait, pathDB).eq(User::getId, id));
    }

    /**
     * 修改信息(除头像)
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserinfo(User user) {
        User user1 = new User();
        user1.setId(user.getId());
        user1.setNickname(user.getNickname());
        user1.setGender(user.getGender());
        user1.setCity(user.getCity());
        user1.setIntroduce(user.getIntroduce());
        return userDao.updateById(user1) > 0;
    }

    @Override
    public boolean updatePassword(User user) {
        User user1 = new User();
        user1.setId(user.getId());
        //存储md5加密后的密码
        String s = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        System.out.println(s);
        user1.setPassword(s);
        return userDao.updateById(user1) > 0;
    }

    @Override
    public Page<User> getPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<User> pageInfo = new Page<>(page, pageSize);
        //过滤条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        .like(StringUtils.isNotEmpty(name), User::getUsername, name)
//        .like(StringUtils.isNotEmpty(name), User::getEmail, name)
        queryWrapper.like(StringUtils.isNotEmpty(name), User::getNickname, name);
        //添加排序条件
        queryWrapper.orderByAsc(User::getId);
        //执行查询
        userDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Override
    public List<WhereVo> getWhere() {
        List<WhereVo> whereVos = new ArrayList<>();
        //查询所有地址列表
        List<User> list = userDao.selectList(Wrappers.lambdaQuery(User.class));
        Set<String> city = new HashSet<>();
        log.info("{}", list);
        //将省份切割处理,并存入map，key中
        for (User user : list) {
            if (user.getCity() != null) {
                //切割出省份名称
                String[] split = user.getCity().split("省-");
                if (split[0] != null) {
                    //添加到Set集合
                    city.add(split[0]);
                }
            }
        }
        //查询相关数量
        for (String c : city) {
            //通过模糊查询查询数量
            int size = userDao.selectList(Wrappers.lambdaQuery(User.class).like(User::getCity, c)).size();
            //填充数据
            WhereVo whereVo = new WhereVo();
            whereVo.setName(c);
            whereVo.setValue(size);
            whereVos.add(whereVo);
        }
        return whereVos;
    }
}
