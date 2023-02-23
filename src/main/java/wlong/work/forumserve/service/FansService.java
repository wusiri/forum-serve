package wlong.work.forumserve.service;

import wlong.work.forumserve.domain.Fans;
import com.baomidou.mybatisplus.extension.service.IService;
import wlong.work.forumserve.domain.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface FansService extends IService<Fans> {

    List<User> getAttentionId(Integer attentionId);

    List<User> getUserId(Integer userId);
}
