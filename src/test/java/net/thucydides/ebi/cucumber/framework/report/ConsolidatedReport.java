package net.thucydides.ebi.cucumber.framework.report;


import net.thucydides.ebi.cucumber.framework.model.ReportModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.thucydides.ebi.cucumber.framework.report.MergeReports.FILE_PATH;


/**
 * Created by S746032 on 27/12/2015.
 * This class is reads all the json files and consolidates the reports
 */
public class ConsolidatedReport {
    private static final String JOBS = "JOBS";
    private static final String ELEMENTS = "elements";
    private static final String KEYWORD = "keyword";
    private static final String TAGS = "tags";
    private static final String BACKGROUND = "Background";
    private static final String NAME = "name";
    private static final String STEPS = "steps";
    private static final String RESULT = "result";
    private static final String STATUS = "status";
    private static final String PASSED = "passed";
    private static final String JOBS_PATH = "/apps/jboss/.jenkins/jobs/";
    private static final String JOB_BUILD_DIR = "/builds";
    private static final String NEXT_BUILD_NUMBER_FILE_NAME = "/nextBuildNumber";

    private MergeReports mergeReports;
    private List<ReportModel> jsonReports;

    /**
     * Constructor to initialize the object
     */
    public ConsolidatedReport() {
        mergeReports = new MergeReports();
        jsonReports = new ArrayList<ReportModel>();
    }

