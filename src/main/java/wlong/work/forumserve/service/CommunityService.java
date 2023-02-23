package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wlong.work.forumserve.domain.Community;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface CommunityService extends IService<Community> {

    Page<Community> getPage(Integer page, Integer pageSize, String name);

    boolean updateGoodNum(int communityGoodNum, Integer communityId);


}
