package net.thucydides.ebi.cucumber.runCukes.reportbuilder;


import net.thucydides.ebi.cucumber.framework.report.MergeReports;
import org.junit.Test;


public class CucumberReportMergerTest {
    @Test
    public void mergeReports(){
        MergeReports mergeReports = new MergeReports();
        try {
            mergeReports.buildMergedReport();
        } catch (Exception e) {
            System.out.println("Report builder failed to merge the cucumber reports");
        }
    }
}
