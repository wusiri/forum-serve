package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.dao.LikeDao;
import wlong.work.forumserve.domain.Article;
import wlong.work.forumserve.dao.ArticleDao;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {


    @Resource
    private ArticleDao articleDao;


    @Resource
    private LikeDao likeDao;


    @Override
    public List<Article> getHeader(Integer articleTypeId, Integer articleLabelId) {
//        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ge("recommend", 1); // 查询recomment >= 1的记录
//        queryWrapper.select("article_id", "article_title", "create_time");
//        queryWrapper.last("limit 5");       //只查询 5 条数据
//        return articleDao.selectList(queryWrapper);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (articleLabelId == 0) {
            if (articleTypeId == 1 || articleTypeId == 2) {
                queryWrapper.eq(Article::getArticleTypeId, articleTypeId);
            }
        } else {
            if (articleTypeId == 1 || articleTypeId == 2) {
                queryWrapper.eq(Article::getArticleTypeId, articleTypeId).eq(Article::getArticleLabelId, articleLabelId);
            }
        }
        queryWrapper.eq(Article::getRecommend, 1).eq(Article::getAuditState, 1).orderByDesc(Article::getArticleViewNum);
        return articleDao.selectList(queryWrapper);
    }

    @Override
    public List<Article> getRecommend() {
//        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("article_good_num");   //按照点赞数量推荐
//        queryWrapper.select("article_id", "article_title", "article_author");
//        queryWrapper.last("limit 3");
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        1.降序order by desc
//        2.升序order by 列名asc
        queryWrapper.eq(Article::getAuditState, 1).orderByDesc(Article::getArticleViewNum);
        return articleDao.selectList(queryWrapper);
    }

    @Override
    public List<Article> getNew(Integer articleTypeId, Integer articleLabelId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (articleLabelId == 0) {
            if (articleTypeId == 1 || articleTypeId == 2) {
                queryWrapper.eq(Article::getArticleTypeId, articleTypeId);
            }
        } else {
            if (articleTypeId == 1 || articleTypeId == 2) {
                queryWrapper.eq(Article::getArticleTypeId, articleTypeId).eq(Article::getArticleLabelId, articleLabelId);
            }
        }
        queryWrapper.eq(Article::getAuditState, 1).orderByDesc(Article::getCreateTime);
        return articleDao.selectList(queryWrapper);
    }

    @Override
    public List<Article> getHot(Integer articleTypeId, Integer articleLabelId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (articleLabelId == 0) {
            if (articleTypeId == 1 || articleTypeId == 2) {
                queryWrapper.eq(Article::getArticleTypeId, articleTypeId);
            }
        } else {
            if (articleTypeId == 1 || articleTypeId == 2) {
                queryWrapper.eq(Article::getArticleTypeId, articleTypeId).eq(Article::getArticleLabelId, articleLabelId);
            }
        }
        queryWrapper.eq(Article::getAuditState, 1).orderByDesc(Article::getArticleViewNum);
        return articleDao.selectList(queryWrapper);
    }

    @Override
    public List<Article> getArticle(Integer id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getAuditState, 1).orderByDesc(Article::getArticleViewNum);
        if (id == 1 || id == 2) {
            queryWrapper.eq(Article::getArticleTypeId, id);
        }
        return articleDao.selectList(queryWrapper);
    }

    @Override
    public boolean updateGoodNum(int articleGoodNum, Integer articleId) {
        return this.update(Wrappers.lambdaUpdate(Article.class).set(Article::getArticleGoodNum, articleGoodNum).eq(Article::getArticleId, articleId));
    }

    @Override
    public List<Article> getByUserArticle(Integer userId) {
        return this.list(Wrappers.lambdaQuery(Article.class).eq(Article::getUserId, userId).eq(Article::getAuditState, 1));
    }

    @Override
    public List<Article> getByUserAndType(Integer articleTypeId, Integer userId, Integer articleLabelId) {

        if (articleLabelId == 0) {
            return this.list(Wrappers.lambdaQuery(Article.class).eq(Article::getUserId, userId).eq(Article::getArticleTypeId, articleTypeId).eq(Article::getAuditState, 1));
        } else {
            return this.list(Wrappers.lambdaQuery(Article.class).eq(Article::getUserId, userId).eq(Article::getArticleTypeId, articleTypeId).eq(Article::getAuditState, 1).eq(Article::getArticleLabelId, articleLabelId));
        }
    }

    @Override
    public Page<Article> getPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Article> pageInfo = new Page<>(page, pageSize);
        //过滤条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Article::getArticleSummary, name);
//        like(StringUtils.isNotEmpty(name), Article::getArticleSummary, name)
        //添加排序条件
        queryWrapper.orderByAsc(Article::getArticleId);
        //执行查询
        articleDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }




}
