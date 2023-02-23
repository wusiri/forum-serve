package wlong.work.forumserve.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_article")
@ApiModel("文章")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    /**
     * 标签id
     */
    @ApiModelProperty("文章标签")
    private Integer articleLabelId;

    /**
     * 文章作者
     */
    @ApiModelProperty("文章作者")
    private String articleAuthor;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String articleTitle;

    /**
     * 文章摘要
     */
    @ApiModelProperty("文章摘要")
    private String articleSummary;

    /**
     * 文章类型
     */
    @ApiModelProperty("文章类型")
    private Integer articleTypeId;

    /**
     * 文章内容
     */
    @ApiModelProperty("文章内容")
    private String articleContent;

    /**
     * 文章内容html
     */
    @ApiModelProperty("文章内容html")
    private String articleContentHtml;

    /**
     * 文章中的图片
     */
    @ApiModelProperty("文章内容图片")
    private String articleImage;

    /**
     * 发布文章的用户的id
     */
    @ApiModelProperty("作者id")
    private Integer userId;

    /**
     * 文章获赞数量
     */
    @ApiModelProperty("获赞数量 ")
    private Integer articleGoodNum;

    /**
     * 问题解决状态
     */
    @ApiModelProperty("解决状态")
    private Integer solveState;


    /**
     * 问题最佳答案
     */
    @ApiModelProperty("问题最佳答案 ")
    private Integer commentId;


    /**
     * 文章评论数量
     */
    @ApiModelProperty("评论数量 ")
    private Integer articleCommentNum;

    /**
     * 文章的浏览量
     */
    @ApiModelProperty("浏览量")
    private Integer articleViewNum;

    /**
     * 文章所属的社区id
     */
    @ApiModelProperty("所属板块")
    private Integer articleCommunityId;

    /**
     * 文章发布的时间
     */
    @ApiModelProperty("发布时间")
    private LocalDateTime createTime;

    /**
     * 是否推荐该文章
     */
    @ApiModelProperty("是否推荐")
    private Integer recommend;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Integer auditState;


}
