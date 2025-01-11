package cn.hjf.job.position.repository;

import cn.hjf.job.model.document.position.PositionDescriptionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;


@Component
public interface PositionDescriptionRepository extends MongoRepository<PositionDescriptionDoc, String> {

}
