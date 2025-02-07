package cn.hjf.job.chat.receiver;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.logging.core.LogUtils;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.model.entity.chat.ChatRelationship;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 聊天关系缓存更新接收器
 */
@Service
public class ChatRelationReceiver {

    @Resource
    private RedisTemplate<String, ChatRelationship> redisTemplate;

    private static final String DATABASE_NAME = "job_chat";
    private static final String TABLE_NAME = "chat_relationship";

    /**
     * 监听 RabbitMQ 队列中的消息，更新聊天关系缓存
     *
     * @param message 消息体
     */
    @RabbitListener(
            bindings = @QueueBinding(value = @Queue
                    (value = MqConst.QUEUE_CANAL_CHAT, durable = "true"),
                    exchange = @Exchange(value = MqConst.EXCHANGE_JOB_CANAL),
                    key = {MqConst.ROUTING_CANAL_CHAT}
            )
    )
    public void chatRelationCache(Message message) {
        // 1. 解析消息
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        JSONObject canalMessage = JSON.parseObject(messageBody);

        // 2. 验证数据库和表名
        String database = canalMessage.getString("database");
        String table = canalMessage.getString("table");

        if (!DATABASE_NAME.equals(database) || !TABLE_NAME.equals(table)) {
            LogUtils.error("数据库或表与期望不同：", database, table);
            return;
        }

        // 3. 获取操作类型
        String operationType = canalMessage.getString("type");

        // 4. 获取数据
        JSONArray dataArray = canalMessage.getJSONArray("data");
        if (dataArray == null || dataArray.isEmpty()) {
            LogUtils.error("数据为 null 或 空");
            return;
        }

        // 5. 逐条处理数据
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject data = dataArray.getJSONObject(i);
            processData(operationType, data);
        }
    }

    /**
     * 根据操作类型处理数据
     *
     * @param operationType 操作类型，如 INSERT、UPDATE、DELETE
     * @param data          数据内容
     */
    private void processData(String operationType, JSONObject data) {
        switch (operationType.toUpperCase()) {
            case "INSERT", "UPDATE":
                handleUpdate(data);
                break;
            case "DELETE":
                handleDelete(data);
                break;
            default:
                LogUtils.error("未知操作", operationType.toUpperCase());
        }
    }

    /**
     * 处理更新操作
     *
     * @param jsonObject 数据内容
     */
    private void handleUpdate(JSONObject jsonObject) {
        if (jsonObject.get("is_deleted").equals("1")) {
            handleDelete(jsonObject);
        }
        ChatRelationship chatRelationship = convertToChatRelationship(jsonObject);
        upDateChatRelationCacheToRs(chatRelationship);
        LogUtils.info("更新 ChatRelation 到 Redis 缓存", chatRelationship.getId());
    }


    /**
     * 处理删除操作
     *
     * @param jsonObject 数据内容
     */
    private void handleDelete(JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String redisKey = RedisConstant.CHAT_RELATION_CACHE + id;
        deleteChatRelationCacheFormRs(redisKey);
        LogUtils.info("从 Redis 删除 ChatRelation 缓存", id);
    }

    /**
     * 更新聊天关系到 redis
     *
     * @param chatRelationship 聊天关系对象
     */
    private void upDateChatRelationCacheToRs(ChatRelationship chatRelationship) {
        redisTemplate.opsForValue().set(
                RedisConstant.CHAT_RELATION_CACHE + chatRelationship.getId().toString(),
                chatRelationship,
                RedisConstant.CHAT_RELATION_CACHE_TIME,
                TimeUnit.SECONDS
        );
    }

    /**
     * 从 redis 删除聊天关系缓存
     *
     * @param redisKey redis 中的键
     */
    private void deleteChatRelationCacheFormRs(String redisKey) {
        redisTemplate.delete(redisKey);
    }

    /**
     * 将 JSON 对象转换为 ChatRelationship 对象
     *
     * @param data JSON 数据
     * @return 转换后的 ChatRelationship 对象
     */
    private ChatRelationship convertToChatRelationship(JSONObject data) {
        ChatRelationship relationship = new ChatRelationship();
        relationship.setId(data.getLong("id"));
        relationship.setRecruiterId(data.getLong("recruiter_id"));
        relationship.setCandidateId(data.getLong("candidate_id"));
        relationship.setPositionId(data.getLong("position_id"));
        relationship.setBlocked(data.getInteger("blocked"));
        relationship.setDeletedByRecruiter(data.getInteger("deleted_by_recruiter"));
        relationship.setDeletedByCandidate(data.getInteger("deleted_by_candidate"));
        relationship.setRelationshipType(data.getInteger("relationship_type"));
        relationship.setCreateTime(parseToDate(data.getString("create_time")));
        relationship.setUpdateTime(parseToDate(data.getString("update_time")));
        relationship.setIsDeleted(data.getInteger("is_deleted"));
        return relationship;
    }

    /**
     * 将日期字符串转换为 Date 对象
     *
     * @param dateTimeStr 日期时间字符串
     * @return 转换后的 Date 对象，如果转换失败则返回 null
     */
    private Date parseToDate(String dateTimeStr) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
