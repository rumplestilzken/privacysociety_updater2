package com.privacysociety_updater.handler;

import com.privacysociety_updater.controller.MainWindowController;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHandler {

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static boolean unzipFileToFolder(String file, String destination) {
        boolean returnValue = false;
        byte[] buffer = new byte[1024];
        try {
            ZipInputStream in = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry = in.getNextEntry();
            while(zipEntry != null) {
                File newFile = newFile(new File(destination), zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = in.getNextEntry();
            }
            returnValue = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return returnValue;
    }

    public static String extractXZ(String path) {
        String outputPath = path.replace(".xz", "");
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedInputStream bin = new BufferedInputStream(in);
            FileOutputStream out = new FileOutputStream(outputPath);
            XZCompressorInputStream xzin = new XZCompressorInputStream(bin);
            final byte[] buffer = new byte[8192];
            int readBytes = 0;
            double counter = .30;
            while((readBytes = xzin.read(buffer)) != -1) {
                out.write(buffer);
                counter += MainWindowController.getProgressBar().getProgress() + .01;
                if(MainWindowController.getProgressBar().getProgress() >= .40) {
                    MainWindowController.getProgressBar().setProgress(.30);
                } else {
                    MainWindowController.getProgressBar().setProgress(counter);
                }
            }
            out.close();
            xzin.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputPath;
    }
}
