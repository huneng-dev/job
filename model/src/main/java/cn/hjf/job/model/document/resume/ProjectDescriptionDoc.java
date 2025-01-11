package cn.hjf.job.model.document.resume;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project_description")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDescriptionDoc {
    @Id
    private String id;

    private String description;
}
