package cn.hjf.job.company.repository;

import cn.hjf.job.model.document.company.CompanyDescriptionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * 公司描述操作接口
 *
 * @author hjf
 * @version 1.0
 * @description
 */

@Component
public interface CompanyDescriptionRepository extends MongoRepository<CompanyDescriptionDoc, String> {


}
