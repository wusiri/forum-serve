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
 * 用户
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user")
@ApiModel("用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户登录名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
    /**
     * 用户密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 用户昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ApiModelProperty("头像")
    private String portrait;

    /**
     * 用户性别
     */
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 用户介绍
     */
    @ApiModelProperty("描述")
    private String introduce;



    /**
     * 用户城市
     */
    @ApiModelProperty("城市")
    private String city;

    /**
     * 粉丝数量
     */
    @ApiModelProperty("粉丝数量")
    private Integer fans;

    /**
     * 关注数量
     */
    @ApiModelProperty("关注数量")
    private Integer attention;

    /**
     * 获赞数量
     */
    @ApiModelProperty("获赞数量")
    private Integer good;

    /**
     * 是否被禁用
     */
    @ApiModelProperty("是否禁用")
    private Integer enabled;

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
