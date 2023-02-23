package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wlong.work.forumserve.domain.Slideshow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface SlideshowService extends IService<Slideshow> {

    Page<Slideshow> getPage(Integer page, Integer pageSize, String name);
}
