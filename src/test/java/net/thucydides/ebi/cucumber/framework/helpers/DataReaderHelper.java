package net.thucydides.ebi.cucumber.framework.helpers;


import net.thucydides.ebi.cucumber.framework.context.DatabaseContext;
import net.thucydides.ebi.cucumber.framework.model.DatabaseModel;
import java.util.concurrent.ThreadLocalRandom;


public class DataReaderHelper {

    public static int getARandomNumber(int min, int max) throws Exception {
        try {
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            return randomNum;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("ERROR: While getting a Random Number");
        }
    }

    public static void readEpmcDbDetails(String environment) throws Exception {
        try {
            String[] dbDetails = ExcelHelper.ReadExcelValues("DatabaseConfig", "'DatabaseName_"+environment+"',"+
                    "'DatabaseUser_"+environment+"','DatabasePWD_"+environment+"'");
            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setDbName(dbDetails[0]);
            databaseModel.setDbUserName(dbDetails[1]);
            databaseModel.setDbPassword(dbDetails[2]);
            DatabaseContext.databaseModel = databaseModel;
        }catch (Exception e){
            throw new Exception("ERROR: While Reading the EPMC DB Details");
        }
    }


}
