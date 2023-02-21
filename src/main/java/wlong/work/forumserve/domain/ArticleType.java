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
 * 文章类型
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_article_type")
@ApiModel("文章类型")
public class ArticleType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型id")
    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    /**
     * 文章类型名称
     */
    @ApiModelProperty("类型名称")
    private String typeName;





}
