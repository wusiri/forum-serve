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
 * 回复
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_reply")
@ApiModel("回复")
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复的id
     */
    @ApiModelProperty("回复id")
    @TableId(value = "reply_id", type = IdType.AUTO)
    private Integer replyId;

    /**
     * 回复的具体内容
     */
    @ApiModelProperty("回复的具体内容")
    private String replyContent;

    /**
     * 回复的对象的Id
     */
    @ApiModelProperty("回复的对象Id")
    private Integer replyToUserId;

    /**
     * 回复评论的ID
     */
    @ApiModelProperty("回复的评论id")
    private Integer commentId;

    /**
     * 回复评论的ID
     */
    @ApiModelProperty("回复的点赞量")
    private Integer replyGood;

    /**
     * 回复的用户id
     */
    @ApiModelProperty("回复的用户id")
    private Integer replyUserId;

    /**
     * 回复时间
     */
    @ApiModelProperty("回复时间")
    private LocalDateTime replyTime;


}
