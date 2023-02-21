package wlong.work.forumserve.service.impl;

import wlong.work.forumserve.domain.Article;
import wlong.work.forumserve.dao.ArticleDao;
import wlong.work.forumserve.service.IArticleService;
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
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements IArticleService {

}
