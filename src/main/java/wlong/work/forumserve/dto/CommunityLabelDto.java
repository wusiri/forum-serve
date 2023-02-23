package wlong.work.forumserve.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel(value = "CommunityLabel对象",description = "")
public class CommunityLabelDto {


    /**
     * 标签id
     */
    @ApiModelProperty("id")
    private Integer tagId;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;

    /**
     * 标识符
     */
    @ApiModelProperty("标签名称")
    private Integer tagState;





}
