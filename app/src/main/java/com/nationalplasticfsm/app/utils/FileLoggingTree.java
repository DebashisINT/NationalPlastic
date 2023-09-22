package com.nationalplasticfsm.app.utils;

import static android.content.Context.MODE_PRIVATE;

import static java.sql.DriverManager.println;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nationalplasticfsm.BuildConfig;
import com.nationalplasticfsm.app.Pref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;
/*Created by Saheli 02-03-2023*/
// modifed work for 03-03-2023 by saheli
public class FileLoggingTree extends Timber.DebugTree {

        private static final String LOG_TAG = FileLoggingTree.class.getSimpleName();
        public static Context context;

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
                try {
                        println("log_tag ");
                        String path = "Log";
                        String fileNameTimeStamp = new SimpleDateFormat("dd-MM-yyyy",
                                Locale.getDefault()).format(new Date());
                        String logTimeStamp = new SimpleDateFormat("E MMM dd yyyy 'at' hh:mm:ss:SSS aaa",
                                Locale.getDefault()).format(new Date());
                        //String fileName = fileNameTimeStamp + ".html";
                        String fileName = "Fsmlog" + ".html";

                        // Create file
//                        File file  = generateFile(path, fileName);
                        File file  = generateFile2(message, fileName,context);

                        // If file created or exists save logs
                        if (file != null) {
                                FileWriter writer = new FileWriter(file, true);
//                                writer.append("<p style=\"background:lightgray;\"><strong "
//                                                + "style=\"background:lightblue;\">&nbsp&nbsp")
//                                        .append(logTimeStamp)
//                                        .append(" :&nbsp&nbsp</strong><strong>&nbsp&nbsp")
//                                        .append(tag)
//                                        .append("</strong> - ")
//                                        .append(message)
//                                        .append("</p>");
                                //writer.append("<p style=<font-size='1' color='#337744'>");
                                writer.append("<p style=\"color:blue;font-size:12px;\">");
                                writer.append("\n"+logTimeStamp+" "+tag +" "+message);
                                writer.append("</font></p>");
                                writer.flush();
                                writer.close();
                        }
                } catch (Exception e) {
                        Log.e(LOG_TAG,"Error while logging into file : " + e);
                }
        }

        @Override
        protected String createStackElementTag(StackTraceElement element) {
                // Add log statements line number to the log
                return super.createStackElementTag(element) + " - " + element.getLineNumber();
        }

        /*  Helper method to create file*/
        @Nullable
        private static File generateFile(@NonNull String path, @NonNull String fileName) {
                // modifed work for 03-03-2023 by saheli
                File file = null;
                if (isExternalStorageAvailable()) {
//                        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),BuildConfig.APPLICATION_ID + File.separator + path);
                        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), BuildConfig.APPLICATION_ID + File.separator + path);

                        boolean dirExists = true;

                        if (!root.exists()) {
                                dirExists = root.mkdirs();
                        }
                        if (dirExists) {
                                file = new File(root, fileName);
                        }
                      /*  try{
                                if(Pref.INSTANCE.getUser_id()==null && Pref.INSTANCE.getUser_id()==""){
                                        fileDelete(file);
                                }
                        }catch (Exception ex){
                                ex.printStackTrace();
                        }*/

                }
              /*  if(isFileLessThan10MB(file)){
                        file.delete();
                }*/
                return file;
        }

        @Nullable
        private  File generateFile2(@NonNull String path, @NonNull String fileName,Context context) {
                // modifed work for 03-03-2023 by saheli
//                File file = null;
//                File file = new File(context.getFilesDir(), fileName);
//                File file = new File(Environment.getDataDirectory().getAbsoluteFile(), fileName);

//                File file = new File(context.getApplicationContext().getFilesDir(), fileName);

                File file = new File("/data/user/0/com.nationalplasticfsm/files", fileName);

                //get file size
                long sizeInBytes = file.length();
                long sizeInMb = sizeInBytes / (1024 * 1024); //transform in MB
                long sizeInKb = sizeInBytes / 1024 ; //transform in MB
                println("log_tag "+sizeInMb);
                //delete file
                if(sizeInMb>25){
                        file.delete();
                        file = new File("/data/user/0/com.nationalplasticfsm/files", fileName);
                }

                try {
                        if(context==null){
                                FileOutputStream fos = context.getApplicationContext().openFileOutput(fileName, MODE_PRIVATE);
                                fos.write(path.getBytes());
                        }
                        else{
                                FileOutputStream fos = context.openFileOutput(fileName, MODE_PRIVATE);
                                fos.write(path.getBytes());
                        }
                } catch (Exception e) {
                        e.printStackTrace();

                }
                return file;
        }



        /* Helper method to determine if external storage is available*/
        private static boolean isExternalStorageAvailable() {
                return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        }

        private static boolean isFileLessThan30MB(File file) {
                int maxFileSize = 10 * 1024 * 1024;
                Long l = file.length();
                String fileSize = l.toString();
                int finalFileSize = Integer.parseInt(fileSize);
                return finalFileSize >= maxFileSize;
        }

        public static boolean fileDelete(File file) {
                return file.delete();
        }

}
