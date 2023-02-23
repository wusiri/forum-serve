package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wlong.work.forumserve.domain.ArticleType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface ArticleTypeService extends IService<ArticleType> {

    Page<ArticleType> getPage(Integer page, Integer pageSize, String name);
}
