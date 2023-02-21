package wlong.work.forumserve.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel(value = "CommentReply对象",description = "")
public class CommentReplyVO {

    @ApiModelProperty("评论ID")
    private Integer commentId;

    @ApiModelProperty("评论的用户的ID")
    private Integer userId;

    @ApiModelProperty("评论人的头像")
    private String portrait;

    @ApiModelProperty("评论人的昵称")
    private String nickname;

    @ApiModelProperty("评论的时间")
    private String commentTime;

    @ApiModelProperty("评论的内容")
    private String commentContent;

    @ApiModelProperty("是否显示输入框")
    private boolean inputShow;

    @ApiModelProperty("回复")
    private List<ReplyVO> reply;

}
