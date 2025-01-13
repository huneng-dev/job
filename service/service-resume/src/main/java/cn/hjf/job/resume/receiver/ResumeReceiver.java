package cn.hjf.job.resume.receiver;

import cn.hjf.job.common.es.entity.BaseEsInfo;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.entity.resume.ResumeInfo;
import cn.hjf.job.model.es.resume.ResumeES;
import cn.hjf.job.model.vo.resume.EducationBackgroundVo;
import cn.hjf.job.model.vo.resume.JobExpectationVo;
import cn.hjf.job.model.vo.resume.ResumeVo;
import cn.hjf.job.model.vo.user.UserInfoAllVo;
import cn.hjf.job.resume.config.KeyProperties;
import cn.hjf.job.resume.service.EducationBackgroundService;
import cn.hjf.job.resume.service.JobExpectationService;
import cn.hjf.job.resume.service.ResumeInfoService;
import cn.hjf.job.user.client.UserInfoFeignClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class ResumeReceiver {

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Resource
    private ResumeInfoService resumeInfoService;

    @Resource
    private JobExpectationService jobExpectationService;

    @Resource
    private EducationBackgroundService educationBackgroundService;

    @Resource
    private KeyProperties keyProperties;


    /**
     * 检查受否有索引
     * 没有就创建
     *
     * @param event
     */
    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        try {
            IndexOperations indexOperations = elasticsearchOperations.indexOps(ResumeES.class);
            boolean exists = indexOperations.exists();
            if (exists) {
                System.out.println("索引存在");
            } else {
                // 创建索引
                indexOperations.create();
                Document mapping = indexOperations.createMapping();
                indexOperations.putMapping(mapping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 用于记录总耗时和执行次数
//    private static final AtomicLong totalTime = new AtomicLong(0);
//    private static final AtomicLong count = new AtomicLong(0);

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_ES_RESUME, durable = "true"),
                    exchange = @Exchange(value = MqConst.EXCHANGE_ES),
                    key = {MqConst.ROUTING_ES_RESUME})
    )
    public void operationResumeEs(BaseEsInfo<ResumeInfo> resumeInfoBaseEsInfo, Message message, Channel channel) {
        if (Objects.equals(resumeInfoBaseEsInfo.getOp(), "u")) {
//            Long startTime = System.currentTimeMillis();
            updateResumeEs(resumeInfoBaseEsInfo.getData());
//            Long endTime = System.currentTimeMillis();
//            Long elapsedTime = endTime - startTime;

            // 累加总耗时和执行次数
//            totalTime.addAndGet(elapsedTime);
//            count.incrementAndGet();

            // 打印每次的耗时
//            System.err.println("updateResumeEs 耗时: " + elapsedTime + " 毫秒");
//            printAverageTime();
        }
    }

//
//    public static void printAverageTime() {
//        long total = totalTime.get();
//        long cnt = count.get();
//        if (cnt > 0) {
//            System.err.println("平均耗时: " + (total / cnt) + " 毫秒");
//        } else {
//            System.err.println("没有执行任何操作，无法计算平均耗时。");
//        }
//    }


//    public void updateResumeEs(ResumeInfo resumeInfo) {
//        try {
//            // 获取简历基本信息
//            ResumeVo resumeVo = resumeInfoService.getResumeVo(resumeInfo.getId());
//            // 获取工作期望信息
//            JobExpectationVo jobExpectationVo = jobExpectationService.getJobExpectationVo(resumeInfo.getId());
//            // 获取教育背景信息
//            EducationBackgroundVo educationBackgroundVo = educationBackgroundService.getEducationBackgroundVo(resumeInfo.getId());
//            // 获取用户信息
//            UserInfoAllVo userInfo = getUserInfo(resumeInfo.getCandidateId());
//
//            ResumeES resumeES = new ResumeES();
//            BeanUtils.copyProperties(resumeVo, resumeES);
//            BeanUtils.copyProperties(jobExpectationVo, resumeES);
//            BeanUtils.copyProperties(educationBackgroundVo, resumeES);
//            resumeES.setId(resumeVo.getId());
//            assert userInfo != null;
//            resumeES.setSurname(getSurname(userInfo.getName()));
//
//            // 保存到 ES
//            elasticsearchOperations.save(resumeES);
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//    }


    public void updateResumeEs(ResumeInfo resumeInfo) {
        // 获取简历基本信息（异步）
        CompletableFuture<ResumeVo> resumeVoAsync = resumeInfoService.getResumeVoAsync(resumeInfo.getId());

        // 获取工作期望信息（异步）
        CompletableFuture<JobExpectationVo> jobExpectationVoAsync = jobExpectationService.getJobExpectationVoAsync(resumeInfo.getId());

        // 获取教育背景信息（异步）
        CompletableFuture<EducationBackgroundVo> educationBackgroundVoAsync = educationBackgroundService.getEducationBackgroundVoAsync(resumeInfo.getId());

        // 获取用户信息（异步）
        CompletableFuture<UserInfoAllVo> userInfoAsync = getUserInfoAsync(resumeInfo.getCandidateId());

        // 使用 CompletableFuture.allOf 来等待所有异步任务完成，并合并结果
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                resumeVoAsync, jobExpectationVoAsync, educationBackgroundVoAsync, userInfoAsync
        );

        allOf.thenRun(() -> {
            try {
                // 等待所有异步任务完成，并获取结果
                ResumeVo resumeVo = resumeVoAsync.get();
                JobExpectationVo jobExpectationVo = jobExpectationVoAsync.get();
                EducationBackgroundVo educationBackgroundVo = educationBackgroundVoAsync.get();
                UserInfoAllVo userInfo = userInfoAsync.get();

                // 拼接并更新 ResumeES
                ResumeES resumeES = new ResumeES();
                BeanUtils.copyProperties(resumeVo, resumeES);
                BeanUtils.copyProperties(jobExpectationVo, resumeES);
                BeanUtils.copyProperties(educationBackgroundVo, resumeES);
                resumeES.setId(resumeVo.getId());

                assert userInfo != null;
                resumeES.setSurname(getSurname(userInfo.getName()));

                // 保存到 Elasticsearch
                deleteResumeEs(resumeInfo);
                elasticsearchOperations.save(resumeES);
            } catch (Exception e) {
                // 捕获任何异常，并将其重新抛出
                throw new RuntimeException("Failed to update Resume in Elasticsearch", e);
            }
        }).exceptionally(ex -> {
            // 异常处理，可以根据需要记录日志或者重新抛出异常
            throw new RuntimeException("Error occurred while processing async tasks", ex);
        }).join();
    }


    public void deleteResumeEs(ResumeInfo resumeInfo) {
        // 创建查询条件，匹配 candidateId
        TermQuery query = QueryBuilders.term().field("candidateId").value(resumeInfo.getCandidateId()).build();
        NativeQuery nativeQuery = NativeQuery.builder().withQuery(q -> q.term(query)).build();
        elasticsearchOperations.delete(nativeQuery, ResumeES.class);
    }


    private UserInfoAllVo getUserInfo(Long userId) {
        Result<UserInfoAllVo> result =
                userInfoFeignClient.getUserInfoAllVo(userId, keyProperties.getKey());
        if (Objects.equals(result.getCode(), 200)) {
            return result.getData();
        } else {
            throw new RuntimeException();
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<UserInfoAllVo> getUserInfoAsync(Long userId) {
        return CompletableFuture.supplyAsync(() -> getUserInfo(userId));
    }

    @Valid
    public String getSurname(@NotNull @NotEmpty String fullName) {
        // 获取姓氏，假设中文姓名，第一个字符即为姓氏
        return String.valueOf(fullName.charAt(0));
    }
}
