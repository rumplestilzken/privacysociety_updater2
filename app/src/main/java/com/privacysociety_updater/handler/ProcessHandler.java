package com.privacysociety_updater.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessHandler {
    public static String processCommand(String executablePath, List<String> args) {
        Process p = null;
        String fullOutput = "";
        try {
            ProcessBuilder pb = new ProcessBuilder();
            List<String> command = new ArrayList<>();
            command.add(executablePath);
            command.addAll(args);
            pb.command(command);
            p = pb.start();
//            p = Runtime.getRuntime().exec(executablePath + command);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            int exitCode = p.waitFor();System.out.println("ExitCode:" + exitCode);

            String output = "";
            System.out.println("Input:");
            while((output = in.readLine()) != null) {
                fullOutput += output + System.lineSeparator();
                System.out.println(output);
            }

            System.out.println("Error:");
            while((output = err.readLine()) != null) {
                fullOutput += output + System.lineSeparator();
                System.out.println(output);
            }
            in.close();
            err.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return fullOutput;
    }

    public static String processCommands(String[] cmd) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(cmd);

        int exitCode = process.waitFor();

        System.out.println("ExitCode:" + exitCode);

        String output = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String buff = "";
        while((buff = br.readLine()) != null) {
            output += buff;
        }
        while((buff = bre.readLine()) != null) {
            output += buff;
        }

        System.out.println(output);
        br.close();
        bre.close();
        return output;
    }
}
