package cn.hjf.job.company.service;

import cn.hjf.job.model.entity.company.CompanyAlbum;
import cn.hjf.job.model.vo.company.PhotoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface CompanyAlbumService extends IService<CompanyAlbum> {


    /**
     * 获取招聘端的全部照片
     *
     * @param companyId 公司 id
     * @return List<String>
     */
    List<PhotoVo> findRecruiterPhotos(Long companyId);

    /**
     * 保存照片
     *
     * @param companyId 公司id
     * @param path      路径
     * @return 是否成功
     */
    Long savePhoto(Long companyId, String path);

    /**
     * 删除照片
     *
     * @param companyId 公司 id
     * @param photoId   照片 id
     * @return 是否成功
     */
    boolean deletePhoto(Long companyId, Long photoId);

    /**
     * 根据公司 id 获取公司照片
     *
     * @param companyId 公司 id
     * @return List<String>
     */
    List<String> findCandidatePhotos(Long companyId);

}
