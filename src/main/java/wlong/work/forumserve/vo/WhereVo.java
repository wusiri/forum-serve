package wlong.work.forumserve.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Where对象",description = "")
public class WhereVo {

    @ApiModelProperty("地点")
    private String name;

    @ApiModelProperty("人数")
    private Integer value;
}
