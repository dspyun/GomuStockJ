package main.java.com.gomu.gomustock.format;

public class FormatAccount {

    public String stock_code;
    public String stock_name;
    public String qunatity;
    public String buyprice;
    public String targetprice;
    public String nowprice;
    public String memo_1;
    public String memo_2;
    public String memo_3;
    public String memo_4;


    public FormatAccount() {

    }

    public String getNowprice() {
        return nowprice;
    }

    public String getTargetprice() {
        return targetprice;
    }

    public String getMemo1() {
        return memo_1;
    }

    public String getMemo2() {
        return memo_2;
    }

    public String getMemo3() {
        return memo_3;
    }

    public String getMemo4() {
        return memo_4;
    }


}
