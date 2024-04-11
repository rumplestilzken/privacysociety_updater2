package com.privacysociety_updater.flash;

import com.privacysociety_updater.Main;
import com.privacysociety_updater.controller.MainWindowController;
import com.privacysociety_updater.data.Variants;
import com.privacysociety_updater.handler.FileHandler;
import com.privacysociety_updater.handler.ZipHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class Flash {
    Variants.Variant variant = null;
    String lineageVariant = null;
    String jsonUrl = "";

    public Flash(Variants.Variant variant, String jsonUrl)
    {
        this.variant = variant;
        lineageVariant = Variants.getLineageMapping(variant);
        this.jsonUrl = jsonUrl;
    }

    public boolean flash()
    {
        MainWindowController.getProgressBar().setProgress(.10);
        prepareResources();

        MainWindowController.getProgressBar().setProgress(.20);
        downloadUpdate();
        MainWindowController.getProgressBar().setProgress(.50);

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

        Flasher flasher = Flasher.getFlasher();

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

        MainWindowController.getProgressBar().setProgress(.30);

        String extractedPath = ZipHandler.extractXZ(fullPath);

    }



    private void flashGSI(String partition) {
    }
}
