package net.thucydides.ebi.cucumber.framework.report;

import net.thucydides.ebi.cucumber.framework.helpers.FileHelper;

import java.io.*;
import java.util.*;

/**
 * Created by S427701 on 11/07/2016.
 */
public class HtmlReportWriter {
    public static void main(String[] args) throws Exception {
        ArrayList<List<String>> failed = new ArrayList<List<String>> ();
        ArrayList<List<String>> success = new ArrayList<List<String>> ();
        failed.add(Arrays.asList("lnx30", null, "sfdfdfsdf"));
        createHTMLReport(failed, success);
    }
/*    public static void createHTMLReport(HashMap<String,String> data, HashMap<String,String> sucessList) throws Exception {
        try {
            StringBuilder htmlStringBuilder = new StringBuilder();
            htmlStringBuilder.append("<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    ".msoFix {\n" +
                    "      mso-table-lspace:-1pt;\n" +
                    "      mso-table-rspace:-1pt;\n" +
                    "   }"+
                    "table, th, td {\n" +
                    "    border: 1.5px solid Red;\n" +
                    "}\n" +
                    "\n" +
                    "table {\n" +
                    "    border-collapse: collapse;\n" +
                    "    width: 100%;\n" +
                    "}\n" +
                    "\n" +
                    "th {\n" +
                    "    height: 50px;\n" +
                    "}\n" +
                    "\n" +
                    "tr:nth-child(even){background-color: #f2f2f2}\n" +
                    "\n" +
                    "th {\n" +
                    "    background-color: #4CAF50;\n" +
                    "    color: white;\n" +
                    "}\n" +
                    "\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1 align=\"center\">Component Health Check Report</h1>\n" +
                    "<p>Hi All,</p>\n"+
                    "<p>Please find below the Component Health Check Status</p>"+
                    "<p></p>"+
                    "<p></p>"+
                    "<br>"+
            "<h2>List Of Failed Component(s)</h2>"+
                    "<br>");
            if(data.size() > 0) {
                htmlStringBuilder.append("\n<table class=\"msoFix\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">" +
                        "\n<tr><th>Sl.No</th><th>Component Name</th><th>Component URL</th></tr></tr>\n");
                Iterator<Map.Entry<String, String>> i = data.entrySet().iterator();
                for (int iterator = 0; iterator < data.size(); ++iterator) {
                    Map.Entry<String, String> entry = i.next();
                    String serviceName = entry.getKey();
                    String serviceURL = entry.getValue();
                    int slNo = iterator + 1;
                    htmlStringBuilder.append("<tr><td align='center'>" + slNo + "</td><td>&nbsp;" + serviceName + "</td><td>&nbsp;" +serviceURL +"</td></tr>\n");
                }
            }
            else
            {
                htmlStringBuilder.append("<p>All the Component(s) are Up and Running ......</p>");
            }
            if(sucessList.size() > 0) {
                if(data.size() > 0) {htmlStringBuilder.append("</table>");}
                htmlStringBuilder.append("" +
                        "<p>&nbsp;</p>\n" +
                        "<h2>List Of Component(s) which are Up & Running</h2>\n"+
                        "<br>\n"+
                        "<table class=\"msoFix\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">" +
                        "\n"+
                        "<tr><th>Sl.No</th><th>Component Name</th><th>Environment</th><th>Component HealthCheck URL</th></tr>\n");
                Iterator<Map.Entry<String, String>> j = sucessList.entrySet().iterator();
                for (int iterator = 0; iterator < sucessList.size(); ++iterator) {
                    Map.Entry<String, String> entry = j.next();
                    String serviceName = entry.getKey();
                    String serviceURL = entry.getValue();
                    int slNo = iterator + 1;
                    htmlStringBuilder.append("<tr><td align='center'>" + slNo + "</td><td>&nbsp;" + serviceName + "</td><td>&nbsp;" +serviceURL + "</td></tr>\n");
                }
            }
            else
            {
                htmlStringBuilder.append("<p>All the Service(s) are Down ......</p>");
            }
            htmlStringBuilder.append("</table>\n" +
                    "<p>&nbsp;</p>\n"+
                    "<p>Thanks,</p>\n" +
                    "<p>Rakesh</p>\n"+
                    "</Div>"+
                    "</body>\n" +
                    "</html>");
            WriteToFile(htmlStringBuilder.toString(), "ServiceHealthCheckFailureRpt.html");
        }catch (Exception e){
            throw new Exception("ERROR: While Creating the HTML Report");
        }
    }*/

