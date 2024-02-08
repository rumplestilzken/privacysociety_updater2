package com.privacysociety_updater;

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
        }
    }
}
