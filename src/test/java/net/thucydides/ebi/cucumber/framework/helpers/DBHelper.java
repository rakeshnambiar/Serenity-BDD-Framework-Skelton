package net.thucydides.ebi.cucumber.framework.helpers;

import net.thucydides.ebi.cucumber.framework.context.DatabaseContext;

import java.sql.*;

public class DBHelper {
    public static Connection connection = null;
    public static Statement stmt = null;

    public static Connection getConnection() throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String strConnection = "jdbc:oracle:thin:@" + DatabaseContext.databaseModel.getDbName();
            connection = DriverManager.getConnection(strConnection, DatabaseContext.databaseModel.getDbUserName(), DatabaseContext.databaseModel.getDbPassword());
        }catch (Exception e){
            throw new Exception("");
        }
        return connection;
    }

    public static Connection getConnection(String dbName, String user, String password) throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String strConnection = "jdbc:oracle:thin:@" + dbName;
            connection = DriverManager.getConnection(strConnection, user, password);
        }catch (Exception e){
            throw new Exception("");
        }
        return connection;
    }

    public static ResultSet getResultSet(Connection connection, String query) throws Exception {
        ResultSet rs;
        stmt = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
        } catch (Exception e) {
            throw new Exception("DataBase error while executing the query"
                    + e.getMessage());
        }
        return rs;
    }

    public static ResultSet getSQLResultSet(Connection con, String query) throws Exception {
        ResultSet rs;
        stmt = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (Exception e) {
            throw new Exception("DataBase error while executing the query"
                    + e.getMessage());
        }
        return rs;
    }

    public static void fireACommitQuery(String query) throws Exception {
        stmt = null;
        try {
            stmt = getConnection().createStatement();
            stmt.executeQuery(query);
            connection.commit();
            connection.close();
        } catch (Exception e) {
            connection.commit();
            connection.close();
            throw new Exception("DataBase error while executing the query"
                    + e.getMessage());
        }
    }

    public static Connection getSQLDataBaseConnection(String db_connect_string, String db_userid, String db_password) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(db_connect_string,
                    db_userid, db_password);
            System.out.println("Connected to SQL Database");
           } catch (Exception e) {
            e.printStackTrace();
        connection.close();
        }
        return connection;
    }

    public static void main(String[] args) throws Exception {
        /*ResultSet xx = getSQLResultSet("jdbc:sqlserver://ppmc-prod-001.ppmc.ebi.ac.uk:1433", "anyone",
                "9u8m3d","select * from dbo.AllocIds");
        while (xx.next()) {
            System.out.println(xx.getString(1));
        }*/

    }
}
