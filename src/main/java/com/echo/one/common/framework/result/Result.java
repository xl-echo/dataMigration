package com.echo.one.common.framework.result;

import com.echo.one.common.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回基类
 *
 * @author xlecho
 * @date 2019-06-20 06:06:11
 * @since jdk 1.8
 * @WeChat t2421499075
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 2120267584344923858L;

    /**
     * 返回状态
     */
    private Integer status = 0;
    /**
     * 调用失败返回信息
     */
    private String message = null;
    /**
     * 调用成功返回结果
     */
    private T data = null;

    /**
     * 封装返回值
     *
     * @param result 返回类
     * @param t      格式化类型
     * @param <T>    泛型
     */
    public static <T> void packageResultMethod(Result<T> result, T t) {
        result.setStatus(StatusCode.SUCCESS.getCode());
        result.setData(t);
    }
}
