package net.thucydides.ebi.cucumber.framework.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.thucydides.ebi.cucumber.framework.hooks.ScenarioHook;




public class RestServiceHelper<RequestClassName>{
    public static int responseCode = 0;
    private static RequestSpecification restHeader;
    public static Response response;
    public static ObjectMapper localObjectMapper;


    public static void checkServiceAvailability(String baseServiceURL){
        responseCode = CheckServiceAvailability.getRESTserviceResponse(baseServiceURL);
        if(responseCode!=200 && responseCode!=405){
            throw new AssertionError("Error : Verified the Service - "+baseServiceURL+ " and it is NOT up and running !"+" Error code : "+responseCode);
        }
        if(ScenarioHook.getScenario()== null){
                System.out.println("Endpoint : " + baseServiceURL);
        }else {
            ScenarioHook.getScenario().write("Endpoint : " + baseServiceURL);
        }
    }

    public static void checkServiceAvailability(String baseServiceURL, String userName, String password){
        responseCode = CheckServiceAvailability.getRESTserviceResponse(baseServiceURL,userName,password);
        if(responseCode != 200){
            throw new AssertionError("Error : Verified the Service - "+baseServiceURL+ " and it is NOT up and running !"+" Error code : "+responseCode);
        }
        if(ScenarioHook.getScenario()== null){
            System.out.println("Endpoint : " + baseServiceURL);
        }else {
            ScenarioHook.getScenario().write("Endpoint : " + baseServiceURL);
        }
    }



    public String postRestServiceRequest(RequestClassName request, String parentNode, String baseURL) throws Exception {
        String responseStr, strRequest;
        try {

            ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
            strRequest = objectMapper.writeValueAsString(request);
            if(!parentNode.isEmpty()) {
                strRequest = "{\"" + parentNode + "\": " + strRequest + " }";
            }
            if(ScenarioHook.getScenario()== null){
                restHeader.log().headers();
                System.out.println(strRequest);
            }else{
                ScenarioHook.getScenario().write("Request Header : "+restHeader.log().headers());
                String strRequest2=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
                ScenarioHook.getScenario().write("Request Posted : "+"{\"" + parentNode + "\": " + strRequest2 + " }");
            }
            response = restHeader.body(strRequest).when().post(baseURL);
            responseCode = response.getStatusCode();
            //response.then().contentType("application/json").appendRoot("Test");
            responseStr= response.asString();
            if(ScenarioHook.getScenario()== null){
                System.out.println("----------------------------Response----------------------------------------");
                System.out.println(response.getHeaders().toString());
                System.out.println(responseStr);
            }else{
                 ScenarioHook.getScenario().write("--------------------------------------------------------------------");
                ScenarioHook.getScenario().write("Response Received : "+response.prettyPrint());
            }
        }catch (Exception e){
            throw new Exception("ERROR: Posting the REST Request");
        }
        return responseStr;
    }

