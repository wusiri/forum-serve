package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Community;
import wlong.work.forumserve.domain.CommunityUser;
import wlong.work.forumserve.domain.Like;
import wlong.work.forumserve.domain.User;
import wlong.work.forumserve.service.CommunityService;
import wlong.work.forumserve.service.CommunityUserService;
import wlong.work.forumserve.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/communityUser")
@Api(value = "板块关注用户Controller", tags = {"板块关注用户访问接口"})
@Slf4j
public class CommunityUserController {


    @Resource
    private CommunityUserService communityUserService;

    @Resource
    private UserService userService;

    @Resource
    private CommunityService communityService;


    @GetMapping("/getState")
    @ApiOperation("获取社区关注状态")
    public R<Integer> getState(@RequestParam("communityId") Integer communityId,@RequestParam("userId") Integer userId){
        CommunityUser one = communityUserService.getOne(Wrappers.lambdaQuery(CommunityUser.class).eq(CommunityUser::getCommunityId, communityId).eq(CommunityUser::getUserId, userId));
        if(one==null){
            return R.success(0);
        }
        return R.success(one.getState());
    }


    @ApiOperation("获取板块所有的关注用户信息")
    @GetMapping("/getByCommunityId/{communityId}")
    public R<List<User>> getByUserId(@PathVariable("communityId") Integer communityId) {
        List<CommunityUser> list = communityUserService.list(Wrappers.lambdaQuery(CommunityUser.class).eq(CommunityUser::getCommunityId, communityId).eq(CommunityUser::getState,1));
        List<User> users = new ArrayList<>();
        if (!list.isEmpty()) {
            for (CommunityUser c : list) {
                User user = userService.getById(c.getUserId());
                users.add(user);
            }
        }
        return R.success(users);
    }


    @ApiOperation("增加关注和取消关注")
    @PutMapping("/add")
    public R<String> getByUserId(@RequestParam("communityId") Integer communityId,@RequestParam("userId") Integer userId,@RequestParam("state") Boolean state) {
        //获取关注数量
        Integer userNum = communityService.getById(communityId).getCommunityUserNum();
        log.info("点赞数:{}",userNum);
        //isDisplay true表示点赞,false表示取消
        if (state) {
            //获取状态码
            CommunityUser communityUser = communityUserService.getOne(Wrappers.lambdaQuery(CommunityUser.class).eq(CommunityUser::getCommunityId, communityId).eq(CommunityUser::getUserId, userId));
            if (communityUser == null) {
                CommunityUser  info = new CommunityUser();
                info.setCommunityId(communityId);
                info.setState(1);
                info.setUserId(userId);
                communityUserService.save(info);
            } else {
                communityUserService.update(Wrappers.lambdaUpdate(CommunityUser.class).set(CommunityUser::getState,1).eq(CommunityUser::getCommunityId,communityId).eq(CommunityUser::getUserId,userId));
            }
            communityService.update(Wrappers.lambdaUpdate(Community.class).set(Community::getCommunityUserNum, userNum+1).eq(Community::getCommunityId, communityId));

        } else {
            communityUserService.update(Wrappers.lambdaUpdate(CommunityUser.class).set(CommunityUser::getState,0).eq(CommunityUser::getCommunityId,communityId).eq(CommunityUser::getUserId,userId));
            communityService.update(Wrappers.lambdaUpdate(Community.class).set(Community::getCommunityUserNum, userNum-1).eq(Community::getCommunityId, communityId));
        }
        return R.success("操作成功");
    }

}

