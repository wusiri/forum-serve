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
 * 管理员
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_admin")
@ApiModel("管理员")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    @ApiModelProperty("管理员id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    

    /**
     * 管理员名称
     */
    @ApiModelProperty("管理员名称")
    private String username;




    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 管理员密码
     */
    @ApiModelProperty("管理员密码")
    private String password;

    /**
     * 管理员头像
     */
    @ApiModelProperty("管理员头像")
    private String portrait;

    /**
     * 是否禁用
     */
    @ApiModelProperty("是否禁用")
    private Integer enabled;

    /**
     * 是否是超级管理员
     */
    @ApiModelProperty("是否是超级管理员")
    private Integer isSuper;

    /**
     * 添加时间
     */
    @ApiModelProperty("注册时间")
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private LocalDateTime loginTime;


}
