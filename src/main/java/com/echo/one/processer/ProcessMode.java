package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.exception.DataMigrationException;

/**
 * @author echo
 * @date 2022/11/22 18:10
 */
public abstract class ProcessMode {

    public final void invoke(TransferContext transferContext) {
        try {
            handler(transferContext);
        } catch (DataMigrationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataMigrationException("system error, msg: " + e.getMessage());
        }
    }

    protected abstract void handler(TransferContext transferContext);

}
