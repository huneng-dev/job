package cn.hjf.job.model.document.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "business_scope")
public class BusinessScopeDoc {

    @Id
    private String id;

    private String businessScope;
}
