package wlong.work.forumserve.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wlong.work.forumserve.common.R;



@Api(value = "评论与回复Controller",tags = {"评论与回复访问接口"})
@Slf4j
@RestController
@RequestMapping("/commentReply")
public class CommentAndReplyController {




    @ApiOperation("获取全部评论与回复")
    @GetMapping("/getPage")
    public R<Page> getPage(int page, int pageSize, String name){
        Page page1=null;
        return R.success(page1);
    }
}
