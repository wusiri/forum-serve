package wlong.work.forumserve.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wlong.work.forumserve.common.R;
import wlong.work.forumserve.domain.*;
import wlong.work.forumserve.dto.CommentReplyDto;
import wlong.work.forumserve.dto.CommunityLabelDto;
import wlong.work.forumserve.dto.ReplyDto;
import wlong.work.forumserve.service.ArticleLabelService;
import wlong.work.forumserve.service.CommunityService;
import wlong.work.forumserve.service.UploadImageService;

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
@RequestMapping("/tags")
@Api(value = "板块Controller", tags = {"板块访问接口"})
@Slf4j
public class CommunityLableController {


    @Autowired
    private CommunityService communityService;
    @Autowired
    private ArticleLabelService articleLabelService;

    @ApiOperation(value = "通过文章ID获取评论和回复")
    @GetMapping("getTag")
    public R<List<CommunityLabelDto>> getCommentReply() {
        List<CommunityLabelDto> list = new ArrayList<>();
        //查询板块的指定字段
        LambdaQueryWrapper<Community> communityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        communityLambdaQueryWrapper.select(Community::getCommunityId, Community::getCommunityName);
        List<Community> communities = communityService.list(communityLambdaQueryWrapper);
        //查询标签的指定字段
        LambdaQueryWrapper<ArticleLabel> labelLambdaQueryWrapper=new LambdaQueryWrapper<>();
        labelLambdaQueryWrapper.select(ArticleLabel::getLabelId,ArticleLabel::getLabelName);
        List<ArticleLabel> labels = articleLabelService.list(labelLambdaQueryWrapper);
        //填充字段
        if (!communities.isEmpty()) {
            for (int i = 0; i < communities.size(); i++) {
                CommunityLabelDto communityLabelDto = new CommunityLabelDto();
                Community community = communities.get(i);
                communityLabelDto.setCommunityId(community.getCommunityId());
                communityLabelDto.setCommunityName(community.getCommunityName());
               


               
                
            }
            
        }

