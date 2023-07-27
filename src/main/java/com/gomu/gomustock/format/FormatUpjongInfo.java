package main.java.com.gomu.gomustock.format;

import java.util.ArrayList;
import java.util.List;

public class FormatUpjongInfo {

    public String stock_code;
    public String stock_name;
    public String market_type;
    public String cur_price;
    public String compare_yester;
    public String updown_ratio;
    public String tran_quantity;
    public String callbuy;
    public String tran_money;
    public String market_volume;
    public String profit;
    public String per;
    public String begin_price;
    public String callsell;
    public String preday_tran_quantity;
    public String assets_total;
    public String profit_ratio;
    public String roe;
    public String high_price;
    public String remain_buy_quantity;
    public String fogn_ratio;
    public String debt_total;
    public String quarter_pureprofit;
    public String roa;
    public String low_price;
    public String remain_sell_quantity;
    public String stock_total;
    public String revenue;
    public String pureprofit_per;
    public String pbr;
    public String revenue_ratio;
    public String div_money;
    public String reserved_ratio;
    List<String> compo = new ArrayList<>();

    String headname[] = {"종목코드","종목명","시장구분","현재가","전일대비","등락율",
            "거래량","매수호가","거래대금","시가총액","영업이익","PER",
            "시가","매도호가","전일거래량","자산총액","영업이익증가율","ROE",
            "고가","매수총잔량","외국인비율","부채총계","당기순이익","ROA",
            "저가", "매도총잔량","상장주식수","매출액","주당순이익","PBR",
            "매출액증가율","보통주배당금","유보율"};

    public FormatUpjongInfo gethead() {
        FormatUpjongInfo one = new FormatUpjongInfo();
        one.stock_code="종목코드";
        one.stock_name="종목명";
        one.market_type="시장구분";
        one.cur_price="현재가";
        one.compare_yester="전일대비";
        one.updown_ratio="등락률";

        one.tran_money="거래량";
        one.callbuy="매수호가";
        one.market_volume="거래대금";
        one.tran_quantity="시가총액";
        one.profit="영업이익";
        one.per="PER";

        one.begin_price="시가";
        one.callsell="매도호가";
        one.preday_tran_quantity="전일거래량";
        one.assets_total="자산총액";
        one.profit_ratio="영업이익증가율";
        one.roe="ROE";

        one.high_price="고가";
        one.remain_buy_quantity="매수총잔량";
        one.fogn_ratio="외국인비율";
        one.debt_total="부채총계";
        one.quarter_pureprofit="당기순이익";
        one.roa="ROA";

        one.low_price="저가";
        one.remain_sell_quantity="매도총잔량";
        one.stock_total="상장주식수";
        one.revenue="매출액";
        one.pureprofit_per="주당순이익";
        one.pbr="PBR";

        one.revenue_ratio="매출액증가율";
        one.div_money="보통주배당금";
        one.reserved_ratio="유보율";

        return one;
    }

    public List<String> toStringlist() {
        List<String> result = new ArrayList<>();
        result.add(stock_code);
        result.add( stock_name);
        result.add(market_type);
        result.add(cur_price);
        result.add( compare_yester);
        result.add(updown_ratio);
        result.add(tran_quantity);
        result.add(callbuy);
        result.add(tran_money);
        result.add(market_volume);
        result.add(profit);
        result.add(per);
        result.add(begin_price);
        result.add(callsell);
        result.add(preday_tran_quantity);
        result.add(assets_total);
        result.add(profit_ratio);
        result.add(roe);
        result.add(high_price);
        result.add(remain_buy_quantity);
        result.add(fogn_ratio);
        result.add(debt_total);
        result.add(quarter_pureprofit);
        result.add(roa);
        result.add(low_price);
        result.add(remain_sell_quantity);
        result.add(stock_total);
        result.add(revenue);
        result.add(pureprofit_per);
        result.add(pbr);
        result.add(revenue_ratio);
        result.add(div_money);
        result.add(reserved_ratio);
        return result;
    }

}
