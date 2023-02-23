package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import wlong.work.forumserve.common.CustomException;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Article;
import wlong.work.forumserve.domain.Reply;
import wlong.work.forumserve.domain.Slideshow;
import wlong.work.forumserve.service.SlideshowService;
import wlong.work.forumserve.service.UploadImageService;
import wlong.work.forumserve.utils.FileUtil;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static wlong.work.forumserve.utils.FontChangeImageUtils.createImage;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/slideshow")
@Api(value = "轮播图Controller",tags = {"轮播图访问接口"})
@Slf4j
public class SlideshowController {
    @Resource
    private SlideshowService slideshowService;

    @Resource
    private UploadImageService uploadImageService;

    @ApiOperation("获取全部轮播图")
    @GetMapping("/getSlideshow")
    public R<List<Slideshow>> getSlideshow(){
        List<Slideshow> list = slideshowService.list(Wrappers.lambdaQuery(Slideshow.class).eq(Slideshow::getEnabled,1));
        return R.success(list);
    }

    @ApiOperation("获取轮播图信息")
    @GetMapping("/getPage")
    public R<Page<Slideshow>> getPage(Integer page, Integer pageSize, String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<Slideshow> page1 = slideshowService.getPage(page, pageSize, name);
        return R.success(page1);
    }


    @ApiOperation("新增轮播图")
    @PostMapping("/save")
    public R<String> publish(@RequestBody Slideshow slideshow) {
        boolean save = slideshowService.save(slideshow);
        if (save) {
            return R.success("发布成功!!!");
        } else {
            return R.success("发布失败");
        }
    }

    @ApiOperation("上传封面")
    @PutMapping("/saveImg")
    public R<String> updatePhoto(@RequestParam("id") Integer id, @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            String path = uploadImageService.uploadQNImg(file);
            log.info("七牛云返回的图片链接:{}", path);
            //链接处理
            int i = path.indexOf("?");
            String paths = "http://" + path.substring(0, i).replace("%2F", "/");
            log.info("处理后的链接:{}", paths);
            /*qiniu.wlong.work/code%2Fduck%2F20230113-850f938c035746e1ab1e112548e739fa.jpg
                ?e=1673605941&token=IaXM0rTcHF4rF__5qn9iuWhHqJgW3XaCXSb1FsAV:v7bOVgb9BsrhPXChymf7exlClck=
                http://qiniu.wlong.work/forum/v2-e0d294a91376a3527fc07a61dd111cfe_r.jpg*/

            return R.success(paths);
        }
        return R.error("上传失败");
    }


    @ApiOperation("编辑轮播图")
    @PostMapping("/update")
    public R<String> update(@RequestBody Slideshow slideshow){
        log.info("{}",slideshow);
        boolean update = slideshowService.updateById(slideshow);
        if(update){
            return R.success("添加成功");
        }else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("根据id删除轮播图")
    @DeleteMapping("/deleteById/{id}")
    public R<String> deleteById(@PathVariable("id") Integer id){
        boolean remove = slideshowService.removeById(id);
        if(remove){
            return R.success("删除成功!");
        }else {
            return R.error("删除失败");
        }
    }



    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = slideshowService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }
}

