package main.java.com.gomu.gomustock;

public class MyUtil {

    public MyUtil() {

    }

    String PATH = "D:\\gomustockj\\";
    public void shell_copy() {

        Runtime runtime = Runtime.getRuntime();
        Process process;
        String res = "-0-";
        try {
            String cmd = "cp " + PATH + "/download/*.csv" + " " + PATH + "/gomustock/";
            process = runtime.exec(cmd);
            /*
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line ;
            while ((line = br.readLine()) != null) {
                Log.i("test",line);
            }
            */
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

}
