package vn.com.zinza.zinzamessenger.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;

import vn.com.zinza.zinzamessenger.model.FileHistory;


public class FileUtils {

    public FileUtils() {
    }
    public ArrayList<FileHistory> getApkFileList(Activity context) {
        //l?y list files apk
        ArrayList<FileHistory> apkfileList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = "_data like ?"; //truy v?n : where "_data" like '.apk%' -> l?y ra dc c�c file du�i apk
        String[] args = new String[]{"%.apk%"};
        Cursor apkCursor = cr.query(uri, null, selection, args, null);

        final PackageManager pm = context.getPackageManager();
        while (apkCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String pathFile = apkCursor.getString(1);
            fileInfo.setName(pathFile.substring(pathFile.lastIndexOf("/") + 1));
            fileInfo.setSize(apkCursor.getLong(2) + "");

            apkfileList.add(fileInfo);
        }

        apkCursor.close();

        return apkfileList;
    }

    public ArrayList<FileHistory> getPdfFileList(Context context) {
        if (context == null) {
            return new ArrayList<>();
        }
        //tr? v? list file van b?n
        ArrayList<FileHistory> fileList = new ArrayList<FileHistory>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {};
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"; // truy v?n l?a ch?n theo mimeType c?a file
        //l?y mimeType c?a c�c lo?i file pdf , doc , html,....
        String pdfType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String[] selectionPdf = new String[]{pdfType};
        Cursor fileCursor = cr.query(uri, projection, selection, selectionPdf, null);
        while (fileCursor.moveToNext()) {
            // th�m ph?n t? new Files , tham s? truy?n : uri , size
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");
            fileList.add(fileInfo);
        }
        fileCursor.close();

        return fileList;
    }

    public ArrayList<FileHistory> getTextListFile(Context context) {
        if (context == null) {
            return new ArrayList<>();
        }

        //tr? v? list file van b?n
        ArrayList<FileHistory> fileList = new ArrayList<FileHistory>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");

//        Uri uri1 = MediaStore.Files.getContentUri("internal"); zzzzzzzzzzzzzzz

        String[] projection = {};
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"; // truy v?n l?a ch?n theo mimeType c?a file
        //l?y mimeType c?a các lo?i file pdf , doc , html,....
        String pdfType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String docType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
        String htmlType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");
        String docxType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
        String exelType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
        String exelxType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
        String pptType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
        String pptxType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
        String[] selectionPdf = new String[]{pdfType};
        String[] selectionDoc = new String[]{docType};
        String[] selectionHtml = new String[]{htmlType};
        String[] selectionDocx = new String[]{docxType};
        String[] selectionExel = new String[]{exelType};
        String[] selectionExelX = new String[]{exelxType};
        String[] selectionPPX = new String[]{pptxType};
        String[] selectionPP = new String[]{pptType};

        // take Doc file
        Cursor fileCursor = cr.query(uri, projection, selection, selectionDoc, null);

        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");
            fileList.add(fileInfo);
        }

        fileCursor.close();

        // take Html file
        fileCursor = cr.query(uri, projection, selection, selectionHtml, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            fileList.add(fileInfo);
        }

        fileCursor.close();

        // take DocX file
        fileCursor = cr.query(uri, projection, selection, selectionDocx, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            fileList.add(fileInfo);
        }

        fileCursor.close();

        // take Exel file
        fileCursor = cr.query(uri, projection, selection, selectionExel, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            fileList.add(fileInfo);
        }

        fileCursor.close();

        // take PP file
        fileCursor = cr.query(uri, projection, selection, selectionPP, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            fileList.add(fileInfo);
        }

        fileCursor.close();

        // take PPX file
        fileCursor = cr.query(uri, projection, selection, selectionPPX, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            fileList.add(fileInfo);
        }

        fileCursor.close();

        // take ExelX file
        fileCursor = cr.query(uri, projection, selection, selectionExelX, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            fileList.add(fileInfo);
        }
        fileCursor.close();

        // get pdf file
        fileCursor = cr.query(uri, projection, selection, selectionPdf, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");
            fileList.add(fileInfo);
        }
        fileCursor.close();

        return fileList;
    }

    public ArrayList<FileHistory> getRarFileList(Context context) {


        if (context == null) {
            return new ArrayList<>();
        }

        // tr? v? list file zip
        ArrayList<FileHistory> rarfileList = new ArrayList<FileHistory>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {};
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"; //c�u l?nh lua ch?n mimeType

        //l?y mimeType file zip , rar
        String zipType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("zip");
        String rarType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rar");
        //String Seven7ZType = "application/x-7z-compressed";
        String TARType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("tar");

        String[] selectionZip = new String[]{zipType};
        String[] selectionRar = new String[]{rarType};
        //String[] selection7Z = new String[]{Seven7ZType};
        String[] selectionTAR = new String[]{TARType};

        Cursor fileCursor = cr.query(uri, projection, selection, selectionZip, null);

        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");


            if (path.toLowerCase().endsWith("zip")) {
                rarfileList.add(fileInfo);
            }

        }

        fileCursor.close();

        fileCursor = cr.query(uri, projection, selection, selectionRar, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            if (path.toLowerCase().endsWith("rar")) {
                rarfileList.add(fileInfo);
            }

        }
        fileCursor.close();

        fileCursor = cr.query(uri, projection, selection, selectionTAR, null);
        while (fileCursor.moveToNext()) {
            FileHistory fileInfo = new FileHistory();
            String path = fileCursor.getString(1);
            fileInfo.setPathFileInStorage(path);
            fileInfo.setName(path.substring(path.lastIndexOf("/") + 1));
            fileInfo.setSize(fileCursor.getLong(2) + "");

            if (path.toLowerCase().endsWith("tar")) {
                rarfileList.add(fileInfo);
            }

        }
        fileCursor.close();


        return rarfileList;
    }
}