    public String getRestServiceResponseString(String baseURL) throws Exception {
        String responseStr, strRequest;
        try {
            if(localObjectMapper == null) {
                localObjectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
                localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                localObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                localObjectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(localObjectMapper.getTypeFactory()));
            }
            restHeader = RestAssured.given().header("Accept", "application/json");
            response = restHeader.when().get(baseURL);
            responseCode = response.getStatusCode();
            responseStr= response.asString();
            if(ScenarioHook.getScenario()== null){
/*                System.out.println("----------------------------Response----------------------------------------");
                System.out.println(response.getHeaders().toString());
                System.out.println(responseStr);*/
                ScenarioHook.writeToLog("----------------------------Response--------------------------------");
                ScenarioHook.writeToLog(responseStr);
            }else{
/*                ScenarioHook.getScenario().write("--------------------------------------------------------------------");
                ScenarioHook.getScenario().write("Response Received : "+response.prettyPrint());*/
                ScenarioHook.writeToLog("----------------------------Response--------------------------------");
                ScenarioHook.writeToLog("Response Received : "+response.prettyPrint());
            }
        }catch (Exception e){
            throw new Exception("ERROR: Posting the REST Request");
        }
        return responseStr;
    }

    public Response getRestServiceResponse(String baseURL) throws Exception {
        String responseStr, strRequest;
        try {

            ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
            restHeader = RestAssured.given().header("Accept", "application/json");
            response = restHeader.when().get(baseURL);
        }catch (Exception e){
            throw new Exception("ERROR: Posting the REST Request");
        }
        return response;
    }

    public String postRestServiceRequest(RequestClassName request, String parentNode, String baseURL, String authUserName, String authPwd) throws Exception {
        String responseStr, strRequest;
        try {

            ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
            strRequest = objectMapper.writeValueAsString(request);
            if(!parentNode.isEmpty()) {
                strRequest = "{\"" + parentNode + "\": " + strRequest + " }";
            }
            if(ScenarioHook.getScenario()== null){
                restHeader.log().headers();
                System.out.println(strRequest);
            }else{
                restHeader.log().headers();
                ScenarioHook.getScenario().write("Request Header : "+restHeader.log().headers());
                strRequest=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
                ScenarioHook.getScenario().write("Request Posted : "+strRequest);
            }


            response = restHeader.body(strRequest).when().authentication().preemptive().basic(authUserName,authPwd).post(baseURL);
            responseCode = response.getStatusCode();
            responseStr= response.asString();
            if(ScenarioHook.getScenario()== null){
                System.out.println("----------------------------Response----------------------------------------");
                System.out.println(response.getHeaders().toString());
                System.out.println(responseStr);
            }else{
                System.out.println(response.getHeaders().toString());
                ScenarioHook.getScenario().write("--------------------------------------------------------------------");
                ScenarioHook.getScenario().write("Response Received : "+response.prettyPrint());
            }

        }catch (Exception e){
            throw new Exception("ERROR: Posting the REST Request");
        }
        return responseStr;
    }

    public String postRestServiceRequestNullFields(RequestClassName request, String parentNode, String baseURL) throws Exception {
        String responseStr, strRequest;
        try {

            ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,true);
            //    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
            strRequest = objectMapper.writeValueAsString(request);
            if(!parentNode.isEmpty()) {
                strRequest = "{\"" + parentNode + "\": " + strRequest + " }";
            }
            if(ScenarioHook.getScenario()== null){
                restHeader.log().headers();
                System.out.println(strRequest);
            }else{
                restHeader.log().headers();
                ScenarioHook.getScenario().write("Request Posted : "+strRequest+"<br>");
            }


            response = restHeader.body(strRequest).when().post(baseURL);
            responseCode = response.getStatusCode();
            responseStr= response.asString();
            if(ScenarioHook.getScenario()== null){
                System.out.println("----------------------------Response----------------------------------------");
                System.out.println(response.getHeaders().toString());
                System.out.println(responseStr);
            }else{
                System.out.println(response.getHeaders().toString());
                ScenarioHook.getScenario().write("--------------------------------------------------------------------"+"<br>");
                ScenarioHook.getScenario().write("Response Received : "+responseStr);
            }

        }catch (Exception e){
            throw new Exception("ERROR: Posting the REST Request");
        }
        return responseStr;
    }


       public String postRestServiceRequestwithNoAnnotation(RequestClassName request, String parentNode, String baseURL) throws Exception {
        String responseStr, strRequest;
        try {

            ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,false);
            //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
            strRequest = objectMapper.writeValueAsString(request);
            if(!parentNode.isEmpty()) {
                strRequest = "{\"" + parentNode + "\": " + strRequest + " }";
            }
            if(ScenarioHook.getScenario()== null){
                restHeader.log().headers();
                System.out.println(strRequest);
            }else{
                restHeader.log().headers();
                strRequest=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
                ScenarioHook.getScenario().write("Request Posted : "+strRequest);
            }


            response = restHeader.body(strRequest).when().post(baseURL);
            responseCode = response.getStatusCode();
            //response.then().contentType("application/json").appendRoot("Test");
            responseStr= response.asString();
            if(ScenarioHook.getScenario()== null){
                System.out.println("----------------------------Response----------------------------------------");
                System.out.println(response.getHeaders().toString());
                System.out.println(responseStr);
            }else{
                System.out.println(response.getHeaders().toString());
                ScenarioHook.getScenario().write("--------------------------------------------------------------------");
                ScenarioHook.getScenario().write("Response Received : "+response.prettyPrint());
            }

        }catch (Exception e){
            throw new Exception("ERROR: Posting the REST Request");
        }
        return responseStr;
    }

      public String postParentNodeRestServiceRequest(RequestClassName request, String parentNode, String baseURL) throws Exception {
        String responseStr, strRequest,strRequest2;
        try {

            ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
            strRequest = objectMapper.writeValueAsString(request);
            if(!parentNode.isEmpty()) {
                strRequest = "{\"" + parentNode + "\": " + strRequest + " }";
            }
            if(ScenarioHook.getScenario()== null){
                restHeader.log().headers();
                System.out.println(strRequest);
            }else{
                restHeader.log().headers();
                ScenarioHook.getScenario().write("Request Header : "+restHeader.log().headers());
                strRequest2=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
                ScenarioHook.getScenario().write("Request Posted : "+"{\"" + parentNode + "\": " + strRequest + " }");
            }

            System.out.println("JSON Request:"+strRequest);
            response = restHeader.body(strRequest).when().post(baseURL);
            responseCode = response.getStatusCode();
            //response.then().contentType("application/json").appendRoot("Test");
            responseStr= response.asString();
            if(ScenarioHook.getScenario()== null){
                System.out.println("----------------------------Response----------------------------------------");
                System.out.println(response.getHeaders().toString());
                System.out.println(responseStr);
            }else{
                System.out.println(response.getHeaders().toString());
                ScenarioHook.getScenario().write("--------------------------------------------------------------------");
                ScenarioHook.getScenario().write("Response Received : "+response.prettyPrint());
            }

        }catch (Exception e){
            throw new Exception("ERROR: Posting the REST Request");
        }
        return responseStr;
    }

    public void printRestResponse(){
        try{
            ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
            ScenarioHook.getScenario().write("Response Received : "+response.prettyPrint());
        }catch (Exception e){
            //Do nothing
        }
    }

}
