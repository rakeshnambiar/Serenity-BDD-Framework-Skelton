package net.thucydides.ebi.cucumber.framework.helpers;


import io.restassured.response.Response;
import net.thucydides.ebi.cucumber.framework.hooks.ScenarioHook;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import static io.restassured.RestAssured.given;


public class CheckServiceAvailability {

	private static HttpURLConnection connection = null;
	private static URL soapURL = null;

	public static void main(String[] args) {

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
			connection.setRequestProperty("Accept", "connect");
			connection.setConnectTimeout(20000);
			connection.setReadTimeout(20000);

			connection.connect();
			return true;
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static Boolean checkServiceResponse() {
		try {
			System.out.println(connection.getResponseCode());
			if (connection.getResponseCode() == 200) {
				System.out.println("Successfully connected to the service");
				connection.disconnect();
				return true;

			} else {
				System.out.println("Could not connect to the service");
				return false;
			}
		} catch (SocketTimeoutException e) {
			//  Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Read timeout exception");
			connection.disconnect();
			return false;
		} catch (IOException e) {
			//  Auto-generated catch block
			// e.printStackTrace();
			System.out.println("I/O Exception occured while connecting to service");
			connection.disconnect();
			return false;
		}
	}

	public static int getRESTserviceResponse(String ServiceName){
		boolean stateful = false;
		int ResponseCode = 0;
		try {
			//java.lang.System.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			Response response = given().relaxedHTTPSValidation("TLSv1").when().post(ServiceName).then().extract().response();
/*					given().post(ServiceName)
					.then().extract().response();*/
			if(response.getStatusCode()!=200 && response.getStatusCode()!=405){
				response =
						given().get(ServiceName)
								.then().extract().response();
			}
			ResponseCode = response.getStatusCode();
		}catch (Exception e){
			e.printStackTrace();
			ScenarioHook.getScenario().write("I/O Exception occured while connecting to service");
		}
		return ResponseCode;
	}

	public static int getRESTserviceResponse(String ServiceName, String userName, String password){
		boolean stateful = false;
		int ResponseCode = 0;
		try {
			//java.lang.System.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			Response response = given().relaxedHTTPSValidation("TLSv1").when().authentication().preemptive().basic(userName,password)
					.post(ServiceName).then().extract().response();

			//response = given().relaxedHTTPSValidation("TLSv1").auth().basic(userName,password).expect().statusCode(200).when().get(ServiceName);

			if(response.getStatusCode()!=200 && response.getStatusCode()!=405){
				response =
						given().get(ServiceName)
								.then().extract().response();
			}
			ResponseCode = response.getStatusCode();
		}catch (Exception e){
			e.printStackTrace();
			ScenarioHook.getScenario().write("I/O Exception occured while connecting to service");
		}
		return ResponseCode;
	}
}
