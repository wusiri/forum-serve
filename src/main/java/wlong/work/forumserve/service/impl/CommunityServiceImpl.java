package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.domain.Admin;
import wlong.work.forumserve.domain.Community;
import wlong.work.forumserve.dao.CommunityDao;
import wlong.work.forumserve.domain.Reply;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.service.CommunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityDao, Community> implements CommunityService {

    @Resource
    private CommunityDao communityDao;

    @Override
    public Page<Community> getPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Community> pageInfo = new Page<>(page, pageSize);
        //过滤条件
        LambdaQueryWrapper<Community> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Community::getCommunityName, name);
        //添加排序条件
        queryWrapper.orderByAsc(Community::getCommunityId);
        //执行查询
        communityDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Override
    public boolean updateGoodNum(int communityGoodNum, Integer communityId) {
        return this.update(Wrappers.lambdaUpdate(Community.class).set(Community::getGoodNum, communityGoodNum).eq(Community::getCommunityId, communityId));
    }


}
