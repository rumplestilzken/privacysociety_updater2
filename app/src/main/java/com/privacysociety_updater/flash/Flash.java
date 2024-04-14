package com.privacysociety_updater.flash;

import com.privacysociety_updater.Main;
import com.privacysociety_updater.controller.MainWindowController;
import com.privacysociety_updater.data.Variants;
import com.privacysociety_updater.handler.FileHandler;
import com.privacysociety_updater.handler.ProcessHandler;
import com.privacysociety_updater.handler.StreamGobbler;
import com.privacysociety_updater.handler.ZipHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import javafx.concurrent.Task;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.privacysociety_updater.handler.ProcessHandler.processCommand;

public class Flash {
    Variants.Variant variant = null;
    String lineageVariant = null;
    String jsonUrl = "";
    Flasher flasher = null;
    String imageFileName = "";
    FlashTask<Void> task = null;

    public Task<Void> getTask() {
        return task;
    }

    public void setTask(FlashTask<Void> task) {
        this.task = task;
    }

    public Flash(Variants.Variant variant, String jsonUrl)
    {
        this.variant = variant;
        lineageVariant = Variants.getLineageMapping(variant);
        this.jsonUrl = jsonUrl;
    }

    private void updateProgress(double progress) {
        if (task != null)
        {
            task.setProgressBarProgress(progress);
        }
    }

    private void updateMessage(String message) {
        if(task != null) {
            task.setProgressMessage(message);
        }
    }

    public boolean flash()
    {
        updateProgress(.10);
        updateMessage("Preparing resources...");
        prepareResources();

        updateMessage("Downloading Update...");
        updateProgress(.20);
        downloadUpdate();
        updateProgress(.50);

        updateMessage("Flashing... Do Not disconnect device.");
        if(variant == Variants.Variant.Pixel5a) {
            flashGSI("system_a");
        }
        else {
            flashGSI("super");
        }

        return true;
    }

    private void prepareResources() {
        String here = System.getProperty("user.dir");

        new File(here + "/resources/").mkdir();

        flasher = Flasher.getFlasher();

        String url = "https://github.com/rumplestilzken/privacysociety_updater/releases/download/resources/" + flasher.getPlatformToolsFilename();
        String fullPath = here + "/resources/" + flasher.getPlatformToolsFilename();
        if(!new File(fullPath).exists())
        {
            FileHandler.downloadOverHTTPS(url, fullPath);
        }

        if(!new File(fullPath.replace(".zip", "")).exists()) {
            ZipHandler.unzipFileToFolder(fullPath, fullPath.replace(".zip", ""));
        }

        flasher.preparePost(fullPath);
    }

    private void downloadUpdate() {
        String here = System.getProperty("user.dir");
        String jsonContent = FileHandler.fileToString(jsonUrl);

        JSONObject jsonObject = new JSONObject(jsonContent);
        JSONArray variants = (JSONArray) jsonObject.get("variants");
        String variantUrl = "";
        for(int i = 0; i < variants.length(); i++) {
            JSONObject variantObject = (JSONObject)variants.get(i);
            String name = variantObject.getString("name");
            if(name.equals(lineageVariant)){
                variantUrl = variantObject.getString("url");
                break;
            }
        }

        if(variantUrl.isEmpty()) {
            //TODO: Show message
        }

        String[] split = variantUrl.split("/");
        String fileName = split[split.length-1];

        String fullPath = here + "/resources/" + fileName;
        File file = new File(fullPath);
        if(file.exists()) {
            file.delete();
        }

        FileHandler.downloadOverHTTPS(variantUrl, fullPath);

        updateProgress(.30);

        updateMessage("Extracting Update...");
        imageFileName = extractXZ(fullPath);
    }

    private String extractXZ(String path) {
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
                    updateProgress(.30);
                } else {
                    updateProgress(counter);
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

    private void flashGSI(String partition) {
        String here = System.getProperty("user.dir");
        String fullPath = here + "/resources/" +
                flasher.getPlatformToolsFilename().replace(".zip", "") +
                "/platform-tools/";


        String output = "";
        List<String> args = new ArrayList<>();
        args.add("reboot");
        args.add("fastboot");
        output = ProcessHandler.processCommand(fullPath + "/adb", args);
        updateMessage("Waiting for device...");

        args.clear();
        args.add("devices");
        output = ProcessHandler.processCommand(fullPath + "/fastboot", args);
        while(!output.toLowerCase().contains("fastboot"))
        {
            output = processCommand(fullPath + "/fastboot", args);
        }

        args.clear();
        args.add("flash");
        args.add(partition);
        args.add(imageFileName);
        updateMessage("Flashing device...");
        output = ProcessHandler.processCommand(fullPath + "/fastboot", args);
        updateProgress(.90);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        args.clear();
        args.add("reboot");
        output = ProcessHandler.processCommand(fullPath + "/fastboot", args);
    }


}
