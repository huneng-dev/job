package cn.hjf.job.resume.repository;

import cn.hjf.job.model.document.resume.WorkDescriptionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface WorkDescriptionRepository extends MongoRepository<WorkDescriptionDoc, String> {
}