    public static void createHTMLReport(ArrayList<List<String>> data, ArrayList<List<String>> sucessList) throws Exception {
        try {
            StringBuilder htmlStringBuilder = new StringBuilder();
            htmlStringBuilder.append("<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    ".msoFix {\n" +
                    "      mso-table-lspace:-1pt;\n" +
                    "      mso-table-rspace:-1pt;\n" +
                    "   }"+
                    "table, th, td {\n" +
                    "    border: 1.5px solid Red;\n" +
                    "}\n" +
                    "\n" +
                    "table {\n" +
                    "    border-collapse: collapse;\n" +
                    "    width: 100%;\n" +
                    "}\n" +
                    "\n" +
                    "th {\n" +
                    "    height: 50px;\n" +
                    "}\n" +
                    "\n" +
                    "tr:nth-child(even){background-color: #f2f2f2}\n" +
                    "\n" +
                    "th {\n" +
                    "    background-color: #4CAF50;\n" +
                    "    color: white;\n" +
                    "}\n" +
                    "\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                   /* "<h1 align=\"center\">Component Health Check Report</h1>\n" +
                    "<p>Hi All,</p>\n"+
                    "<p>Please find below the Component Health Check Status</p>"+*/
                    "<p></p>"+
                    "<p></p>"+
                    "<br>"+
                    "<h2>List Of Failed Component(s)</h2>"+
                    "<br>");
            if(data.size() > 0) {
                htmlStringBuilder.append("\n<table class=\"msoFix\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">" +
                        "\n<tr><th>Sl.No</th><th>Component Name</th><th>Environment</th><th>Component URL</th></tr></tr>\n");

                for (int iterator = 0; iterator < data.size(); ++iterator) {
                    String serviceName = data.get(iterator).get(0);
                    String environment = data.get(iterator).get(1);
                    String serviceURL = data.get(iterator).get(2);
                    int slNo = iterator + 1;
                    htmlStringBuilder.append("<tr><td align='center'>" + slNo + "</td><td>&nbsp;" + serviceName + "</td><td>&nbsp;" + environment + "</td><td>&nbsp;" +serviceURL +"</td></tr>\n");
                }
            }
            else if(sucessList.size() > 0)
            {
                htmlStringBuilder.append("<p>All the Component(s) are Up and Running ......</p>");
            }
            if(sucessList.size() > 0) {
                /*if(data.size() > 0) {htmlStringBuilder.append("</table>");}
                htmlStringBuilder.append("" +
                        "<p>&nbsp;</p>\n" +
                        "<h2>List Of Component(s) which are Up & Running</h2>\n"+
                        "<br>\n"+
                        "<table class=\"msoFix\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">" +
                        "\n"+
                        "<tr><th>Sl.No</th><th>Component Name</th><th>Environment</th><th>Component HealthCheck URL</th></tr>\n");
                for (int iterator = 0; iterator < sucessList.size(); ++iterator) {
                    String serviceName = sucessList.get(iterator).get(0);
                    String environment = sucessList.get(iterator).get(1);
                    String serviceURL = sucessList.get(iterator).get(2);
                    int slNo = iterator + 1;
                    htmlStringBuilder.append("<tr><td align='center'>" + slNo + "</td><td>&nbsp;" + serviceName + "</td><td>&nbsp;" + environment + "</td><td>&nbsp;" + serviceURL + "</td></tr>\n");
                }*/ //Commented for email filtering
            }
            else
            {
                htmlStringBuilder.append("<p>All the Service(s) are Down ......</p>");
            }
/*            htmlStringBuilder.append("</table>\n" +
                    "<p>&nbsp;</p>\n"+
                    "<p>Thanks,</p>\n" +
                    "<p>Rakesh</p>\n"+
                    "</Div>"+
                    "</body>\n" +
                    "</html>");*/
            FileHelper.WriteToFile(htmlStringBuilder.toString(), "ServiceHealthCheckFailureRpt.html");
        }catch (Exception e){
            throw new Exception("ERROR: While Creating the HTML Report");
        }
    }


    public static void writeFailedServerName(ArrayList<List<String>> data) throws Exception {
        try{
            if(data.size() > 0) {
                String serverName = null;
                for (int iterator = 0; iterator < data.size(); ++iterator) {
                    if (iterator != 0) {
                        serverName = serverName + ", " + data.get(iterator).get(0)+ " URL:"+data.get(iterator).get(2);
                    } else {
                        serverName = "SERVERNAME="+data.get(iterator).get(0) + " URL:"+data.get(iterator).get(2);
                    }
                }
                FileHelper.WriteToFile(serverName, "serverList.txt");
            }
        }catch (Exception e){
            throw new Exception("");
        }
    }
}
