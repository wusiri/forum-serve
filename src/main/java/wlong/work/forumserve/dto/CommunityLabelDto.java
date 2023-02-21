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
     * 社区id
     */
    @ApiModelProperty("社区id")
    private Integer communityId;

    /**
     * 社区名称
     */
    @ApiModelProperty("社区名称")
    private String communityName;


    @ApiModelProperty("标签id")
    private Integer labelId;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String labelName;
}
