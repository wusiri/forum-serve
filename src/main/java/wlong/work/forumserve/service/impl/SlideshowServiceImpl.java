package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.domain.Slideshow;
import wlong.work.forumserve.dao.SlideshowDao;
import wlong.work.forumserve.service.SlideshowService;
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
public class SlideshowServiceImpl extends ServiceImpl<SlideshowDao, Slideshow> implements SlideshowService {

    @Resource
    private SlideshowDao slideshowDao;

    @Override
    public Page<Slideshow> getPage(Integer page, Integer pageSize, String name) {
//分页构造器
        Page<Slideshow> pageInfo = new Page<>(page, pageSize);
        //过滤条件
        LambdaQueryWrapper<Slideshow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Slideshow::getImageUrl, name);
        //添加排序条件
        queryWrapper.orderByAsc(Slideshow::getSlideshowId);
        //执行查询
        slideshowDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }
}