    public void copyJsonFilesFromJobs() {
        String jobNames = System.getenv(JOBS);
        String[] jobs = jobNames.split(",");
        File destDir = new File(FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        for (int i = 0; i < jobs.length; i++) {
            String jobPath = JOBS_PATH + jobs[i].trim();
            String buildDirPath = jobPath + JOB_BUILD_DIR;
            int buildNumber = getBuildNumber(jobPath);
            System.out.println(String.format("Build number for JOB %s is %d", jobs[i], buildNumber));
            if (buildNumber != 0) {
                System.out.println("JSON File path is " + buildDirPath + "/" + buildNumber + "/");
                List<String> jsonFilePaths = mergeReports.getJsonFiles(buildDirPath + "/" + buildNumber + "/");
                for (String jsonFilePath : jsonFilePaths) {
                    File jsonFile = new File(jsonFilePath);
                    try {
                        FileUtils.copyFileToDirectory(jsonFile, destDir);
                    } catch (IOException e) {
                        System.out.println(String.format("%s file could not be copied do loaction %s", jsonFile, destDir));
                    }
                }
            }
        }
    }

    public int getBuildNumber(String jobBuildPath) {
        FileReader inputFile = null;
        BufferedReader br = null;
        try {
            inputFile = new FileReader(jobBuildPath + NEXT_BUILD_NUMBER_FILE_NAME);
            br = new BufferedReader(inputFile);
            String line = br.readLine();
            if (!StringUtils.isEmpty(line)) {
                return Integer.parseInt(line.trim()) - 1;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Could not read build number for " + jobBuildPath);
        }
        return 0;
    }

    /**
     * Creates a List object with all the scenrion data fetched from json files
     *
     * @throws Exception
     */
    public void getStatisticsFromJsons() throws Exception {
        List<String> jsonFileList = mergeReports.getJsonFiles(FILE_PATH);
        for (String jsonFile : jsonFileList) {
            getDataFromJsonFile(jsonFile);
        }
        createReport();
    }

    /**
     * Fetches scenarion data from a json file
     *
     * @param jsonFileName
     * @return
     * @throws Exception
     */
    public List<ReportModel> getDataFromJsonFile(String jsonFileName) {
        try {
            JSONObject jsonObject = mergeReports.getJsonObject(jsonFileName);
            String id = (String) jsonObject.get(NAME);
            JSONArray scenarios = (JSONArray) jsonObject.get(ELEMENTS);
            for (int i = 0; i < scenarios.size(); i++) {
                ReportModel reportModel = new ReportModel();
                JSONObject scenario = (JSONObject) scenarios.get(i);
                if(!getElementKeyword(scenario).equalsIgnoreCase(BACKGROUND)) {
                    reportModel.setId(id);
                    reportModel.setScenario(getScenarioName(scenario));
                    reportModel.setTestName(getTestName(scenario));
                    String status = getStatus(scenario);
                    reportModel.setStatus(status);
                    jsonReports.add(reportModel);
                }
            }
        } catch (Exception e) {
            ReportModel reportModelException = new ReportModel();
            reportModelException.setId(jsonFileName);
            reportModelException.setScenario("Unknown");
            reportModelException.setTestName("Unknown");
            reportModelException.setStatus("failed");
            jsonReports.add(reportModelException);
        }
        return jsonReports;
    }

    public String getElementKeyword(JSONObject jsonObject){
        return (String) jsonObject.get(KEYWORD);
    }

    /**
     * Gets the scenario text
     *
     * @param jsonObject
     * @return
     */
    public String getScenarioName(JSONObject jsonObject) {
        return (String) jsonObject.get(NAME);
    }

    /**
     * Gets the tags in the scenario
     *
     * @param jsonObject
     * @return
     */
    public String getTestName(JSONObject jsonObject) {
        String tags = "";
        JSONArray jsonTags = (JSONArray) jsonObject.get(TAGS);
        for (int i = 0; i < jsonTags.size(); i++) {
            JSONObject jsonTag = (JSONObject) jsonTags.get(i);
            tags = tags + " " + jsonTag.get(NAME);
        }
        return tags.trim();
    }

    /**
     * Sets the status of a scenarion to passed/failed. If any steps were skipped it sets the status to failed
     *
     * @param jsonObject
     * @return
     */
    public String getStatus(JSONObject jsonObject) {
        String status = "passed";
        JSONArray jsonSteps = (JSONArray) jsonObject.get(STEPS);
        for (int i = 0; i < jsonSteps.size(); i++) {
            JSONObject jsonStep = (JSONObject) jsonSteps.get(i);
            JSONObject jsonResult = (JSONObject) jsonStep.get(RESULT);
            String jsonStatus = (String) jsonResult.get(STATUS);
            if (!jsonStatus.equalsIgnoreCase(PASSED)) {
                status = "failed";
                break;
            }
        }
        return status;
    }

    /**
     * Creates the html string with data fetched from json files
     *
     * @throws IOException
     */
    public void createReport() throws IOException {
        Date dateTime = new Date();
        int totalScenarioCount = jsonReports.size();
        int passedScenarioCount = 0;
        int failedScenrioCount = 0;
        float percentage;
        StringBuilder table2Rows = new StringBuilder();
        for (ReportModel reportModel : jsonReports) {
            String bgColor;
            if (reportModel.getStatus().equalsIgnoreCase(PASSED)) {
                passedScenarioCount++;
                bgColor = "#ffffff";
            } else {
                failedScenrioCount++;
                bgColor = "#dbdbdb";
            }
            table2Rows.append("<tr bgcolor=\"");
            table2Rows.append(bgColor);
            table2Rows.append("\"><td>");
            table2Rows.append(reportModel.getId());
            table2Rows.append("</td><td>");
            table2Rows.append(reportModel.getScenario());
            table2Rows.append("</td><td>");
            table2Rows.append(reportModel.getTestName());
            table2Rows.append("</td><td>");
            table2Rows.append(reportModel.getStatus());
            table2Rows.append("</td></tr>");
        }
        StringBuilder scriptTag = new StringBuilder();
        scriptTag.append("<script type=\"text/javascript\">\n");
        scriptTag.append("\t\t\twindow.onload = function () {\n");
        scriptTag.append("\t\t\tvar chart = new CanvasJS.Chart(\"chartContainer\",\n");
        scriptTag.append("\t\t\t{\n");
        scriptTag.append("\t\t\t\ttitle:{\n");
        scriptTag.append("\t\t\t\t\ttext: \"Test Statistics\"    \n");
        scriptTag.append("\t\t\t\t},\n");
        scriptTag.append("\t\t\t\tanimationEnabled: true,\n");
        scriptTag.append("\t\t\t\ttheme: \"theme5\",\n");
        scriptTag.append("\t\t\t\tdata: [\n");
        scriptTag.append("\t\t\t\t{        \n");
        scriptTag.append("\t\t\t\t\ttype: \"bar\",  \n");
        scriptTag.append("\t\t\t\t\tshowInLegend: false, \n");
        scriptTag.append("\t\t\t\t\tdataPoints: [");
        scriptTag.append("{y: ");
        scriptTag.append(totalScenarioCount);
        scriptTag.append(", label: \"Total\"},");
        scriptTag.append("{y: ");
        scriptTag.append(failedScenrioCount);
        scriptTag.append(", label: \"Failed\"},");
        scriptTag.append("{y: ");
        scriptTag.append(passedScenarioCount);
        scriptTag.append(", label: \"Passed\"}");
        scriptTag.append("]}]});\n");
        scriptTag.append("\t\t\tchart.render();\n");
        scriptTag.append("\t\t\t}\n");
        scriptTag.append("\t\t</script>\n");
        scriptTag.append("\t\t<script type=\"text/javascript\" src=\"linuxLogImpl-classes/canvasjs.min.js\"></script>");
        percentage = (float) passedScenarioCount / totalScenarioCount * 100;
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"en\">");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Consolidated ASUI automation linuxLogImpl report</title>");
        html.append(scriptTag);
        html.append("</head>");
        html.append("<body bgcolor=\"#ffffff\">");
        html.append("<h1>Consolidated linuxLogImpl report</h1>");
        html.append("<h2>Date : ");
        html.append(dateTime);
        html.append("</h2>");
        html.append("<div id=\"chartContainer\" style=\"height: 250px; width: 100%;\"></div>");
        html.append("<br>");
        StringBuilder table1 = new StringBuilder();
        table1.append("<div>");
        table1.append("<table bgcolor=\"#ffffff\" width=\"50%\" style=\"border:1px solid\">");
        table1.append("<tr><th>Total</th><th>Passed</th><th>Failed</th><th>Passed Percentage</th></tr>");
        table1.append("<tr align=\"center\"><td>");
        table1.append(totalScenarioCount);
        table1.append("</td><td>");
        table1.append(passedScenarioCount);
        table1.append("</td><td>");
        table1.append(failedScenrioCount);
        table1.append("</td><td>");
        table1.append(String.format("%.2g%n", percentage));
        table1.append("</td></tr></table></div>");

        StringBuilder table2 = new StringBuilder();
        table2.append("<div><table border=\"1\" bgcolor=\"#ffffff\" width=\"100%\" cellspacing=\"0.5\"><tr>");
        table2.append("<th>Feature</th>");
        table2.append("<th>Scenario</th>");
        table2.append("<th>Tags</th>");
        table2.append("<th>Status</th></tr>");


        html.append(table1);
        html.append("<br>");
        html.append(table2);
        html.append(table2Rows);
        html.append("</table></div>");
        html.append("</body>");
        html.append("</html>");

        createHTML(html.toString());
    }

    /**
     * Main method to trigger the service
     *
     * @param a
     */
    public static void main(String a[]) {
        ConsolidatedReport consolidatedReport = new ConsolidatedReport();
        try {
            consolidatedReport.copyJsonFilesFromJobs();
            consolidatedReport.getStatisticsFromJsons();
        } catch (Exception e) {
            System.out.println("Report could not be created" + e.getMessage());
        }
    }

    /**
     * Writes the html strin to a file
     *
     * @param html
     * @throws IOException
     */
    public void createHTML(String html) throws IOException {
        PrintWriter writer = new PrintWriter("target/Consolidated-Report.html");
        writer.println(html);
        writer.close();
    }

}
