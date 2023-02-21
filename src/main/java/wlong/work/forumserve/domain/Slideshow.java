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
 * 轮播图
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_slideshow")
@ApiModel("轮播图")
public class Slideshow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 轮播图的id
     */
    @ApiModelProperty("轮播图id")
    @TableId(value = "slideshow_id", type = IdType.AUTO)
    private Integer slideshowId;

    /**
     * 轮播图的地址
     */
    @ApiModelProperty("轮播图地址")
    private String imageUrl;


    /**
     * 是否被禁用
     */
    @ApiModelProperty("是否禁用")
    private Integer enabled;


}
