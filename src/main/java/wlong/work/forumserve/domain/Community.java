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
 * 社区
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_community")
@ApiModel("社区")
public class Community implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 社区id
     */
    @ApiModelProperty("社区id")
    @TableId(value = "community_id", type = IdType.AUTO)
    private Integer communityId;

    /**
     * 社区名称
     */
    @ApiModelProperty("社区名称")
    private String communityName;

    /**
     * 社区介绍
     */
    @ApiModelProperty("社区介绍")
    private String communityIntroduce;

    /**
     * 社区点赞数量
     */
    @ApiModelProperty("社区点赞数量")
    private Integer goodNum;

    /**
     * 社区的照片
     */
    @ApiModelProperty("社区封面")
    private String communityImage;

    /**
     * 社区用户数量
     */
    @ApiModelProperty("社区用户数量")
    private Integer communityUserNum;

    /**
     * 创建改社区的用户id
     */
    @ApiModelProperty("创建者id")
    private Integer createUserId;

    /**
     * 社区的创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
