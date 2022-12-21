package com.echo.one.utils.jdbc;

import com.echo.one.common.constant.DataMigrationConstant;
import com.echo.one.common.exception.DataMigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author echo
 * @date 2022/11/10 14:40
 */
public class JdbcUtils {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("init mysql Driver error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.INIT_MYSQL_DRIVER_ERROR);
        }
    }

    private JdbcUtils() {
    }

    public static Connection getConn(String url, String user, String password) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            logger.error("init mysql connection error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.INIT_MYSQL_CONNECTION_ERROR);
        }
        return conn;
    }

    public static Connection getDruidConn(String url, String user, String password) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            logger.error("init mysql connection error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.INIT_MYSQL_CONNECTION_ERROR);
        }
        return conn;
    }

    public static void closeConn(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            logger.error("mysql connection close error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_CLOSE_ERROR);
        }
    }

    public static Statement getStmt(Connection conn) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            logger.error("mysql getStmt error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_GET_STATEMENT_ERROR);
        }
        return stmt;
    }

    public static PreparedStatement getPreStmt(Connection conn, String sql) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            logger.error("mysql getPreStmt error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_GET_PREPARED_STATEMENT_ERROR);
        }
        return pstmt;
    }

    public static void closeStmt(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (SQLException e) {
            logger.error("mysql closeStmt error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_CLOSE_STATEMENT_ERROR);
        }

    }

    public static ResultSet executeQuery(Statement stmt, String sql) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("mysql executeQuery error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_EXECUTE_QUERY_ERROR);
        }
        return rs;
    }

    public static void closeRs(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            logger.error("mysql closeRs error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_CLOSE_RESULT_SET_ERROR);
        }
    }

    public static ResultSet executeQuery(Connection conn, String sql) {
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            logger.error("mysql executeQuery error!!! error msg: {}", e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_EXECUTE_QUERY_ERROR);
        }
        return rs;
    }

}