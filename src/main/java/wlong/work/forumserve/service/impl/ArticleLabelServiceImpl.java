package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.domain.ArticleLabel;
import wlong.work.forumserve.dao.ArticleLabelDao;
import wlong.work.forumserve.service.ArticleLabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Service
public class ArticleLabelServiceImpl extends ServiceImpl<ArticleLabelDao, ArticleLabel> implements ArticleLabelService {


    @Resource
    private ArticleLabelDao articleLabelDao;

    @Override
    public Page<ArticleLabel> getPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<ArticleLabel> pageInfo = new Page<>(page,pageSize);
        //过滤条件
        LambdaQueryWrapper<ArticleLabel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), ArticleLabel::getLabelName, name);
        //添加排序条件
        queryWrapper.orderByAsc(ArticleLabel::getLabelId);
        //执行查询
        articleLabelDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }
}
