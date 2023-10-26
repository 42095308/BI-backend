package com.yupi.springbootinit.model.vo;

import lombok.Data;

/**
 * BI的返回结果
 */
@Data
public class BiResponse {

    private String genChart;

    private String genResult;
    // 新生成的图标id
    private Long chartId;

}
