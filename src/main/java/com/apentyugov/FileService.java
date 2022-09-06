package com.apentyugov;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileService {

    public static final String USER_FOLDER = System.getProperty("user.home") + "/vdcom/";
    public static final String FILE_NAME = "out.txt";

    public static File prepareOutputFile() {
        File folder = new File(USER_FOLDER);
        if (!folder.exists())
            folder.mkdirs();

        File file = new File(USER_FOLDER + FILE_NAME);
        try {
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(0));
            fileWriter.close();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
