package net.thucydides.ebi.cucumber.framework.report;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class MergeReports {
    public static final String FILE_PATH = "target/cucumber-report/";
    public List<String> mergedFileList;
	static Object jsonObjectVal = null;

    public List<String> getJsonFiles(String directoryPath) {
        List<String> jsonFiles = new ArrayList<String>();
        File directory = new File(directoryPath);
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && file.getName().contains(".json")) {
                    jsonFiles.add(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    jsonFiles.addAll(getJsonFiles(file.getAbsolutePath()));
                }
            }
        } else {
            System.out.println("There are no json reports available");
        }
        return jsonFiles;
    }


    public void mergeFiles(List<String> jsonFiles) throws Exception {
        mergedFileList = new ArrayList<String>();
        for (String jsonFile : jsonFiles) {
            if (!checkIfMerged(jsonFile)) {
                String featureId = getJsonFeatureId(jsonFile);
                JSONObject jsonMainFile = getJsonObject(jsonFile);
                for (String mergingFile : jsonFiles) {
                    if (!checkIfMerged(mergingFile) && !jsonFile.equalsIgnoreCase(mergingFile) && (featureId.equals(getJsonFeatureId(mergingFile)))) {
                        JSONObject jsonSubFile = getJsonObject(mergingFile);
                        jsonMainFile = merge(jsonMainFile, jsonSubFile);
                        deleteFile(mergingFile);
                        mergedFileList.add(mergingFile);
                    }
                }
                mergedFileList.add(jsonFile);

                saveFile(createFileName(featureId), jsonMainFile);
                deleteFile(jsonFile);
            }
        }
    }

    public String createFileName(String featureID) {
        if (featureID != null && featureID.length() > 100) {
            return featureID.substring(0, 100);
        }
        return featureID;
    }

    public boolean checkIfMerged(String fileName) {
        if (mergedFileList.size() != 0) {
            for (String mergedFile : mergedFileList) {
                if (fileName.equalsIgnoreCase(mergedFile)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void saveFile(String name, JSONObject jsonObject) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = "[" + gson.toJson(jsonObject) + "]";
        System.out.println(jsonOutput);
        PrintWriter writer = new PrintWriter(FILE_PATH + name + ".json");
        writer.println(jsonOutput);
        writer.close();
    }

    public JSONObject merge(JSONObject mainFile, JSONObject subFile) throws Exception {
        JSONArray elements = (JSONArray) subFile.get("elements");
        JSONArray mainElements = (JSONArray) mainFile.get("elements");
        for (int i = 0; i < elements.size(); i++) {
            mainElements.add(elements.get(i));
        }
        mainFile.put("elements", mainElements);
        return mainFile;
    }

    public void deleteFile(String filePath) {
        try {

            File file = new File(filePath);
            file.delete();

        } catch (Exception e) {
            System.out.println(String.format("Failed to delete %s after merging", filePath));
        }
    }

    public String getJsonFeatureId(String filePath) throws Exception {
        return (String) getJsonObject(filePath).get("id");
    }


    public JSONObject getJsonObject(String filePath) throws Exception {
        FileReader reader = null;
        JSONObject jsonObject = null;
        try {
            reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
//            try {
//                jsonObject = (JSONObject) jsonParser.parse(reader);
//            } catch (Exception ce) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            jsonObject = (JSONObject) jsonArray.get(0);
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return jsonObject;
    }

//    public static void main(String a[]) throws Exception {
//        MergeReports mergeReports = new MergeReports();
//        List<String> jsonFiles = mergeReports.getJsonFiles(filePath);
//        mergeReports.mergeFiles(jsonFiles);
//
//    }

    public void buildMergedReport() throws Exception {
        List<String> jsonFiles = getJsonFiles(FILE_PATH);
        mergeFiles(jsonFiles);
    }
    
    public static Object getValueFromJSONString(JSONObject json , String key) {
		if (null != json)  {
			if (json.containsKey(key)) {
				jsonObjectVal = json.get(key);
			}
			else {
				for (Object jsonKey : json.keySet()) {
					if (json.get(jsonKey) instanceof JSONObject) {
						getValueFromJSONString(((JSONObject) json.get(jsonKey)) , key);
					}
				}
			}
		}
		return jsonObjectVal;
	}
	
	@SuppressWarnings("unchecked")
	public static void putValueInJSONString(JSONObject json , String key , Object objectVal) {
		if (null != json)  {
			if (json.containsKey(key)) {
				json.put(key, objectVal);
			}
			else {
				for (Object jsonKey : json.keySet()) {
					if (json.get(jsonKey) instanceof JSONObject) {
						putValueInJSONString(((JSONObject) json.get(jsonKey)) , key , objectVal);
					}
				}
			}
		}
	}
}
