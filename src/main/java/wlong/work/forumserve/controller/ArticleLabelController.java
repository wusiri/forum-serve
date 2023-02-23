package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wlong.work.forumserve.common.R;
import wlong.work.forumserve.dao.ArticleLabelDao;
import wlong.work.forumserve.domain.Admin;
import wlong.work.forumserve.domain.ArticleLabel;
import wlong.work.forumserve.service.ArticleLabelService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Api(value = "文章标签Controller",tags = {"文章标签访问接口"})
@RestController
@RequestMapping("/articleLabel")
@Slf4j
public class ArticleLabelController {

    @Resource
    private ArticleLabelService articleLabelService;

    @ApiOperation("获取文章标签")
    @GetMapping("/getArticleLabel")
    public R<List<ArticleLabel>> getArticleLabel(){
        List<ArticleLabel> list = articleLabelService.list();
        return R.success(list);
    }

    @ApiOperation("获取文章标签id")
    @GetMapping("/getArticleLabel/{labelId}")
    public R<ArticleLabel> getArticleLabelId(@PathVariable("labelId") Integer labelId){
        ArticleLabel label = articleLabelService.getById(labelId);
        return R.success(label);
    }

    @ApiOperation("获取文章标签信息")
    @GetMapping("/getPage")
    public R<Page<ArticleLabel>> getPage(Integer page, Integer pageSize, String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<ArticleLabel> page1 = articleLabelService.getPage(page, pageSize, name);
        return R.success(page1);
    }

    @ApiOperation("获取标签信息")
    @GetMapping("/getName/{name}")
    public R<ArticleLabel> getPage(@PathVariable("name") String name){
        log.info("{}",name);
        ArticleLabel label = articleLabelService.getOne(Wrappers.lambdaQuery(ArticleLabel.class).eq(ArticleLabel::getLabelName, name));
        return R.success(label);
    }


    @ApiOperation("添加标签")
    @PostMapping("/save")
    public R<String> save(@RequestBody ArticleLabel articleLabel){
        boolean save = articleLabelService.save(articleLabel);
        if(save){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }


    @ApiOperation("编辑标签")
    @PostMapping("/update")
    public R<String> update(@RequestBody ArticleLabel articleLabel){
        boolean update = articleLabelService.updateById(articleLabel);
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("根据id删除标签")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = articleLabelService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }

    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = articleLabelService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }



}

