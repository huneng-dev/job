package cn.hjf.job.company.service;

import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CompanyIndustryServiceTest {

    @Resource
    private CompanyIndustryService companyIndustryService;

    @Resource
    private RabbitService rabbitService;

    @Test
    public void selectAllIndustries() {
    }

    @Test
    public void validateCompanyInfoAndBusinessLicense() {
        rabbitService.sendMessage(MqConst.EXCHANGE_COMPANY, MqConst.ROUTING_VALIDATE_COMPANY_BUSINESS_LICENSE, "kjhakjsjfdklajslk");
    }

}
