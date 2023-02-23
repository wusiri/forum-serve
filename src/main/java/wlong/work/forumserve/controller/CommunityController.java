package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Community;
import wlong.work.forumserve.domain.Like;
import wlong.work.forumserve.service.CommentService;
import wlong.work.forumserve.service.CommunityService;
import wlong.work.forumserve.service.LikeService;
import wlong.work.forumserve.service.UploadImageService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/community")
@Api(value = "板块Controller",tags = {"板块访问接口"})
@Slf4j
public class CommunityController {


    @Resource
    private LikeService likeService;


    @Resource
    private UploadImageService uploadImageService;
    @Resource
    private CommunityService communityService;

    //文件路径
    @Value("${forum.path}")
    private String root;


    private String path;


    @ApiOperation("更新点赞数")
    @PutMapping("/addGood")
    public R<String> addGood(@RequestParam("type") Boolean isDisplay, @RequestParam("communityId") Integer communityId, @RequestParam("userId") Integer userId) {
        //获取点赞数量
        Integer goodNum = communityService.getById(communityId).getGoodNum();
        log.info("点赞数:{}",goodNum);
        //isDisplay true表示点赞,false表示取消
        if (isDisplay) {
            //获取状态码
            Like like = likeService.getCommunityLikeState(communityId, userId);
            if (like == null) {
                Like communityLike = new Like();
                communityLike.setCommunityId(communityId);
                communityLike.setCommunityLikeNum(1);
                communityLike.setUserId(userId);
                likeService.save(communityLike);
            } else {
                likeService.setCommunityLikeState(communityId, userId, 1);
            }
            communityService.updateGoodNum(goodNum + 1, communityId);

        } else {
            likeService.setCommunityLikeState(communityId, userId, 0);
            communityService.updateGoodNum(goodNum - 1, communityId);
        }
        return R.success("操作成功");
    }


    @ApiOperation("获取所有板块")
    @GetMapping("/getCommunity")
    public R<List<Community>> getCommunity(){
        List<Community> list = communityService.list();
        return R.success(list);
    }


    @ApiOperation("分页获取")
    @GetMapping("/getPage")
    public R<Page<Community>> getPage(Integer page,Integer pageSize,String name){
        log.info("{},{},{}",page,pageSize,name);
        Page<Community> page1 = communityService.getPage(page, pageSize, name);
        return R.success(page1);
    }

    @ApiOperation("获取指定板块信息")
    @GetMapping("/getByCommunityId/{communityId}")
    public R<Community> getCommunityById(@PathVariable("communityId") Integer communityId){
        Community community = communityService.getById(communityId);
        return R.success(community);
    }


    @ApiOperation("上传封面")
    @PutMapping("/add")
    public R<String> add(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            path = uploadImageService.uploadQNImg(file);
            int i = path.indexOf("?");
            String paths="http://"+path.substring(0,i).replace("%2F","/");
            log.info("处理后的链接:{}",paths);
            //存储到数据库
            return R.success(paths);
        }
        return R.error("上传失败");
    }


    @ApiOperation("增加板块")
    @PostMapping("/save")
    public R<String> save(@RequestBody Community community){
        boolean save = communityService.save(community);
        if(save){
          return   R.success("添加成功");
        }else {
          return   R.error("添加失败");
        }
    }

    @ApiOperation("更新板块")
    @PostMapping("/update")
    public R<String> update(@RequestBody Community community){
        boolean update = communityService.updateById(community);
        if(update){
            return   R.success("更新成功");
        }else {
            return   R.error("更新失败");
        }
    }

    @ApiOperation("删除板块")
    @DeleteMapping("/deleteById/{communityId}")
    public R<String> deleteById(@PathVariable("communityId") Integer communityId){
        log.info("删除id为：{}",communityId);
        communityService.removeById(communityId);
        return R.success("删除成功");
    }

    @ApiOperation("删除选中内容")
    @DeleteMapping()
    public R<String> deleteByIds(@RequestBody  List<Integer> ids) {
        boolean remove = communityService.removeByIds(ids);
        if (remove) {
            return R.success("删除成功");
        } else {
            return R.success("删除失败");
        }
    }



}

