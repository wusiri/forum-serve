package wlong.work.forumserve.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.Area;
import wlong.work.forumserve.service.AreaService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器 推荐
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */

@Api(value = "AreaController", tags = {"推荐访问接口"})
@RestController
@RequestMapping("/area")
public class AreaController {

    @Resource
    private AreaService areaService;

    @ApiOperation("获取推荐区资源图片")
    @GetMapping("/getArea")
    public R<List<Area>> getArea(){
        List<Area> list = areaService.list();
        return R.success(list);
    }

}

