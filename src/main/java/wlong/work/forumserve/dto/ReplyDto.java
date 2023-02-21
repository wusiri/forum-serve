package com.jingchao.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Reply对象",description = "")
public class ReplyVO {

    @ApiModelProperty("回复人的头像")
    private String portrait;

    @ApiModelProperty("回复人的昵称")
    private String nickname;

    @ApiModelProperty("回复的时间")
    private String replyTime;

    @ApiModelProperty("回复对象的ID")
    private Integer replyToUserId;

    @ApiModelProperty("回复对象的昵称")
    private String replyToNickname;

    @ApiModelProperty("回复的内容")
    private String replyContent;

    @ApiModelProperty("是否显示输入框")
    private boolean inputShow;
}
