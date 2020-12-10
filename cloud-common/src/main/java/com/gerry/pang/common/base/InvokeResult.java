package com.gerry.pang.common.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 统一返回值
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvokeResult<T extends Object> {
    private Object data;
    private String errorMessage;
    // private String resultCode;
    private boolean hasErrors;
    private boolean success;

    public InvokeResult() {
        super();
    }

    /**
     * 构造一个失败的响应
     *
     * @param msg 失败描述
     * @return 失败的响应
     */
    public static InvokeResult fail(String msg) {
        InvokeResult invokeResult = new InvokeResult();
        invokeResult.failure(msg);
        return invokeResult;
    }

    /**
     * 构造一个成功的响应
     *
     * @param data 数据
     * @return 成功的响应
     */
    public static InvokeResult success(Object data) {
        InvokeResult invokeResult = new InvokeResult();
        invokeResult.success();
        invokeResult.setData(data);
        return invokeResult;
    }

    /**
     * success
     *
     * @return
     */
    public InvokeResult<T> success() {
        this.hasErrors = false;
        this.errorMessage = "";
        this.success = true;
        return this;
    }

    /**
     * failure
     *
     * @param message
     * @return
     */
    public InvokeResult<T> failure(String message) {
        this.hasErrors = true;
        this.success = false;
        this.errorMessage = message;
        return this;
    }

}
