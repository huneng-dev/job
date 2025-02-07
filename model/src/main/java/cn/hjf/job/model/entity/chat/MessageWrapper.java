package cn.hjf.job.model.entity.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 消息包装类，用于封装不同类型的消息内容
 * 该类使用了Lombok注解来简化构造函数和数据访问器的编写
 *
 * @param <T> 泛型参数，表示可以包装任意类型的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息包装")
public class MessageWrapper<T> {
    // 消息类型，指示当前包装的消息类型
    private MessageWrapperType messageWrapperType;
    // 数据字段，存储泛型类型的数据
    private T data;
}
