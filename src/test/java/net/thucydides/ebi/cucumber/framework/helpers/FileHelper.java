package net.thucydides.ebi.cucumber.framework.helpers;

import java.io.*;

/**
 * Created by rakeshnbr on 25/05/2017.
 */
public class FileHelper {

    public static File[] getFilesInDirectory(String folder, String ext) {
        File[] txts = new File(folder).listFiles(
                (dir, name) -> {
                    return name.toLowerCase().endsWith(ext);
                }
        );
        return txts;
    }

    public static void main(String[] args) {
        File[] files  = getFilesInDirectory("target/site/serenity",".png");
        System.out.println("");
        for(File file: files){
            if (file.isFile() & (!file.toString().contains("scaled_"))) {
                System.out.println(file.toString());
            }
        }
    }

    public static void WriteToFile(String fileContent, String fileName) throws Exception {
        try {
            String projectPath = System.getProperty("user.dir") + "\\target";
            String tempFile = projectPath + File.separator + fileName;
            File file = new File(tempFile);
            OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
            Writer writer = new OutputStreamWriter(outputStream);
            writer.write(fileContent);
            writer.close();
        }catch (Exception e){
            throw new Exception("ERROR: While Writing the HTML File");
        }
    }
}
