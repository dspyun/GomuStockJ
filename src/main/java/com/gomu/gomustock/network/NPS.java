package main.java.com.gomu.gomustock.network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class NPS {

    public void getNPSlist() throws IOException {

        Map<String, String> npsmap = new HashMap<>();
        final String urlPost = "https://comp.fnguide.com/SVO/WooriRenewal/inst.asp";
        //final String urlPost = "https://finance.naver.com/sise/sise_group_detail.naver?type=upjong&no=314";


        System.setProperty("webdriver.chrome.driver", "d:\\webdriver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        options.addArguments("headless"); // 크롬을 열지 않고 실행

        WebDriver driver = new ChromeDriver(options);
        driver.get(urlPost);

        /*
        System.setProperty("webdriver.gecko.driver", "d:\\webdriver\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        options.addArguments("--headless"); // 크롬을 열지 않고 실행
        options.addArguments("--disable-default-apps"); // default app으로 사용하지 않음
        WebDriver driver = new FirefoxDriver(options);
        driver.get(urlPost);

        */
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // 6. 조회, 로드될 때까지 최대 10초 대기
        //WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            Thread.sleep(2000); // 테스트용 대기 시간 2초
            //driver.wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // getPageSource : 위에서 한 번 불러주고, 아래처럼 다시 한번 불러 주어야야 한다
        String webpage = driver.getPageSource();
        //System.out.println( driver.getPageSource());

        Document doc = Jsoup.parseBodyFragment(webpage);
        Elements tbodylist =doc.select("tbody");
        Elements trlist = tbodylist.get(0).getElementsByTag("tr");
        String selected = tbodylist.toString();

        int size = trlist.size();
        if(size > 0) {
            for (int i = 0; i < size; i++) {

                Elements tdlist = trlist.get(i).getElementsByTag("td");
                String stock_name = tdlist.get(1).text();
                String buytime = tdlist.get(6).text();
                npsmap.put(buytime, stock_name);
            }
            ArrayList<String> keyList = new ArrayList<>(npsmap.keySet());
            //keyList.sort((s1, s2) -> s1.compareTo(s2));
            Collections.sort(keyList, Collections.reverseOrder());

            for (String key : keyList) {
                System.out.println("key: " + key + ", value: " + npsmap.get(key));
            }
        } else {
            System.out.println( driver.toString());
        }
    }
}
