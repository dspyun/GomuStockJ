package main.java.com.gomu.gomustock.stockengin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CPUID {

    String cputable[] = {"007E-5219","9C7B-74B0"};
    String CPUid;

    public CPUID(String letter) {
        CPUid = getSerialKey(letter);
    }

    public String getSerialKey(String letter) {

        String line = null;
        List<String> serialist = new ArrayList<>();
        Process process = null;

        try {
            process = Runtime.getRuntime().exec("cmd /c vol " + letter + ":");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            while ((line = in.readLine()) != null) {
                serialist.add(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = serialist.get(1).length();
        CPUid = serialist.get(1).substring(size-9, size);

        return CPUid;
    }

    public boolean checkID() {
        boolean flag=false;
        int size = cputable.length;
        for(int i =0;i<size;i++) {
            if(CPUid.equals(cputable[i])) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
