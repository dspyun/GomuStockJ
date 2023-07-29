package main.java.com.gomu.gomustock;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.min;

public class MyStat {


    public MyStat() {

    }

    public List<Integer> sumlist(List<List<Integer>> listdata) {

        List<Integer> result = new ArrayList<>();
        result = listdata.get(0);
        int size = listdata.size();
        for(int i = 1;i<size;i++) {
            List<Integer> templist = new ArrayList<>();
            int size2 = listdata.get(i).size();
            for(int j=0;j<size2;j++) {
                templist.add(result.get(j)+listdata.get(i).get(j));
            }
            result = templist;
        }
        return result;
    }

    public List<Float> yf_real_price(List<String> rawdata) {

        List<Float> closeprice = new ArrayList<Float>();
        List<Float> standardcloseprice = new ArrayList<Float>();

        // 1. 읽어온 데이터에서 종가를 Float로 추출한다
        closeprice = yf_extractClosePrice(rawdata);
        // 2. Float로 추출된 종가를 표준화 한다

        return closeprice;
    }

    public List<Float> yf_standard_price(List<String> rawdata) {

        List<Float> closeprice = new ArrayList<Float>();
        List<Float> standardcloseprice = new ArrayList<Float>();

        // 1. 읽어온 데이터에서 종가를 Float로 추출한다
        closeprice = yf_extractClosePrice(rawdata);
        // 2. Float로 추출된 종가를 표준화 한다
        standardcloseprice = standardization_lib(closeprice);
        return standardcloseprice;
    }

    String findNotZero(List<String> input) {
        String result="";
        int size = input.size();
        for(int i =0;i<size;i++) {
            if(!input.get(i).equals("0") && !input.get(i).equals("")) {
                result = input.get(i);
                break;
            }
        }
        return result;
    }

    public List<Float> string2float(List<String> input, int scale) {
        List<Float> result = new ArrayList<Float>();
        String temp;
        int size = input.size();
        for(int i=0;i< size;i++) {
            temp = input.get(i).replaceAll(",", "");
            result.add(Float.parseFloat(temp)/scale);
        }
        return result;
    }

    public List<Float> string2float_fillpre(List<String> input, int scale) {
        List<Float> result = new ArrayList<Float>();
        String temp, pretemp="";
        pretemp = findNotZero(input);
        if(pretemp.isEmpty()) pretemp = "1";
        int size = input.size();
        for(int i=0;i< size;i++) {
            temp = input.get(i).replaceAll(",", "");
            if(temp.equals("0") || temp.equals("") || temp.isEmpty()) temp = pretemp;
            result.add(Float.parseFloat(temp)/scale);
            pretemp = temp;
        }
        return result;
    }

    public List<Integer> string2int(List<String> input, int scale) {
        List<Integer> result = new ArrayList<Integer>();
        String temp;
        int size = input.size();
        for(int i=0;i< size;i++) {
            temp = input.get(i).replaceAll(",", "");
            result.add(Integer.parseInt(temp)/scale);
        }
        return result;
    }
    public List<Double> string2double(List<String> input, int scale) {
        List<Double> result = new ArrayList<Double>();
        String temp;
        int size = input.size();
        for(int i=0;i< size;i++) {
            if(input.get(i).equals("")) temp = "0";
            temp = input.get(i).replaceAll(",", "");
            result.add(Double.valueOf(temp)/scale);
        }
        return result;
    }

    public List<Float> oa_standardization(List<String> rawdata) {

        List<Float> closeprice = new ArrayList<Float>();
        List<Float> standardcloseprice = new ArrayList<Float>();

        // 1. 읽어온 데이터에서 종가를 Float로 추출한다
        closeprice = string2float(rawdata, 1);
        // 2. Float로 추출된 종가를 표준화 한다
        standardcloseprice = standardization_lib(closeprice);
        return standardcloseprice;
    }

    public List<Float> normalization(List<Float> input) {
        List<Float> temp = new ArrayList<Float>();
        List<Float> normalization = new ArrayList<Float>();
        Float max, min, low, value;
        temp = input;
        Collections.sort(temp); // 오름차순으로 정렬 후, 최대값, 최소값 저장
        max = temp.get(0);
        min = temp.get(temp.size()-1);
        low = max - min;
        for(int i=0;i<temp.size();i++) {
            value = (input.get(i) - min)/low;
            normalization.add(value);
        }
        return normalization;
    }

    public List<Float> standardization_cal(List<Float> input) {
        List<Float> temp = new ArrayList<Float>();
        List<Float> standardization = new ArrayList<Float>();
        double ave=0, stddev=0, sum=0, value=0;


        temp = input;
        for(int i=0;i<temp.size();i++) {
            sum += temp.get(i);
        }
        ave = sum/temp.size();

        for(int i=0;i<temp.size();i++) {
            sum += (temp.get(i)-ave)*(temp.get(i)-ave);
        }
        stddev = Math.sqrt(sum/temp.size());

        for(int i=0;i<temp.size();i++) {
            value = (input.get(i) - ave)/stddev;
            standardization.add((float)value);
        }
        return standardization;
    }

