package wlong.work.forumserve.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author wl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_message")
@ApiModel("消息")
public class Message {

    private static final long serialVersionUID = 1L;

    /**
     * 消息的id
     */
    @ApiModelProperty("消息id")
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    /**
     * 具体内容
     */
    @ApiModelProperty("具体内容")
    private String messageInfo;

    /**
     * 具体发送者
     */
    @ApiModelProperty("具体发送者")
    private Integer messageFromUser;

    /**
     * 具体对象
     */
    @ApiModelProperty("具体对象")
    private Integer messageUser;

    /**
     * 具体类型
     */
    @ApiModelProperty("具体类型")
    private Integer messageType;

    /**
     * 具体状态
     */
    @ApiModelProperty("具体状态")
    private Integer messageState;

    /**
     * 具体时间
     */
    @ApiModelProperty("具体时间")
    private LocalDateTime messageTime;


}
