package com.echo.one.common.exception;

/**
 * 全局错误
 *
 * @author echo
 * @date 2022/11/3 16:32
 */
public class DataMigrationException extends RuntimeException {

    /**
     * 参数
     */
    protected Object[] params;

    protected DataMigrationException() {
        super();
    }

    public DataMigrationException(String message) {
        super(message);
    }

    public DataMigrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataMigrationException(Throwable cause) {
        super(cause);
    }

    public DataMigrationException(String message, Object... params) {
        super(message);
        this.params = params;
    }


    public DataMigrationException(String message, Throwable cause, Object... params) {
        super(message, cause);
        this.params = params;
    }

    public Object[] getParams() {
        return this.params;
    }

}
