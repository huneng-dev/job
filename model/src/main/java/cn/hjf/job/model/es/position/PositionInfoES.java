package cn.hjf.job.model.es.position;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Date;

@Document(indexName = "position")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionInfoES {
    /**
     * 职位 id
     */
    @Id
    private Long id;

    /**
     * 公司 id
     */
        @Field(type = FieldType.Long)
    private Long companyId;

    /**
     * 职位名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String positionName;

    /**
     * 职位描述
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String positionDescription;

    /**
     * 公司名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String companyName;

    /**
     * 职位类型 id
     */
    @Field(type = FieldType.Long)
    private Long positionTypeId;

    /**
     * 求职类型
     */
    @Field(type = FieldType.Integer)
    private Integer positionType;

    /**
     * 经验要求
     */
    @Field(type = FieldType.Integer)
    private Integer experienceRequirement;

    /**
     * 学历要求
     */
    @Field(type = FieldType.Integer)
    private Integer educationRequirement;

    /**
     * 最低薪资
     */
    @Field(type = FieldType.Integer)
    private Integer minSalary;

    /**
     * 最高薪资
     */
    @Field(type = FieldType.Integer)
    private Integer maxSalary;

    /**
     * 地区 | 县
     */
    @Field(type = FieldType.Keyword)
    private String district;

    /**
     * GEO 地理位置
     */
    @GeoPointField
    private GeoPoint location;

    /**
     * 公司规模 id
     */
    @Field(type = FieldType.Integer)
    private Integer companySizeId;

    /**
     * 查看数量
     */
    @Field(type = FieldType.Long)
    private Long watchCount;

    /**
     * 交流数量
     */
    @Field(type = FieldType.Long)
    private Long communicationCount;

    /**
     * 收藏数量
     */
    @Field(type = FieldType.Long)
    private Long favoriteCount;

    /**
     * 职位更新时间
     */
    @Field(type = FieldType.Date)
    private Date updateTime;
}
