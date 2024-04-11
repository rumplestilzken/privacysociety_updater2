package com.privacysociety_updater.handler;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class FileHandler {
    public static boolean downloadOverHTTPS(String url, String downloadLocation) {
        boolean returnValue = false;
        try (FileOutputStream out = new FileOutputStream(downloadLocation);){
            ReadableByteChannel channel = Channels.newChannel(new URL(url).openStream());
            FileChannel fileChannel = out.getChannel();
            fileChannel.transferFrom(channel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return returnValue;
    }

    public static String fileToString(String url) {
        try {
            URL actualURL = new URL(url);
            return new String(actualURL.openStream().readAllBytes());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
