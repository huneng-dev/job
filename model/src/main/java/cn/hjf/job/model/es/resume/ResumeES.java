package cn.hjf.job.model.es.resume;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "resume")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeES {

    /**
     * 简历 id
     */
    @Id
    private Long id;

    /**
     * 用户 id
     */
    @Field(type = FieldType.Long)
    private Long candidateId;

    /**
     * 用户姓氏
     */
    @Field(type = FieldType.Keyword, index = false)
    private String surname;

    /**
     * 求职状态
     */
    @Field(type = FieldType.Long)
    private String jobStatus;

    /**
     * 个人优势
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String personalAdvantages;

    /**
     * 专业技能
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String professionalSkills;

    /**
     * 期望职位 id
     */
    @Field(type = FieldType.Long)
    private Long expectedPositionId;

    /**
     * 期望行业
     */
    @Field(type = FieldType.Long)
    private Long industryId;

    /**
     * 期望城市
     */
    @Field(type = FieldType.Keyword)
    private String workCity;

    /**
     * 最低薪资
     */
    @Field(type = FieldType.Integer)
    private Integer salaryMin;

    /**
     * 最高薪资
     */
    @Field(type = FieldType.Integer, nullValue = "-1")
    private Integer salaryMax;

    /**
     * 是否面议
     */
    @Field(type = FieldType.Integer)
    private Integer isNegotiable;

    /**
     * 工作类型
     */
    @Field(type = FieldType.Integer)
    private Integer jobType;

    /**
     * 学校名称
     */
    @Field(type = FieldType.Keyword, index = false)
    private String schoolName;

    /**
     * 专业
     */
    @Field(type = FieldType.Keyword, index = false)
    private String major;

    /**
     * 教育水平
     */
    @Field(type = FieldType.Integer)
    private Integer educationLevel;

    /**
     * 开始年
     */
    @Field(type = FieldType.Integer)
    private Integer startYear;

    /**
     * 结束年
     */
    @Field(type = FieldType.Integer, nullValue = "-1")
    private Integer endYear;
}
