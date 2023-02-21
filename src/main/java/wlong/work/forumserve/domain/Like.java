package wlong.work.forumserve.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 点赞
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_like")
@ApiModel("点赞")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点赞的id
     */
    @ApiModelProperty("点赞id")
    @TableId(value = "like_id", type = IdType.AUTO)
    private Integer likeId;


    /**
     * 点赞的文章
     */
    @ApiModelProperty("点赞的文章")
    private Integer articleId;

    /**
     * 点赞的评论
     */
    @ApiModelProperty("点赞的评论")
    private Integer commentId;

    /**
     * 点赞的回复
     */
    @ApiModelProperty("点赞的回复")
    private Integer replyId;

    /**
     * 点赞的社区
     */
    @ApiModelProperty("点赞的社区")
    private Integer communityId;


    /**
     * 文章点赞状态
     */
    @ApiModelProperty("文章点赞状态")
    private Integer articleLikeNum;
    /**
     * 评论点赞状态
     */
    @ApiModelProperty("评论点赞状态")
    private Integer commentLikeNum;

    /**
     * 回复点赞状态
     */
    @ApiModelProperty("回复点赞状态")
    private Integer replyLikeNum;

    /**
     * 社区点赞状态
     */
    @ApiModelProperty("社区点赞状态")
    private Integer communityLikeNum;

    /**
     * 点赞用户
     */
    @ApiModelProperty("点赞用户")
    private Integer userId;

}
