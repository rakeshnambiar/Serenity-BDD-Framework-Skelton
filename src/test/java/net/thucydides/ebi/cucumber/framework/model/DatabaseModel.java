package net.thucydides.ebi.cucumber.framework.model;

/**
 * Created by rakeshnbr on 11/01/2017.
 */
public class DatabaseModel {

    private String dbName;
    private String dbUserName;
    private String dbPassword;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

}
