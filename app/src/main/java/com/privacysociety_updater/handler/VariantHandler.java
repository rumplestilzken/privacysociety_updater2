package com.privacysociety_updater.handler;

import com.privacysociety_updater.data.Variants;

public class VariantHandler {
    public static Variants.Variant getVariant() {
        return Variant;
    }

    public static void setVariant(Variants.Variant variant) {
        Variant = variant;
    }

    public static Variants.Variant Variant = null;

}
