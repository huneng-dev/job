package cn.hjf.job.model.document.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "position_description")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDescriptionDoc {

    @Id
    private String id;

    private String description;
}
