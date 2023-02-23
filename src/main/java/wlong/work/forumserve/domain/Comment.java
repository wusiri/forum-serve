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
 * 评论
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_comment")
@ApiModel("评论")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论的id
     */
    @ApiModelProperty("评论id")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    /**
     * 评论的内容
     */
    @ApiModelProperty("评论内容")
    private String commentContent;

    /**
     * 评论的文章id
     */
    @ApiModelProperty("评论文章id")
    private Integer commentArticleId;



    /**
     * 评论的用户id
     */
    @ApiModelProperty("评论用户id")
    private Integer commentUserId;

    /**
     * 评论的点赞量
     */
    @ApiModelProperty("评论的点赞量")
    private Integer commentGood;

    /**
     * 评论时间
     */
    @ApiModelProperty("评论时间")
    private LocalDateTime commentTime;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Integer auditStatus;



}
