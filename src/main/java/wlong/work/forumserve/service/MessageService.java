package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wlong.work.forumserve.domain.ArticleLabel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface ArticleLabelService extends IService<ArticleLabel> {

    Page<ArticleLabel> getPage(Integer page, Integer pageSize, String name);
}
