package com.privacysociety_updater.flash;

import com.privacysociety_updater.data.Variants;

public class Flasher {
    public static Flasher getFlasher() {
        Variants.OS os = Variants.getOS();
        switch (os) {
            case Linux:
                return new LinuxFlasher();
            case Windows:
                return new WindowsFlasher();
        }
        return null;
    }

    public String getPlatformToolsFilename() {
        return "";
    }

    public void preparePost(String fullPath) {

    }
}
