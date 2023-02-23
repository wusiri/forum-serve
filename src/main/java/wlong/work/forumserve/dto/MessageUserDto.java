package wlong.work.forumserve.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value = "消息对象",description = "")
public class MessageUserDto {


    @ApiModelProperty("发送者id")
    private Integer userId;

    @ApiModelProperty("消息id")
    private Integer messageId;

    @ApiModelProperty("发送者昵称")
    private String nickname;

    @ApiModelProperty("发送内容")
    private String messageInfo;

    @ApiModelProperty("阅读状态")
    private Integer state;

    @ApiModelProperty("消息类型")
    private Integer type;

    @ApiModelProperty("消息产生时间")
    private LocalDateTime messageTime;

}
