package cn.hjf.job.position.service.impl;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.es.entity.BaseEsInfo;
import cn.hjf.job.common.rabbit.constant.MqConst;
import cn.hjf.job.common.rabbit.service.RabbitService;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.client.CompanyAddressFeignClient;
import cn.hjf.job.company.client.CompanyEmployeeFeignClient;
import cn.hjf.job.company.client.CompanyInfoFeignClient;
import cn.hjf.job.model.document.position.PositionDescriptionDoc;
import cn.hjf.job.model.entity.position.PositionInfo;
import cn.hjf.job.model.es.position.PositionInfoES;
import cn.hjf.job.model.form.position.PositionInfoForm;
import cn.hjf.job.model.request.position.CandidatePositionPageParam;
import cn.hjf.job.model.vo.base.PagePositionEsVo;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.company.AddressInfoVo;
import cn.hjf.job.model.vo.company.CompanyEmployeeVo;
import cn.hjf.job.model.vo.company.CompanyIdAndIsAdmin;
import cn.hjf.job.model.vo.company.CompanyInfoVo;
import cn.hjf.job.model.vo.position.CandidateBasePositionInfoVo;
import cn.hjf.job.model.vo.position.CandidatePositionInfoVo;
import cn.hjf.job.model.vo.position.RecruiterBasePositionInfoVo;
import cn.hjf.job.model.vo.position.RecruiterPositionInfoVo;
import cn.hjf.job.position.config.KeyProperties;
import cn.hjf.job.position.mapper.PositionInfoMapper;
import cn.hjf.job.position.repository.PositionDescriptionRepository;
import cn.hjf.job.position.service.PositionInfoService;
import cn.hjf.job.position.service.PositionTypeService;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class PositionInfoServiceImpl extends ServiceImpl<PositionInfoMapper, PositionInfo> implements PositionInfoService {

    @Resource
    private PositionInfoMapper positionInfoMapper;

    @Resource
    private CompanyEmployeeFeignClient companyEmployeeFeignClient;

    @Resource
    private CompanyAddressFeignClient companyAddressFeignClient;

    @Resource(name = "positionTypeServiceImpl")
    private PositionTypeService positionTypeService;

    @Resource
    private PositionDescriptionRepository positionDescriptionRepository;

    @Resource
    private RabbitService rabbitService;

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @Resource
    private RedisTemplate<String, PositionInfo> redisTemplate;

    @Resource
    private CompanyInfoFeignClient companyInfoFeignClient;

    @Resource
    private KeyProperties keyProperties;

    @Override
    public boolean create(PositionInfoForm positionInfoForm, Long userId) {
        // 获取当前用户的公司的 id
        Result<Long> result = companyEmployeeFeignClient.findCompanyIdByUserId();
        if (!result.getCode().equals(200)) {
            return false;
        }
        Long companyId = result.getData();

        PositionDescriptionDoc positionDescriptionDoc = new PositionDescriptionDoc();
        positionDescriptionDoc.setDescription(positionInfoForm.getPositionDescription());
        PositionDescriptionDoc descriptionDoc = positionDescriptionRepository.save(positionDescriptionDoc);

        // 准备数据
        PositionInfo positionInfo = new PositionInfo();
        BeanUtils.copyProperties(positionInfoForm, positionInfo);
        positionInfo.setCompanyId(companyId);
        positionInfo.setCreatorId(userId);
        positionInfo.setPositionDescription(descriptionDoc.getId());

        /*
        设置状态为 待开放 3
        一般情况下将 职位名称，职位描述，上传到 腾讯云数据万象 进行审核。
        在进行人工审核后，设置为 待开放
        此处省略以上流程，直接设置为 3
         */

        positionInfo.setStatus(3);
        int insert = positionInfoMapper.insert(positionInfo);

        return insert == 1;
    }

    @Override
    public PageVo<RecruiterBasePositionInfoVo> findRecruiterBasePositionInfoByUserId(Page<PositionInfo> positionInfoPage, String positionName, Integer status, Long userId) {
        // 查询当前用户是否是管理员 管理员查询全部职位,非管理员查询的自己创建的
        Result<CompanyIdAndIsAdmin> companyIdAndIsAdminByUserId = companyEmployeeFeignClient.findCompanyIdAndIsAdminByUserId();
        CompanyIdAndIsAdmin companyIdAndIsAdmin = companyIdAndIsAdminByUserId.getData();
        Long companyId = companyIdAndIsAdmin.getCompanyId();
        Integer isAdmin = companyIdAndIsAdmin.getIsAdmin();

        LambdaQueryWrapper<PositionInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(PositionInfo::getId, PositionInfo::getPositionName, PositionInfo::getEducationRequirement, PositionInfo::getExperienceRequirement, PositionInfo::getMinSalary, PositionInfo::getMaxSalary, PositionInfo::getStatus, PositionInfo::getWatchCount, PositionInfo::getCommunicationCount, PositionInfo::getFavoriteCount).eq(PositionInfo::getCompanyId, companyId).orderByDesc(PositionInfo::getId);


        // 设置状态 0 标识没有状态查询全部
        if (!status.equals(0)) {
            queryWrapper.eq(PositionInfo::getStatus, status);
        }

        // 如果不是管理员就设置 创建人 id
        if (isAdmin.equals(0)) {
            queryWrapper.eq(PositionInfo::getCreatorId, userId);
        }

        // 设置模糊查询
        if (!positionName.isEmpty()) {
            queryWrapper.like(PositionInfo::getPositionName, positionName);
        }


        // 分页查询
        Page<PositionInfo> selectPage = positionInfoMapper.selectPage(positionInfoPage, queryWrapper);

        List<PositionInfo> records = selectPage.getRecords();

        List<RecruiterBasePositionInfoVo> recruiterBasePositionInfoVos = records.stream().map(positionInfo -> new RecruiterBasePositionInfoVo(positionInfo.getId(), positionInfo.getPositionName(), positionInfo.getEducationRequirement(), positionInfo.getExperienceRequirement(), positionInfo.getMinSalary(), positionInfo.getMaxSalary(), positionInfo.getStatus(), positionInfo.getWatchCount(), positionInfo.getCommunicationCount(), positionInfo.getFavoriteCount())).toList();

        PageVo<RecruiterBasePositionInfoVo> recruiterBasePositionInfoVoPageVo = new PageVo<>();
        recruiterBasePositionInfoVoPageVo.setRecords(recruiterBasePositionInfoVos);
        recruiterBasePositionInfoVoPageVo.setLimit(selectPage.getSize());
        recruiterBasePositionInfoVoPageVo.setPages(selectPage.getPages());
        recruiterBasePositionInfoVoPageVo.setPage(selectPage.getCurrent());
        recruiterBasePositionInfoVoPageVo.setTotal(selectPage.getTotal());
        return recruiterBasePositionInfoVoPageVo;
    }

    @Override
    public RecruiterPositionInfoVo getRecruiterPositionInfoVoById(Long positionId, Long userId) {
        // 获取公司id
        Result<Long> result = companyEmployeeFeignClient.findCompanyIdByUserId();

        Long companyId = result.getData();

        LambdaQueryWrapper<PositionInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PositionInfo::getId, positionId).eq(PositionInfo::getCompanyId, companyId);

        // 职位信息
        PositionInfo positionInfo = positionInfoMapper.selectOne(queryWrapper);
        // 结果职位信息
        RecruiterPositionInfoVo recruiterPositionInfoVo = new RecruiterPositionInfoVo();
        recruiterPositionInfoVo.setId(positionId);

        // 获取创建人与负责人信息
        Result<CompanyEmployeeVo> creatorResult = companyEmployeeFeignClient.findCompanyEmployeeById(positionInfo.getCreatorId());
        Result<CompanyEmployeeVo> responsibleResult = companyEmployeeFeignClient.findCompanyEmployeeById(positionInfo.getResponsibleId());
        recruiterPositionInfoVo.setCreator(creatorResult.getData());
        recruiterPositionInfoVo.setResponsible(responsibleResult.getData());

        // 获取地址
        Result<AddressInfoVo> addressInfoVoResult = companyAddressFeignClient.getAddressById(positionInfo.getAddressId());
        recruiterPositionInfoVo.setAddress(addressInfoVoResult.getData());

        // 获取职位类型描述
        String positionTypeDesc = positionTypeService.getPositionTypeDescByPositionId(positionInfo.getPositionTypeId());
        recruiterPositionInfoVo.setPositionTypeDesc(positionTypeDesc);

        // 设置职位名称
        recruiterPositionInfoVo.setPositionName(positionInfo.getPositionName());

        // 设置职位描述
        Optional<PositionDescriptionDoc> optional = positionDescriptionRepository.findById(positionInfo.getPositionDescription());
        optional.ifPresent(positionDescriptionDoc -> recruiterPositionInfoVo.setPositionDescription(positionDescriptionDoc.getDescription()));

        // 设置 职位类型,
        recruiterPositionInfoVo.setPositionType(positionInfo.getPositionType());
        recruiterPositionInfoVo.setEducationRequirement(positionInfo.getEducationRequirement());
        recruiterPositionInfoVo.setExperienceRequirement(positionInfo.getExperienceRequirement());
        recruiterPositionInfoVo.setDailyWorkHours(positionInfo.getDailyWorkHours());
        recruiterPositionInfoVo.setWeeklyWorkDays(positionInfo.getWeeklyWorkDays());
        recruiterPositionInfoVo.setMinSalary(positionInfo.getMinSalary());
        recruiterPositionInfoVo.setMaxSalary(positionInfo.getMaxSalary());
        recruiterPositionInfoVo.setStatus(positionInfo.getStatus());
        recruiterPositionInfoVo.setWatchCount(positionInfo.getWatchCount());
        recruiterPositionInfoVo.setCommunicationCount(positionInfo.getCommunicationCount());
        recruiterPositionInfoVo.setFavoriteCount(positionInfo.getFavoriteCount());

        return recruiterPositionInfoVo;
    }

    @Override
    public boolean setPositionStatusToOpen(Long positionId, Long userId) {
        // 获取用户的 公司id 和 是否是管理员
        Result<CompanyIdAndIsAdmin> companyIdAndIsAdminByUserId = companyEmployeeFeignClient.findCompanyIdAndIsAdminByUserId();
        CompanyIdAndIsAdmin data = companyIdAndIsAdminByUserId.getData();
        if (data == null) {
            throw new IllegalStateException("用户数据查询失败");
        }
        Long companyId = data.getCompanyId();
        Integer isAdmin = data.getIsAdmin();

        // 如果是管理员,可以操控全部职位，如果不是只能操控自己
        LambdaUpdateWrapper<PositionInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PositionInfo::getStatus, 4)
                .eq(PositionInfo::getId, positionId)
                .eq(PositionInfo::getCompanyId, companyId)
                .eq(PositionInfo::getStatus, 3);

        // 不是管理员只能操控自己的创建或负责的职位
        if (Objects.equals(isAdmin, 0)) {
            updateWrapper.and(wrapper -> wrapper.eq(PositionInfo::getCreatorId, userId)
                    .or().eq(PositionInfo::getResponsibleId, userId));
        }

        int isSuccess = positionInfoMapper.update(updateWrapper);

        BaseEsInfo<PositionInfo> positionInfoBaseEsInfo = new BaseEsInfo<>();
        positionInfoBaseEsInfo.setOp("u");
        PositionInfo positionInfo = new PositionInfo();
        positionInfo.setId(positionId);
        positionInfoBaseEsInfo.setData(positionInfo);
        rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_POSITION, positionInfoBaseEsInfo);
        return isSuccess == 1;
    }

    @Override
    public boolean setPositionStatusToNoOpen(Long positionId, Long userId) {
        // 获取用户的 公司id 和 是否是管理员
        Result<CompanyIdAndIsAdmin> companyIdAndIsAdminByUserId = companyEmployeeFeignClient.findCompanyIdAndIsAdminByUserId();
        CompanyIdAndIsAdmin data = companyIdAndIsAdminByUserId.getData();
        if (data == null) {
            throw new IllegalStateException("用户数据查询失败");
        }
        Long companyId = data.getCompanyId();
        Integer isAdmin = data.getIsAdmin();

        // 如果是管理员,可以操控全部职位，如果不是只能操控自己
        LambdaUpdateWrapper<PositionInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PositionInfo::getStatus, 3)
                .eq(PositionInfo::getId, positionId)
                .eq(PositionInfo::getCompanyId, companyId)
                .eq(PositionInfo::getStatus, 4);

        // 不是管理员只能操控自己的创建或负责的职位
        if (Objects.equals(isAdmin, 0)) {
            updateWrapper.and(wrapper -> wrapper.eq(PositionInfo::getCreatorId, userId)
                    .or().eq(PositionInfo::getResponsibleId, userId));
        }

        int isSuccess = positionInfoMapper.update(updateWrapper);

        BaseEsInfo<PositionInfo> positionInfoBaseEsInfo = new BaseEsInfo<>();
        positionInfoBaseEsInfo.setOp("d");
        PositionInfo positionInfo = new PositionInfo();
        positionInfo.setId(positionId);
        positionInfoBaseEsInfo.setData(positionInfo);
        rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_POSITION, positionInfoBaseEsInfo);
        return isSuccess == 1;
    }

    @Override
    public boolean setPositionStatusToClose(Long positionId, Long userId) {
        // 获取用户的 公司id 和 是否是管理员
        Result<CompanyIdAndIsAdmin> companyIdAndIsAdminByUserId = companyEmployeeFeignClient.findCompanyIdAndIsAdminByUserId();
        CompanyIdAndIsAdmin data = companyIdAndIsAdminByUserId.getData();
        if (data == null) {
            throw new IllegalStateException("用户数据查询失败");
        }
        Long companyId = data.getCompanyId();
        Integer isAdmin = data.getIsAdmin();

        // 如果是管理员,可以操控全部职位，如果不是只能操控自己
        LambdaUpdateWrapper<PositionInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PositionInfo::getStatus, 5)
                .eq(PositionInfo::getId, positionId)
                .eq(PositionInfo::getCompanyId, companyId)
                .in(PositionInfo::getStatus, 3, 4);

        // 不是管理员只能操控自己的创建或负责的职位
        if (Objects.equals(isAdmin, 0)) {
            updateWrapper.and(wrapper -> wrapper.eq(PositionInfo::getCreatorId, userId)
                    .or().eq(PositionInfo::getResponsibleId, userId));
        }

        int isSuccess = positionInfoMapper.update(updateWrapper);
        BaseEsInfo<PositionInfo> positionInfoBaseEsInfo = new BaseEsInfo<>();
        positionInfoBaseEsInfo.setOp("d");
        PositionInfo positionInfo = new PositionInfo();
        positionInfo.setId(positionId);
        positionInfoBaseEsInfo.setData(positionInfo);
        rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_POSITION, positionInfoBaseEsInfo);
        return isSuccess == 1;
    }

    @Override
    public boolean deletePositionById(Long positionId, Long userId) {
        // 获取用户的 公司id 和 是否是管理员
        Result<CompanyIdAndIsAdmin> companyIdAndIsAdminByUserId = companyEmployeeFeignClient.findCompanyIdAndIsAdminByUserId();
        CompanyIdAndIsAdmin data = companyIdAndIsAdminByUserId.getData();
        if (data == null) {
            throw new IllegalStateException("用户数据查询失败");
        }
        Long companyId = data.getCompanyId();
        Integer isAdmin = data.getIsAdmin();

        // 如果是管理员,可以操控全部职位，如果不是只能操控自己
        LambdaQueryWrapper<PositionInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PositionInfo::getId, positionId)
                .eq(PositionInfo::getCompanyId, companyId);

        // 不是管理员只能操控自己的创建或负责的职位
        if (Objects.equals(isAdmin, 0)) {
            queryWrapper.and(wrapper -> wrapper.
                    eq(PositionInfo::getCreatorId, userId).or().eq(PositionInfo::getResponsibleId, userId));
        }

        int isSuccess = positionInfoMapper.delete(queryWrapper);

        BaseEsInfo<PositionInfo> positionInfoBaseEsInfo = new BaseEsInfo<>();
        positionInfoBaseEsInfo.setOp("d");
        PositionInfo positionInfo = new PositionInfo();
        positionInfo.setId(positionId);
        positionInfoBaseEsInfo.setData(positionInfo);
        rabbitService.sendMessage(MqConst.EXCHANGE_ES, MqConst.ROUTING_ES_POSITION, positionInfoBaseEsInfo);
        return isSuccess == 1;
    }

    @Override
    public PagePositionEsVo<CandidateBasePositionInfoVo> searchCandidateBasePositionInfo(Integer limit, CandidatePositionPageParam candidatePositionPageParam) {
        // 设置查询参数
        NativeQueryBuilder builder = NativeQuery.builder();
        BoolQuery.Builder bool = QueryBuilders.bool();

        // 如果 搜索关键词存在 设置 positionName,positionDescription,companyName
        String search = candidatePositionPageParam.getSearch();
        if (search != null && !search.isEmpty()) {
            MultiMatchQuery query = QueryBuilders.multiMatch()
                    .query(search)
                    .fields(Arrays.asList("companyName^6", "positionName^2", "positionDescription"))
                    .type(TextQueryType.BestFields)
                    .operator(Operator.Or)
                    .fuzziness("AUTO")
                    .build();
            bool.must(q -> q.multiMatch(query));
        }

        // 如果 职位类型 id 存在 设置 positionTypeId
        Long positionTypeId = candidatePositionPageParam.getPositionTypeId();
        if (positionTypeId != null) {
            TermQuery query = QueryBuilders.term()
                    .field("positionTypeId")
                    .value(positionTypeId)
                    .build();

            bool.filter(q -> q.term(query));
        }

        // 如果 求职类型 存在 设置 positionType
        Integer positionType = candidatePositionPageParam.getPositionType();
        if (positionType != null) {
            TermQuery query = QueryBuilders.term()
                    .field("positionType")
                    .value(positionType)
                    .build();
            bool.filter(q -> q.term(query));
        }

        // 如果 经验要求 存在 设置 experienceRequirement
        Integer experienceRequirement = candidatePositionPageParam.getExperienceRequirement();
        if (experienceRequirement != null) {
            TermQuery query = QueryBuilders.term()
                    .field("experienceRequirement")
                    .value(experienceRequirement)
                    .build();
            bool.filter(q -> q.term(query));
        }

        // 如果 学历要求 存在 设置 educationRequirement
        Integer educationRequirement = candidatePositionPageParam.getEducationRequirement();
        if (educationRequirement != null) {
            TermQuery query = QueryBuilders.term()
                    .field("educationRequirement")
                    .value(educationRequirement)
                    .build();
            bool.filter(q -> q.term(query));
        }

        // 如果 地区 存在 设置 district
        String district = candidatePositionPageParam.getDistrict();
        if (district != null && !district.isEmpty()) {
            TermQuery query = QueryBuilders.term()
                    .field("district")
                    .value(district)
                    .build();
            bool.filter(q -> q.term(query));
        }

        /*
         * 如果 minSalary,maxSalary 存在 (任意一个也能处理)
         * min = 12 , max = 20     : 12 - 20
         * min = null , max = 20   : 0  - 20
         * min = 12 , max = null   : 12 - 999
         */
        Integer minSalary = candidatePositionPageParam.getMinSalary();
        Integer maxSalary = candidatePositionPageParam.getMaxSalary();
        if (minSalary != null || maxSalary != null) {
            Query minSalaryQuery = QueryBuilders.range(q -> q.field("minSalary").lte(maxSalary != null ? JsonData.of(maxSalary) : JsonData.of(999)));
            Query maxSalaryQuery = QueryBuilders.range(q -> q.field("maxSalary").gte(minSalary != null ? JsonData.of(minSalary) : JsonData.of(0)));

            BoolQuery boolSalaryQuery = QueryBuilders.bool().must(minSalaryQuery).must(maxSalaryQuery).build();
            bool.filter(q -> q.bool(boolSalaryQuery));
        }


        /*
         * 如果 lat、lon (必须) 和 distance (非必须,默认值:50km)
         */
        Double lat = candidatePositionPageParam.getLat(); // 维度
        Double lon = candidatePositionPageParam.getLon(); // 经度
        Integer distance = candidatePositionPageParam.getDistance();
        if (lat != null && lon != null) {
            GeoDistanceQuery location = QueryBuilders.geoDistance()
                    .field("location")
                    .distance((distance != null ? distance : 50) + "km")
                    .location(
                            g -> g.latlon(LatLonGeoLocation.of(l -> l.lat(lat).lon(lon)))
                    ).build();
            bool.filter(q -> q.geoDistance(location));
        }

        // 如果 公司规模 存在 设置 companySizeId
        Integer companySizeId = candidatePositionPageParam.getCompanySizeId();
        if (companySizeId != null) {
            TermQuery query = QueryBuilders.term()
                    .field("companySizeId")
                    .value(companySizeId)
                    .build();
            bool.filter(q -> q.term(query));
        }

        // 如果有 分页 信息 设置 limit , search_after
        Double score = candidatePositionPageParam.getScore();
        Long updateTime = candidatePositionPageParam.getUpdateTime();
        if (score != null && updateTime != null) {
            builder.withSearchAfter(Arrays.asList(score, updateTime));
        }

        // 设置查询
        NativeQuery query = builder
                .withQuery(q -> q.bool(bool.build()))
                .withSort(Sort.by(  // 查询排序
                        Sort.Order.desc("_score"),
                        Sort.Order.desc("updateTime")
                ))
                .withPageable(PageRequest.of(0, limit))
                .build();

        // 搜索结果
        SearchHits<PositionInfoES> positionInfoESSearchHits = elasticsearchOperations.search(query, PositionInfoES.class);

        PagePositionEsVo<CandidateBasePositionInfoVo> candidateBasePositionInfoVoPagePositionEsVo = new PagePositionEsVo<>();

        // 设置总记录条数
        candidateBasePositionInfoVoPagePositionEsVo.setTotal(positionInfoESSearchHits.getTotalHits());

        // 获取 searchHit 列表
        List<SearchHit<PositionInfoES>> searchHits = positionInfoESSearchHits.getSearchHits();
        if (!searchHits.isEmpty()) {
            // 获取最后一条记录
            SearchHit<PositionInfoES> lastHit = searchHits.get(searchHits.size() - 1);

            // 设置 sortValues 结果
            candidateBasePositionInfoVoPagePositionEsVo.setScore((Double) lastHit.getSortValues().get(0));
            candidateBasePositionInfoVoPagePositionEsVo.setUpdateTime((Long) lastHit.getSortValues().get(1));
        }

        List<CandidateBasePositionInfoVo> candidateBasePositionInfoVoList = searchHits.stream().map(positionInfoESSearchHit -> {
            PositionInfoES positionInfoES = positionInfoESSearchHit.getContent();
            return new CandidateBasePositionInfoVo(
                    positionInfoES.getId(),
                    positionInfoES.getPositionName(),
                    positionInfoES.getCompanyName(),
                    positionInfoES.getPositionTypeId(),
                    positionInfoES.getPositionType(),
                    positionInfoES.getExperienceRequirement(),
                    positionInfoES.getEducationRequirement(),
                    positionInfoES.getMinSalary(),
                    positionInfoES.getMaxSalary(),
                    positionInfoES.getDistrict(),
                    positionInfoES.getLocation(),
                    positionInfoES.getCompanySizeId()
            );
        }).toList();

        candidateBasePositionInfoVoPagePositionEsVo.setRecords(candidateBasePositionInfoVoList);
        return candidateBasePositionInfoVoPagePositionEsVo;
    }

    @Override
    public CandidatePositionInfoVo getCandidatePositionInfoById(Long id) {
        // 1.职位信息 2. 公司信息 3.职位负责人信息 4.地址信息
        // 获取职位信息
        CandidatePositionInfoVo candidatePositionInfoVo = new CandidatePositionInfoVo();
        PositionInfo positionInfo = getPositionInfoById(id);
        if (positionInfo == null) {
            throw new RuntimeException("查询不到职位信息");
        }
        BeanUtils.copyProperties(positionInfo, candidatePositionInfoVo);

        // 获取 公司信息
        Result<CompanyInfoVo> companyInfoVoResult = companyInfoFeignClient.getCompanyInfoVo(positionInfo.getCompanyId());
        if (!Objects.equals(companyInfoVoResult.getCode(), 200)) throw new RuntimeException("查询不到职位公司信息");
        candidatePositionInfoVo.setCompanyInfoVo(companyInfoVoResult.getData());

        // 获取 公司地址
        Result<AddressInfoVo> addressInfoVoResult = companyAddressFeignClient.getAddressById(positionInfo.getAddressId());
        if (!Objects.equals(addressInfoVoResult.getCode(), 200)) throw new RuntimeException("查询不到地址信息");
        candidatePositionInfoVo.setAddress(addressInfoVoResult.getData());

        // 获取 职位负责人信息
        Result<CompanyEmployeeVo> companyEmployeeVoResult = companyEmployeeFeignClient.getCompanyEmployeeById(positionInfo.getResponsibleId(), keyProperties.getKey());
        if (!Objects.equals(companyEmployeeVoResult.getCode(), 200)) throw new RuntimeException("职位负责人获取失败");
        candidatePositionInfoVo.setResponsible(companyEmployeeVoResult.getData());

        return candidatePositionInfoVo;
    }

    /**
     * 获取职位信息
     *
     * @param id 职位 id
     * @return PositionInfo
     */
    private PositionInfo getPositionInfoById(Long id) {
        String redisKey = RedisConstant.POSITION_INFO_CANDIDATE + id;
        PositionInfo positionInfo = null;
        positionInfo = redisTemplate.opsForValue().get(redisKey);
        // 未命中从 Mysql 查询
        if (positionInfo == null) {
            // 查询条件 职位 id , status = 4
            LambdaQueryWrapper<PositionInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PositionInfo::getId, id)
                    .eq(PositionInfo::getStatus, 4);

            positionInfo = positionInfoMapper.selectOne(queryWrapper);

            // 保存到 redis
            redisTemplate.opsForValue().set(
                    redisKey,
                    positionInfo,
                    RedisConstant.POSITION_INFO_CANDIDATE_TIME_OUT,
                    TimeUnit.SECONDS
            );
        }

        if (positionInfo == null) return null;
        // 查询 MongoDB 中的 职位描述
        Optional<PositionDescriptionDoc> optional = positionDescriptionRepository.findById(positionInfo.getPositionDescription());
        String valPositionDesc = null;
        valPositionDesc = optional.map(PositionDescriptionDoc::getDescription).orElse("职位详情获取失败");
        positionInfo.setPositionDescription(valPositionDesc);

        // 返回结果
        return positionInfo;
    }
}
