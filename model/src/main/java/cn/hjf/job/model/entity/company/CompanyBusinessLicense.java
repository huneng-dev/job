package cn.hjf.job.model.entity.company;

import cn.hjf.job.model.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
                                                                                                                                                                                                                                                                                                                                                                                    /**
 * 
 * @author hjf
 * @date 2024-10-23
 */
@Data
@Schema(description = "CompanyBusinessLicense")
@TableName("company_business_license")
public class CompanyBusinessLicense extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @Schema(description = "公司id")
    @TableField("company_business_license")
    private Long companyId;

    @Schema(description = "公司名称")
    @TableField("company_business_license")
    private String name;

    @Schema(description = "统一社会信用代码")
    @TableField("company_business_license")
    private String licenseNumber;

    @Schema(description = "注册资本")
    @TableField("company_business_license")
    private String capital;

    @Schema(description = "法人姓名")
    @TableField("company_business_license")
    private String legalPerson;

    @Schema(description = "地址")
    @TableField("company_business_license")
    private String address;

    @Schema(description = "经营范围")
    @TableField("company_business_license")
    private String businessScope;

    @Schema(description = "主体类型  示例值：有限责任公司")
    @TableField("company_business_license")
    private String type;

    @Schema(description = "营业期限")
    @TableField("company_business_license")
    private String period;

    @Schema(description = "成立日期")
    @TableField("company_business_license")
    private String establishmentDate;

    @Schema(description = "登记日期")
    @TableField("company_business_license")
    private String registrationDate;

    @Schema(description = "注册机构")
    @TableField("company_business_license")
    private String registrationAuthority;

    @Schema(description = "是否是电子营业执照")
    @TableField("company_business_license")
    private Integer isElectronic;

    @Schema(description = "0 表示否，1 表示是")
    @TableField("company_business_license")
    private Integer isCopy;

    @Schema(description = "是否有印章，0 表示没有，1 表示有")
    @TableField("company_business_license")
    private Integer hasSeal;

    @Schema(description = "法人id")
    @TableField("company_business_license")
    private Long legalPersonId;

    @Schema(description = "营业执照照片")
    @TableField("company_business_license")
    private String businessLicenseUrl;

    @Schema(description = "法人认证状态，0 代表未认证，1 代表认证中，2 代表认证通过，3 代表认证失败等。")
    @TableField("company_business_license")
    private Integer legalPersonAuthStatus;
    }
