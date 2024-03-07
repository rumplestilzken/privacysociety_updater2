package com.privacysociety_updater.flash;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class LinuxFlasher extends Flasher {
    @Override
    public String getPlatformToolsFilename() {
        return "platform-tools_r34.0.4-linux.zip";
    }

    @Override
    public void preparePost(String fullPath) {
        super.preparePost(fullPath);
        fullPath = fullPath.replace(".zip", "");

        File file = new File(fullPath + "/platform-tools/adb");
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        try {
            Files.setPosixFilePermissions(Path.of(file.getPath()), perms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        file = new File(fullPath + "/platform-tools/fastboot");
        try {
            Files.setPosixFilePermissions(Path.of(file.getPath()), perms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
