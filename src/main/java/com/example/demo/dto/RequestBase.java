package com.example.demo.dto;

import java.io.Serializable;
import javax.validation.constraints.Min;

/**
 * 请求基本要素
 */
public class RequestBase implements Serializable {

    /**
     * 请求ID
     */
    @Min(value = 1, message = "无效的请求ID")
    private int reqeustId;


}
