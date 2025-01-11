package cn.hjf.job.model.document.resume;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "work_description")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDescriptionDoc {
    @Id
    private String id;

    private String description;
}
