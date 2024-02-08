package com.privacysociety_updater.flash;

import com.privacysociety_updater.controller.MainWindowController;
import com.privacysociety_updater.data.Variants;

public class Flash {
    Variants.Variant variant = null;
    String lineageVariant = null;

    public Flash(Variants.Variant variant)
    {
        this.variant = variant;
        lineageVariant = Variants.getLineageMapping(variant);
    }

    public boolean flash() {
        return true;
    }
}
