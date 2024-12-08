package cn.hjf.job.company.repository;

import cn.hjf.job.model.document.company.BusinessScopeDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface BusinessScopeRepository extends MongoRepository<BusinessScopeDoc, String> {
}