    public List<Float> standardization_lib(List<Float> input) {
        float average, value;
        double stddev;
        List<Float> standardization = new ArrayList<Float>();

        Mean m = new Mean(); // 이것이 math3 라이브러리에서 평균을 구해주는 객체이다.
        for (int i = 0; i < input.size(); i++) {
            m.increment(input.get(i));//자료를 넣고
        }
        average = (float) m.getResult();

        Variance v = new Variance();
        for (int i = 0; i < input.size(); i++) {
            v.increment(input.get(i));//자료를 넣고
        }
        stddev = Math.sqrt((float)v.getResult());

        for(int i=0;i<input.size();i++) {
            value = (input.get(i) - average)/(float)stddev;
            standardization.add((float)value);
        }
        return standardization;
    }

    public List<Float> yf_extractClosePrice(List<String> mycsv) {
        // mycsv의 구조는 아래와 같고 종가는 4번째 컬럼의 값이다
        // Date,Open,High,Low,Close,Adj Close,Volume
        // 2023-04-24,2538.360107,2541.889893,2518.729980,2523.500000,2523.500000,925900

        List<Float> closeprice = new  ArrayList<Float>();
        String line;
        String[] linearray;

        // csv 사이즈만큼 돌려서
        // 스트링라인 한 개를 읽어서 어레이에 분할저장 후
        // 네번째 값(종가)을 추출 후 종가 어레이 리스트에 저장한다
        // 헤더부분을 건너뛰기 위해서 1부터 시작한다
        for(int i=1; i< mycsv.size();i++) {
            line = mycsv.get(i);
            linearray = line.split(",");
            closeprice.add(Float.valueOf(linearray[4]));
        }
        return closeprice;
    }

    public List<Float> oa_extractClosePrice(List<String> pricelist) {
        // mycsv의 구조는 아래와 같고 종가는 4번째 컬럼의 값이다
        // Date,Open,High,Low,Close,Adj Close,Volume
        // 2023-04-24,2538.360107,2541.889893,2518.729980,2523.500000,2523.500000,925900

        List<Float> closeprice = new  ArrayList<Float>();
        String line;
        String[] linearray;

        // csv 사이즈만큼 돌려서
        // 스트링라인 한 개를 읽어서 어레이에 분할저장 후
        // 네번째 값(종가)을 추출 후 종가 어레이 리스트에 저장한다
        // 헤더부분을 건너뛰기 위해서 1부터 시작한다
        for(int i=0; i< pricelist.size();i++) {
            line = pricelist.get(i);
            linearray = line.split(",");
            closeprice.add(Float.valueOf(linearray[4]));
        }
        return closeprice;
    }

    public List<Float> arrangeRev_float(List<Float> input) {
        List<Float> temp = new ArrayList<>();
        for (int i = input.size() - 1; i >= 0; i--) {
            temp.add(input.get(i));
        }
        return temp;
    }
    public List<Integer> arrangeRev_int(List<Integer> input) {
        List<Integer> temp = new ArrayList<>();
        for (int i = input.size() - 1; i >= 0; i--) {
            temp.add(input.get(i));
        }
        return temp;
    }
    public List<Double> arrangeRev_double(List<Double> input) {
        List<Double> temp = new ArrayList<>();
        for (int i = input.size() - 1; i >= 0; i--) {
            temp.add(input.get(i));
        }
        return temp;
    }

    public List<String> arrangeRev_string(List<String> input) {
        List<String> temp = new ArrayList<>();
        for (int i = input.size() - 1; i >= 0; i--) {
            temp.add(input.get(i));
        }
        return temp;
    }

    public List<Float> scaling_float(List<Float> input, float min) {
        List<Float> temp = new ArrayList<>();
        float diff = (float) (min*1.1 - min);// 최저치=min, 최고치는 min*1.1 사이에서 백분율값으로 왔다갔다 하게 한다

        //temp = standardization_lib(input);
        float inputmax = Collections.max(temp);
        int size = temp.size();
        for(int i =0;i<size;i++) {
            temp.set(i,temp.get(i)/inputmax);
        }
        for(int i =0;i<size;i++) {
            temp.set(i,min+diff*temp.get(i));
        }
        return temp;
    }

    // 누적치를 스케일링하는 함수
    public List<Float> scaling_float2(List<Float> input, float min) {
        int size = input.size();
        List<Float> temp = new ArrayList<>();
        float diff = (float) (min*1.1 - min);// 최저치=min, 최고치는 min*1.1 사이에서 백분율값으로 왔다갔다 하게 한다

        // volume은 누적량이기 때문에 변동량으로 바꿔주어야 한다.
        for(int i =0;i<size-1;i++) {
            temp.add(input.get(i+1)-input.get(i));
        }

        // 변동량으로 바뀐 숫자를 스케일링한다
        float inputmax = Collections.max(temp);
        size = temp.size();
        for(int i =0;i<size;i++) {
            temp.set(i,temp.get(i)/inputmax);
        }
        for(int i =0;i<size;i++) {
            temp.set(i,min+diff*temp.get(i)*0.1f);
        }
        temp.remove(size-1);
        // input(get(i+1)-input.get(i))를 적용하면
        // 가장 마지막은 계산이 안됨.(계상이 가능하려면 size+1번째 거래량이 있어야 됨)
        // 그래서 잘라줌
        return temp;
    }

    public List<Float> leveling_float(List<Float> input) {
        List<Float> temp = new ArrayList<>();
        int size = input.size();
        float lev = (float) (Collections.min(input)*0.2);
        for(int i =0;i<size;i++) {
            temp.add(input.get(i)-lev);
        }
        return temp;
    }
}
