package wlong.work.forumserve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import wlong.work.forumserve.domain.ArticleType;
import wlong.work.forumserve.dao.ArticleTypeDao;
import wlong.work.forumserve.service.ArticleTypeService;
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
public class ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeDao, ArticleType> implements ArticleTypeService {


    @Resource
    private ArticleTypeDao articleTypeDao;

    @Override
    public Page<ArticleType> getPage(Integer page, Integer pageSize, String name) {

        //分页构造器
        Page<ArticleType> pageInfo = new Page<>(page,pageSize);
        //过滤条件
        LambdaQueryWrapper<ArticleType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), ArticleType::getTypeName, name);
        //添加排序条件
        queryWrapper.orderByAsc(ArticleType::getTypeId);
        //执行查询
        articleTypeDao.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }
}
