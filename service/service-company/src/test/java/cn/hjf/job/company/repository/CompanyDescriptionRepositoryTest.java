package cn.hjf.job.company.repository;

import cn.hjf.job.model.document.company.CompanyDescription;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class CompanyDescriptionRepositoryTest {

    @Resource
    private CompanyDescriptionRepository companyDescriptionRepository;

    @Test
    public void saveCompanyDesc() {

        CompanyDescription companyDescription = new CompanyDescription();
        companyDescription.setDescription("公司描述数据");
        CompanyDescription save = companyDescriptionRepository.save(companyDescription);
        System.err.println(save);
    }

    @Test
    public void findCompanyDesc() {
        Optional<CompanyDescription> byId = companyDescriptionRepository.findById("67558b708e15d9241b6dec61");
        CompanyDescription companyDescription = byId.get();
        System.out.println(companyDescription);
    }
}
