package wlong.work.forumserve.service.impl;

import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.dao.UserDao;
import wlong.work.forumserve.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

}
