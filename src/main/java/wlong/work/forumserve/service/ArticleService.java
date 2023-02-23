package wlong.work.forumserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wlong.work.forumserve.domain.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
public interface ArticleService extends IService<Article> {

    List<Article> getHeader(Integer articleTypeId,Integer articleLabelId);

    List<Article> getRecommend();

    List<Article> getNew(Integer id,Integer articleLabelId);

    List<Article> getHot(Integer id,Integer articleLabelId);

    List<Article> getArticle(Integer id);

    boolean updateGoodNum(int articleGoodNum, Integer articleId);

    List<Article> getByUserArticle(Integer userId);

    List<Article> getByUserAndType(Integer articleTypeId, Integer userId,Integer articleLabelId);

    Page<Article> getPage(Integer page, Integer pageSize, String name);


}
