package net.thucydides.ebi.cucumber.framework.helpers;

import net.thucydides.ebi.cucumber.framework.hooks.ScenarioHook;
import org.apache.commons.io.FileUtils;

import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SoapServiceHelper {
    private static SOAPMessage soapResponse;
    private static SOAPMessage soapMessageObj;
    private static HttpURLConnection connection = null;
    private static URL soapURL = null;
    public static SOAPConnection intialSetup() throws UnsupportedOperationException, SOAPException {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        return soapConnection;
    }

    public static SOAPMessage triggerSoapMessage(String request, URL url, String actionName) throws Exception {
        try {
            SOAPConnection con = intialSetup();
            ScenarioHook.getScenario().write(url.toString());
            soapResponse = con.call(getSoapMessageFromString(request, actionName), url);
            ScenarioHook.getScenario().write("SOAP RESPONSE OBTAINED ");
            ScenarioHook.getScenario().write(soapResponse.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return soapResponse;
    }

    private static SOAPMessage getSoapMessageFromString(String xml, String actionName) throws Exception {
        MessageFactory factory = MessageFactory.newInstance();
        soapMessageObj = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        addSOAPAction(actionName);
        return soapMessageObj;
    }

    public static SOAPMessage addSOAPAction(String actionName) throws  Exception{
        MimeHeaders actionHeader = soapMessageObj.getMimeHeaders();
        actionHeader.addHeader("SOAPAction", actionName);
        return soapMessageObj;
    }

    public static URL createcConnection(String inputURL) {
        try {
            soapURL = new URL(inputURL);
            return soapURL;
        } catch (MalformedURLException e) {
            System.out.println("URL passed is not correct");
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean connectService() {

        try {
            connection = (HttpURLConnection) soapURL.openConnection();
            //connection.setRequestMethod("GET");
            connection.setRequestMethod("HEAD");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            // connection.setRequestProperty("Accept", "connect");

//            connection.connect();
//            System.out.println("ResponseCode1:" + connection.getResponseCode());
            InputStream inputStream = null;
            try {
                System.out.println("Connecting1:");
                inputStream = connection.getInputStream();
                System.out.println("GetContentStream2:"+inputStream);
            } catch (IOException exception) {
                inputStream = connection.getErrorStream();
                System.out.println("ErrorContentStream3:" + inputStream);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("------------"+result.toString());
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("CatchBlock:");
            e.printStackTrace();
            return false;
        }

    }

    public static Boolean checkServiceResponse() {
        try {
            System.out.println("ResponseCode2:" + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                System.out.println("Successfully connected to the service");
                connection.disconnect();
                return true;

            } else {
                System.out.println("Could not connect to the service, ResponseCode2:" + connection.getResponseCode());


                return false;
            }
        } catch (SocketTimeoutException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println("Read timeout exception");
            connection.disconnect();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println("I/O Exception occured while connecting to service");
            connection.disconnect();
            return false;
        }
    }

    public static boolean verifyServiceAvailability(String endPoint){
        if(ScenarioHook.getScenario()!=null) {
            ScenarioHook.getScenario().write("WSDL - " + endPoint);
        }
        createcConnection(endPoint);
        connectService();
        if(checkServiceResponse()){
            return true;
        }
        else {
            return false;
        }
    }

    public static Map<String,String> readEndpointWSDL(String sheetName,String operationName) throws Exception{

        Map<String,String> serviceDetails = new HashMap<String,String>();
        serviceDetails.put("Endpoint",ExcelHelper.ReadExcelColumnData(sheetName,operationName,"Endpoint"));
        serviceDetails.put("WSDL",ExcelHelper.ReadExcelColumnData(sheetName,operationName,"WSDL"));

        return serviceDetails;
    }

    public static boolean verifyServerPortAvailability(String host, int port) {
        boolean result = false;
        try {
            (new Socket(host, port)).close();
            result = true;
        }catch(SocketException e) {
            //ScenarioHook.getScenario().write("Count NOT connect to Host - "+host+" Port - "+port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        boolean xx = verifyServerPortAvailability("xx",6114);
        System.out.println("");
    }
}
