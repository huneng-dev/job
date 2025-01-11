package cn.hjf.job.resume.repository;

import cn.hjf.job.model.document.resume.ProjectDescriptionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProjectDescriptionRepository extends MongoRepository<ProjectDescriptionDoc, String> {
}
