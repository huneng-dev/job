package cn.hjf.job.model.document.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company_description")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDescriptionDoc {

    @Id
    private String id;

    private String description;
}
