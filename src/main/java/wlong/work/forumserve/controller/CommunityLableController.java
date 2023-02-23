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
@RequestMapping("/tags")
@Api(value = "板块Controller", tags = {"搜索词访问接口"})
@Slf4j
public class CommunityLableController {


    @Resource
    private CommunityService communityService;
    @Resource
    private ArticleLabelService articleLabelService;

    @ApiOperation(value = "获取搜索词")
    @GetMapping("getTag")
    public R<List<CommunityLabelDto>> getTag() {
        List<CommunityLabelDto> list = new ArrayList<>();
        //查询板块的指定字段
        LambdaQueryWrapper<Community> communityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        communityLambdaQueryWrapper.select(Community::getCommunityId, Community::getCommunityName);
        List<Community> communities = communityService.list(communityLambdaQueryWrapper);
        //查询标签的指定字段
        LambdaQueryWrapper<ArticleLabel> labelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        labelLambdaQueryWrapper.select(ArticleLabel::getLabelId, ArticleLabel::getLabelName);
        List<ArticleLabel> labels = articleLabelService.list(labelLambdaQueryWrapper);
        //填充字段,0为板块，1为标签
        if (!communities.isEmpty()) {
            for (int i = 0; i < communities.size(); i++) {
                CommunityLabelDto communityLabelDto = new CommunityLabelDto();
                Community community = communities.get(i);
                communityLabelDto.setTagId(community.getCommunityId());
                communityLabelDto.setTagName(community.getCommunityName());
                communityLabelDto.setTagState(1);
                list.add(communityLabelDto);
            }
        }
        if(!labels.isEmpty()){
            for (int i = 0; i < labels.size(); i++) {
                CommunityLabelDto communityLabelDto = new CommunityLabelDto();
                ArticleLabel label = labels.get(i);
                communityLabelDto.setTagId(label.getLabelId());
                communityLabelDto.setTagName(label.getLabelName());
                communityLabelDto.setTagState(2);
            }
        }
        return R.success(list);
    }


    @ApiOperation(value = "获取搜索词")
    @GetMapping("getTags")
    public R<List<String>> getTags() {
        List<String> list=new ArrayList<>();
        //查询板块的指定字段
        LambdaQueryWrapper<Community> communityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        communityLambdaQueryWrapper.select(Community::getCommunityName);
        List<Community> communities = communityService.list(communityLambdaQueryWrapper);
        //查询标签的指定字段
        LambdaQueryWrapper<ArticleLabel> labelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        labelLambdaQueryWrapper.select(ArticleLabel::getLabelName);
        List<ArticleLabel> labels = articleLabelService.list(labelLambdaQueryWrapper);
        //填充字段,0为板块，1为标签
        if (!communities.isEmpty()) {
            for (int i = 0; i < communities.size(); i++) {
                Community community = communities.get(i);
                list.add(community.getCommunityName());
            }
        }
        if(!labels.isEmpty()){
            for (int i = 0; i < labels.size(); i++) {
                ArticleLabel label = labels.get(i);
                list.add(label.getLabelName());
            }
        }
        return R.success(list);
    }
}

