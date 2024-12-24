package cn.hjf.job.position.receiver;

import cn.hjf.job.common.es.entity.BaseEsInfo;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.client.CompanyAddressFeignClient;
import cn.hjf.job.company.client.CompanyInfoFeignClient;
import cn.hjf.job.model.document.position.PositionDescriptionDoc;
import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.es.position.PositionInfoES;
import cn.hjf.job.model.vo.company.AddressInfoVo;
import cn.hjf.job.model.vo.company.CompanyInfoEsVo;
import cn.hjf.job.position.mapper.PositionInfoMapper;
import cn.hjf.job.position.repository.PositionDescriptionRepository;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PositionReceiver {

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @Resource
    private PositionInfoMapper positionInfoMapper;

    @Resource
    private PositionDescriptionRepository positionDescriptionRepository;

    @Resource
    private CompanyAddressFeignClient companyAddressFeignClient;

    @Resource
    private CompanyInfoFeignClient companyInfoFeignClient;


    /**
     * 检查受否有索引
     * 没有就创建
     *
     * @param event
     */
    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        try {
            IndexOperations indexOperations = elasticsearchOperations.indexOps(PositionInfoES.class);
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


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_ES_POSITION, durable = "true"), exchange = @Exchange(value = MqConst.EXCHANGE_ES), key = {MqConst.ROUTING_ES_POSITION}))
    public void testPosition(BaseEsInfo<PositionInfo> positionInfoBaseEsInfo, Message message, Channel channel) {
        // 1. 判断操作类型
        if ("u".equals(positionInfoBaseEsInfo.getOp())) {
            updatePositionES(positionInfoBaseEsInfo.getData());
        }
        if ("d".equals(positionInfoBaseEsInfo.getOp())) {
            deletePositionES(positionInfoBaseEsInfo.getData());
        }
    }


    /**
     * 更新 positionInfoES to es
     * 如果不存在就创建
     *
     * @param positionInfo 职位信息
     */
    public void updatePositionES(PositionInfo positionInfo) {
        PositionInfoES positionInfoES = getPositionInfoES(positionInfo);
        elasticsearchOperations.save(positionInfoES);
    }

    /**
     * 删除 positionInfoES
     *
     * @param positionInfo 职位信息
     */
    public void deletePositionES(PositionInfo positionInfo) {
        elasticsearchOperations.delete(positionInfo.getId().toString(), PositionInfoES.class);
    }


    private PositionInfoES getPositionInfoES(PositionInfo positionInfo) {
        // 获取 职位详情
        PositionInfo positionInfoData = positionInfoMapper.selectById(positionInfo.getId());
        // 获取 职位描述详情
        Optional<PositionDescriptionDoc> optional = positionDescriptionRepository.findById(positionInfoData.getPositionDescription());
        optional.ifPresent(positionDescriptionDoc -> positionInfoData.setPositionDescription(positionDescriptionDoc.getDescription()));
        // 获取 地址信息
        Result<AddressInfoVo> addressInfoVoResult = companyAddressFeignClient.getAddressById(positionInfoData.getAddressId());
        AddressInfoVo addressInfoVoData = addressInfoVoResult.getData();
        // 获取 公司信息
        Result<CompanyInfoEsVo> companyInfoEsVoResult = companyInfoFeignClient.getCompanyInfoEsById(positionInfoData.getCompanyId());
        CompanyInfoEsVo companyInfoEsVo = companyInfoEsVoResult.getData();

        // 创建 PositionInfoES
        PositionInfoES positionInfoES = new PositionInfoES();
        BeanUtils.copyProperties(positionInfoData, positionInfoES);
        BeanUtils.copyProperties(companyInfoEsVo, positionInfoES);
        BeanUtils.copyProperties(addressInfoVoData, positionInfoES);

        positionInfoES.setLocation(new GeoPoint(addressInfoVoData.getLatitude().doubleValue(), addressInfoVoData.getLongitude().doubleValue()));
        positionInfoES.setId(positionInfoData.getId());
        return positionInfoES;
    }
}
