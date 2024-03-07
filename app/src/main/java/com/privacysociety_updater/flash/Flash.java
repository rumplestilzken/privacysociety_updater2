package com.privacysociety_updater.flash;

import com.privacysociety_updater.controller.MainWindowController;
import com.privacysociety_updater.data.Variants;
import com.privacysociety_updater.handler.FileHandler;
import com.privacysociety_updater.handler.ZipHandler;

import java.io.File;

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
    }

    private void flashGSI(String systemA) {
    }
}
