package cn.hjf.job.company.service.impl;

import cn.hjf.job.common.minio.resolver.PublicFileUrlResolver;
import cn.hjf.job.company.mapper.CompanyAlbumMapper;
import cn.hjf.job.company.service.CompanyAlbumService;
import cn.hjf.job.model.entity.company.CompanyAlbum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class CompanyAlbumServiceImpl extends ServiceImpl<CompanyAlbumMapper, CompanyAlbum> implements CompanyAlbumService {

    @Resource
    private CompanyAlbumMapper companyAlbumMapper;

    @Resource
    private PublicFileUrlResolver publicFileUrlResolver;

    @Override
    public List<String> findRecruiterPhotos(Long companyId) {
        LambdaQueryWrapper<CompanyAlbum> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CompanyAlbum::getFileUrl)
                .eq(CompanyAlbum::getMediaType, 0)
                .eq(CompanyAlbum::getCompanyId, companyId);
        List<CompanyAlbum> companyAlbums = companyAlbumMapper.selectList(queryWrapper);
        if (companyAlbums == null) return new ArrayList<>();
        List<String> paths = companyAlbums.stream().map(CompanyAlbum::getFileUrl).toList();
        return publicFileUrlResolver.resolveMultipleUrls(paths);
    }

    @Override
    public boolean savePhoto(Long companyId, String path) {
        LambdaQueryWrapper<CompanyAlbum> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CompanyAlbum::getCompanyId, companyId).eq(CompanyAlbum::getMediaType, 0);
        Long count = companyAlbumMapper.selectCount(queryWrapper);
        // 如果 照片数量大于 20 张 就不加载了
        if (count >= 20) return false;

        CompanyAlbum companyAlbum = new CompanyAlbum();
        companyAlbum.setCompanyId(companyId);
        companyAlbum.setMediaType(0);
        companyAlbum.setFileUrl(path);

        int insert = companyAlbumMapper.insert(companyAlbum);
        return insert == 1;
    }
}
