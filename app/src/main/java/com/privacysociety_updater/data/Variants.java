package com.privacysociety_updater.data;

import java.util.Dictionary;
import java.util.Hashtable;

public class Variants {
    public enum Variant {
        Pixel5a {
            @Override
            public String toString() {
                return "Pixel 5a";
            }
        },
        TitanPocket {
            @Override
            public String toString() {
                return "Titan Pocket";
            }
        },
        Jelly2E {
            @Override
            public String toString() {
                return "Jelly 2E";
            }
        };
    }

    public static String getLineageMapping(Variant variant) {
        Dictionary<Variant, String> dict = new Hashtable<>();
        dict.put(Variant.Pixel5a, "lineage_privacysociety_pixel5a");
        dict.put(Variant.TitanPocket, "lineage_privacysociety_pocket");
        dict.put(Variant.Jelly2E, "lineage_privacysociety_jelly2e");
        return dict.get(variant);
    }
}
