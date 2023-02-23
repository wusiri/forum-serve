package wlong.work.forumserve.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.vo.WhereVo;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface UserService extends IService<User> {

    User login(String email, String password);

    boolean getByUsername(String username);

    User getByName(String username);

    boolean updatePortrait(Integer id, String pathDB);

    boolean updateUserinfo(User user);

    boolean updatePassword(User user);

    Page<User> getPage(Integer page, Integer pageSize, String name);

    List<WhereVo> getWhere();
}
