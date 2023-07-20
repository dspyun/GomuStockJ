package main.java.com.gomu.gomustock.format;

public class FormatOHLCV {
    // 아래 정보를 엑셀에 저장한다
    public String date; // basDt 날짜
    public String open; // mkp 시가
    public String high; // hipr 고가
    public String low; // lopr 저가
    public String close; // clpr 종가
    public String adjclose; // clpr 종가
    public String volume; // trqu 거래량

    public FormatOHLCV() {
        date="";
        open="";
        high = "";
        low="";
        close="";
        adjclose="";
        volume="";
    }
    public void set(String date1, String open1, String high1, String low1, String close1, String volume1) {
        date = date1;
        open = open1;
        high = high1;
        low = low1;
        close =close1;
        adjclose =close1;
        volume = volume1;
    }
    public void yf_set(String date1, String open1, String high1, String low1, String close1, String close2,String volume1) {
        date = date1;
        open = open1;
        high = high1;
        low = low1;
        close =close1;
        adjclose =close2;
        volume = volume1;
    }
    public void setheader() {
        date="DATE";
        open="OPEN";
        high="HIGH";
        low="LOW";
        close="CLOSE";
        adjclose="ADJCLOSE";
        volume="VOLUME";
    }
    public void yf_setheader() {
        date="DATE";
        open="OPEN";
        high="HIGH";
        low="LOW";
        close="CLOSE";
        adjclose="ADJ_CLOSE";
        volume="VOLUME";
    }
}
