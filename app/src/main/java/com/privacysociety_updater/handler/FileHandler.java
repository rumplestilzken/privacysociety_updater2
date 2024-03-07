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
//        try(BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
//            FileOutputStream out = new FileOutputStream(downloadLocation);
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while((bytesRead = in.read(buffer, 0, 1024)) != -1) {
//                out.write(buffer);
//            }
//            out.close();
//            returnValue = true;
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try (FileOutputStream out = new FileOutputStream(downloadLocation);){
            ReadableByteChannel channel = Channels.newChannel(new URL(url).openStream());
            FileChannel fileChannel = out.getChannel();
            fileChannel.transferFrom(channel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return returnValue;
    }
}
