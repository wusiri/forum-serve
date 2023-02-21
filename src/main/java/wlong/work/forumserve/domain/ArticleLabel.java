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
 * 文章标签
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_article_label")
@ApiModel("文章标签")
public class ArticleLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章标签的id
     */
    @ApiModelProperty("标签id")
    @TableId(value = "label_id", type = IdType.AUTO)
    private Integer labelId;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String labelName;

    /**
     * 标签是否禁用
     */
    @ApiModelProperty("标签状态")
    private Integer enabled;


}
