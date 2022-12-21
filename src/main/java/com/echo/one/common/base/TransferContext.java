package com.echo.one.common.base;

import com.echo.one.po.TransferDataTask;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.po.TransferInsertConfig;
import com.echo.one.po.TransferSelectConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/11 9:19
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferContext {

    private TransferDataTask transferDataTask;

    private List<TransferDatabaseConfig> transferDatabaseConfig;

    private TransferInsertConfig transferInsertConfig;

    private TransferSelectConfig transferSelectConfig;

    private String traceId;
}
