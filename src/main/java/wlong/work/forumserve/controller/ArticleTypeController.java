package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.ArticleType;
import wlong.work.forumserve.service.ArticleTypeService;

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

@RestController
@RequestMapping("/articleType")
@Api(value = "文章类型Controller",tags = {"文章类型访问接口"})
@Slf4j
public class ArticleTypeController {
    @Resource
    private ArticleTypeService articleTypeService;

    @ApiOperation("获取文章类型")
    @GetMapping("/getArticleType")
    public R<List<ArticleType>> getArticleType(){
        List<ArticleType> list = articleTypeService.list();
        return R.success(list);
    }

    @ApiOperation("获取类型信息")
    @GetMapping("/getPage")
    public R<Page<ArticleType>> getPage(Integer page, Integer pageSize, String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<ArticleType> page1 = articleTypeService.getPage(page, pageSize, name);
        return R.success(page1);
    }


    @ApiOperation("添加类型")
    @PostMapping("/save")
    public R<String> save(@RequestBody ArticleType articleType){
        boolean save = articleTypeService.save(articleType);
        if(save){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }


    @ApiOperation("编辑类型")
    @PostMapping("/update")
    public R<String> update(@RequestBody ArticleType articleType){
        boolean update = articleTypeService.updateById(articleType);
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("根据id删除类型")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = articleTypeService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }

    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = articleTypeService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }
}

