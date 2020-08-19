package de.gerritstapper.casscheduler.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterUtil {

    public static void writeToFile(List<String> lines, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        
        for(String line: lines) {
            writeToFile(line + "\n", filename);
        }

        writer.close();
    }

    public static void writeToFile(String content, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        
        writer.write(content);

        writer.close();
    }
}