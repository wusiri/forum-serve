package wlong.work.forumserve.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 社区用户
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_community_user")
@ApiModel("社区用户")
public class CommunityUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 社区的id
     */
    @ApiModelProperty("社区id")
    private Integer communityId;

    /**
     * 社区用户的id
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 社区用户的关注状态
     */
    @ApiModelProperty("用户状态")
    private Integer state;


}
