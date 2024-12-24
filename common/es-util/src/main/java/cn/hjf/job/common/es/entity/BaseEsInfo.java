package cn.hjf.job.common.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEsInfo<T> {

    // 操作方式 u : 更新 ,c : 创建,d : 删除,
    private String op;

    // 数据
    private T data;
}
