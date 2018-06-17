package com.example.neha.myapplication.Utilities;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheUtil {


    public static void saveInfo(Context context, String info) {
        try {
            File folder = context.getExternalFilesDir("geniogames");// Folder Name
            File myFile = new File(folder, "mobile.txt");// Filename
            writeData(myFile, info);
        }catch(Exception e){

        }
    }

    private static void writeData(File myFile, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getInfo(Context context) {
        String text="";
        try {
            File folder = context.getExternalFilesDir("geniogames"); // Folder Name
            File myFile = new File(folder, "mobile.txt"); // Filename
            text = getdata(myFile);
        }catch (Exception e){

        }
        return text;
    }

    private static String getdata(File myfile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(myfile);
            int i = -1;
            StringBuffer buffer = new StringBuffer();
            while ((i = fileInputStream.read()) != -1) {
                buffer.append((char) i);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
