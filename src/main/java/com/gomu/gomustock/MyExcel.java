package main.java.com.gomu.gomustock;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import main.java.com.gomu.gomustock.format.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class MyExcel extends MyStat {



    private String DATADIR = "D:\\gomustockj\\history\\";;
    private String INFODIR = "D:\\gomustockj\\";
    private ArrayList<String> initInfo;
    public MyExcel(String filename) {

    }

    public MyExcel() {
        //initInfo = readInitFile();
    }


    public List<String> readColumn(String excelfile, int col) {

        String PathFile = INFODIR+excelfile;
        List<String> mArrayBuffer = new ArrayList<String>();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    // 현재 컬럼의 내용을 추가한다.
                    int size = sheet.getColumn(col).length;
                    for(int i=0; i < size; i++) {
                        String contents = sheet.getCell(col, i).getContents();
                        mArrayBuffer.add(contents);
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return mArrayBuffer;
    }


    public List<FormatTestData> readall_testdata(String code, boolean header) {

        String filename = code+"_testset";
        String PathFile = DATADIR+filename+".xls";;
        List<FormatTestData> testdatalist = new ArrayList<FormatTestData>();
        FormatTestData testdata = new FormatTestData();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int start = 0;
                    if(header != TRUE) start = 1;
                    int size = sheet.getColumn(0).length;
                    for(int i=start;i<size;i++) {
                        // formatOA class의 구조로 저장된다
                        // 종가는 6번째 컬럼의 값
                        testdata = new FormatTestData();
                        testdata.date = sheet.getCell(0, i).getContents();
                        testdata.price = sheet.getCell(1, i).getContents();
                        testdata.buy_quantity = sheet.getCell(2, i).getContents();
                        testdata.sell_quantity = sheet.getCell(3, i).getContents();
                        testdatalist.add(testdata);
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        return testdatalist;
    }


    public void writeprice( String filename, List<FormatOHLCV> history) {

        WritableSheet writablesheet;
        String PathFile = DATADIR+filename+".xls";;
        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            if(workbook != null) {
                //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
                workbook.createSheet("sheet1", 0);
                writablesheet = workbook.getSheet(0);
                //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();

                if(writablesheet != null) {
                    int size = history.size();
                    for(int row =0;row<size;row++) {
                        writablesheet.addCell(new Label(0, row, history.get(row).date));
                        writablesheet.addCell(new Label(1, row, history.get(row).open));
                        writablesheet.addCell(new Label(2, row, history.get(row).high));
                        writablesheet.addCell(new Label(3, row, history.get(row).low));
                        writablesheet.addCell(new Label(4, row, history.get(row).close));
                        writablesheet.addCell(new Label(5, row, history.get(row).volume));
                    }
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();

    }




    public void writefogninfo(String filename, List<String> fogn, List<String> agency) {

        WritableSheet writablesheet;
        String PathFile = DATADIR+filename+"fogn.xls";;
        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            if(workbook != null) {
                //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
                workbook.createSheet("sheet2", 0);
                writablesheet = workbook.getSheet(0);
                //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();

                if(writablesheet != null) {
                    int size = fogn.size();
                    for(int row =0;row<size;row++) {
                        // 0번은 날짜 입력하는 컬럼으로 남겨둔다. 현재 미구현
                        writablesheet.addCell(new Label(1, row, fogn.get(row)));
                        writablesheet.addCell(new Label(2, row, agency.get(row)));
                    }
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();

    }

    public int getTagColumn(String tag) {
        // default 값은  종가는읽게 6으로 리턴
        int column=0;
        if(tag.equals("DATE")) column = 0;
        else if(tag.equals("OPEN")) column = 1; // 매도 1
        else if(tag.equals("HIGH")) column = 2; // 매수 2
        else if(tag.equals("LOW")) column = 3;
        else if(tag.equals("CLOSE")) column = 4; // 체결가 4
        else if(tag.equals("VOLUME")) column = 5;
        else column = 4;

        return column;
    }

    public void write_ohlcv( String filename, List<FormatOHLCV> src_csvlist) {

        List<FormatOHLCV> csvlist = new ArrayList<>();
        csvlist = src_csvlist;

        WritableSheet writablesheet;
        String PathFile = DATADIR+filename+".xls";;
        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            if(workbook != null) {
                //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
                workbook.createSheet("sheet1", 0);
                writablesheet = workbook.getSheet(0);
                //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();

                if(writablesheet != null) {
                    int size = csvlist.size();
                    for(int row =0;row<size;row++) {
                        writablesheet.addCell(new Label(0, row, csvlist.get(row).date));
                        writablesheet.addCell(new Label(1, row, csvlist.get(row).open));
                        writablesheet.addCell(new Label(2, row, csvlist.get(row).high));
                        writablesheet.addCell(new Label(3, row, csvlist.get(row).low));
                        writablesheet.addCell(new Label(4, row, csvlist.get(row).close));
                        writablesheet.addCell(new Label(5, row, csvlist.get(row).adjclose));
                        writablesheet.addCell(new Label(6, row, csvlist.get(row).volume));
                    }
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
    }


    public List<String> oa_readItem(String filename, String tag, boolean header) {

        int column = getTagColumn(tag);
        String PathFile = DATADIR+filename;;
        List<String> pricebuffer = new ArrayList<String>();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int start = 0;
                    if(header != TRUE) start = 1;
                    int size = sheet.getColumn(column).length;
                    for(int i=start;i<size;i++) {
                        // formatOA class의 구조로 저장된다
                        // 종가는 6번째 컬럼의 값
                        pricebuffer.add(sheet.getCell(column, i).getContents());
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        return pricebuffer;
    }

    public int getTodayTagColumn(String tag) {
        // default 값은  종가는읽게 6으로 리턴
        int column=0;
        if(tag.equals("DATE")) column = 0;
        else if(tag.equals("DEAL")) column = 4;
        else if(tag.equals("SELL")) column = 1;
        else if(tag.equals("BUY")) column = 2;
        else if(tag.equals("VOLUME")) column = 6;
        else column = 4;

        return column;
    }

    public void writetodayprice( String filename, List<FormatOHLCV> history) {
        write_ohlcv( filename, history);
    }

    public List<String> readtodayprice( String stock_code, String tag, int days, boolean header) {
        // data 저장순서는 현재>과거순으이다, 60일치를 읽으려면 0부터 60개를 읽으면 된다
        int column = getTodayTagColumn(tag);
        InputStream is=null;
        Workbook wb=null;
        int maxcol;

        String PathFile = DATADIR+stock_code+".xls";;
        List<String> pricebuffer = new ArrayList<String>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int start = 0, end;
                if(header != true) start = 1;
                maxcol = sheet.getColumn(column).length;
                // days가 maxcol을 넘어가거나 -1보다 작으면 maxcol로 읽는다
                if(days >= maxcol || days <= -1) {
                    start = 1;
                    end = maxcol;
                }
                else {
                    start = maxcol-days;
                    end = maxcol;
                }
                for(int i=start;i<end;i++) {
                    // formatOA class의 구조로 저장된다
                    pricebuffer.add(sheet.getCell(column, i).getContents());
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        List<String> pricebuffer_rev = new ArrayList<>();
        int size = pricebuffer.size();
        for(int i =size-1;i>=0;i--) {
            pricebuffer_rev.add(pricebuffer.get(i));
        }

        return pricebuffer_rev;
    }

    public List<String> read_ohlcv(String stock_code, String tag, int days, boolean header) {

        // data 저장순서는 현재>과거순으이다, 60일치를 읽으려면 0부터 60개를 읽으면 된다
        int column = getTagColumn(tag);
        InputStream is=null;
        Workbook wb=null;
        int maxcol;

        String PathFile = DATADIR+stock_code+".xls";;
        List<String> pricebuffer = new ArrayList<String>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int start = 0, end;
                if(!header) start = 1;
                maxcol = sheet.getColumn(column).length;
                // days가 maxcol을 넘어가거나 -1보다 작으면 maxcol로 읽는다
                if(days >= maxcol || days <= -1) {
                    start = 1;
                    end = maxcol;
                }
                else {
                    start = maxcol-days;
                    end = maxcol;
                }
                for(int i=start;i<end;i++) {
                    // formatOA class의 구조로 저장된다
                    pricebuffer.add(sheet.getCell(column, i).getContents());
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        return pricebuffer;
    }
    public List<FormatOHLCV> readall_ohlcv(String stock_code) {

        // data 저장순서는 현재>과거순으이다, 60일치를 읽으려면 0부터 60개를 읽으면 된다

        String PathFile = DATADIR+stock_code+".xls";;
        List<FormatOHLCV> ohlcvlist = new ArrayList<FormatOHLCV>();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for(int i=1;i<size;i++) {
                    FormatOHLCV oneohlcv = new FormatOHLCV();
                    // formatOA class의 구조로 저장된다
                    oneohlcv.date = sheet.getCell(0, i).getContents();
                    oneohlcv.open = sheet.getCell(1, i).getContents();
                    oneohlcv.high = sheet.getCell(2, i).getContents();
                    oneohlcv.low = sheet.getCell(3, i).getContents();
                    oneohlcv.close = sheet.getCell(4, i).getContents();
                    oneohlcv.adjclose = sheet.getCell(5, i).getContents();
                    oneohlcv.volume = sheet.getCell(6, i).getContents();
                    ohlcvlist.add(oneohlcv);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        return ohlcvlist;
    }


    public List<List<String>> readStockDic() {
        List<String> STOCK_CODE = new ArrayList<String>();
        List<String> STOCK_NAME = new ArrayList<String>();
        List<String> MARKET = new ArrayList<String>();
        List<List<String>> diclist = new ArrayList<List<String>>();

        String PathFile = INFODIR+"table_stock.xls";;

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for (int i = 1; i <= size-1; i++) {
                    STOCK_CODE.add(sheet.getCell(1, i).getContents());
                    STOCK_NAME.add(sheet.getCell(3, i).getContents());
                    MARKET.add(sheet.getCell(6, i).getContents());
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        diclist.add(STOCK_CODE);
        diclist.add(STOCK_NAME);
        diclist.add(MARKET);
        return diclist;
    }

    public List<List<String>> readETFDict() {
        List<String> STOCK_CODE = new ArrayList<String>();
        List<String> STOCK_NAME = new ArrayList<String>();
        List<String> MARKET = new ArrayList<String>();
        List<List<String>> diclist = new ArrayList<List<String>>();
        InputStream is=null;
        Workbook wb=null;

        String PathFile = INFODIR+"table_etf.xls";;

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for (int i = 1; i <= size-1; i++) {
                    STOCK_CODE.add(sheet.getCell(1, i).getContents());
                    STOCK_NAME.add(sheet.getCell(3, i).getContents());
                    MARKET.add(sheet.getCell(3, i).getContents());
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        diclist.add(STOCK_CODE);
        diclist.add(STOCK_NAME);
        diclist.add(MARKET);
        return diclist;
    }


    public Boolean file_check(String filename) {

        String PathFile = DATADIR + filename;
        boolean return_flag=false;

        java.io.File file1 = new java.io.File(PathFile);
        // 1. check if the file exists or not
        if (file1.exists()) return_flag = true;
        else return_flag = false;

        return return_flag;
    }


    public List<FormatTestData> readtestset(String filename, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = DATADIR+filename;;
        List<FormatTestData> testdatalist = new ArrayList<FormatTestData>();
        FormatTestData testdata = new FormatTestData();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int start = 0;
                if(header != TRUE) start = 0;
                int size = sheet.getColumn(0).length;
                for(int i=start;i<size;i++) {
                    // formatOA class의 구조로 저장된다
                    // 종가는 6번째 컬럼의 값
                    testdata = new FormatTestData();
                    testdata.date = sheet.getCell(0, i).getContents();
                    testdata.price = sheet.getCell(1, i).getContents();
                    testdata.buy_quantity = sheet.getCell(2, i).getContents();
                    testdata.sell_quantity = sheet.getCell(3, i).getContents();
                    testdatalist.add(testdata);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        return testdatalist;
    }
    public List<FormatTestData> readtestbuy(String filename, boolean header) {

        String PathFile = DATADIR+filename;;
        List<FormatTestData> testdatalist = new ArrayList<FormatTestData>();
        FormatTestData testdata = new FormatTestData();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int start = 0;
                if(header != TRUE) start = 0;
                int size = sheet.getColumn(0).length;
                for(int i=start;i<size;i++) {
                    // formatOA class의 구조로 저장된다
                    // 종가는 6번째 컬럼의 값
                    testdata = new FormatTestData();
                    testdata.date = sheet.getCell(0, i).getContents();
                    testdata.price = sheet.getCell(1, i).getContents();
                    testdata.buy_quantity = sheet.getCell(2, i).getContents();
                    testdatalist.add(testdata);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        return testdatalist;
    }


    public List<FormatTestData> readtestsell(String filename, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = DATADIR+filename;;
        List<FormatTestData> testdatalist = new ArrayList<FormatTestData>();
        FormatTestData testdata = new FormatTestData();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                int start = 0;
                if(header != TRUE) start = 0;
                for(int i=start;i<size;i++) {
                    // sell quantity를 읽는ㄴ다
                    testdata = new FormatTestData();
                    testdata.date = sheet.getCell(0, i).getContents();
                    testdata.price = sheet.getCell(1, i).getContents();
                    testdata.sell_quantity = sheet.getCell(3, i).getContents();
                    testdatalist.add(testdata);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return testdatalist;
    }

    public void write_testdata(String code, List<String> date ,List<String> price,List<Integer> buy, List<Integer> sell) {

        String filename = code+"_testset";
        String PathFile = DATADIR+filename+".xls";;
        WritableSheet writablesheet;

        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
            workbook.createSheet("sheet1", 0);
            writablesheet = workbook.getSheet(0);
            //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();

            if(writablesheet != null) {
                // header 때문에 1부터 시작해야 한다
                int size = buy.size();
                for(int row =1;row < size+1 ;row++) {
                    writablesheet.addCell(new Label(0, row, date.get(row-1)));
                    writablesheet.addCell(new Label(1, row, price.get(row-1)));
                    writablesheet.addCell(new Label(2, row, String.valueOf(buy.get(row-1))));
                    writablesheet.addCell(new Label(3, row, String.valueOf(sell.get(row-1))));
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();

    }


    public void writestockinfo(int index, List<FormatStockInfo> information) {

        WritableSheet writablesheet;
        WritableWorkbook workbook;
        String PathFile="";
        if(index==0) PathFile = INFODIR+"stockinfo"+".xls";
        if(index==1) PathFile = INFODIR+"stockinfo01"+".xls";;
        if(index==2) PathFile = INFODIR+"monitor"+".xls";;
        java.io.File file1 = new java.io.File(PathFile);

        // 헤더를 붙여준다
        FormatStockInfo info1 = new FormatStockInfo();
        info1.setHeader();
        information.add(0,info1);

        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
            workbook.createSheet("sheet0", 0);
            writablesheet = workbook.getSheet(0);
            //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();
            int size = information.size();
            if(writablesheet != null) {
                for(int row =0;row<size;row++) {
                    writablesheet.addCell(new Label(0, row, information.get(row).stock_code));
                    writablesheet.addCell(new Label(1, row, information.get(row).stock_name));
                    writablesheet.addCell(new Label(2, row, information.get(row).stock_type));
                    writablesheet.addCell(new Label(3, row, information.get(row).ranking));
                    writablesheet.addCell(new Label(4, row, information.get(row).per));
                    writablesheet.addCell(new Label(5, row, information.get(row).expect_per));
                    writablesheet.addCell(new Label(6, row, information.get(row).area_per));
                    writablesheet.addCell(new Label(7, row, information.get(row).pbr));
                    writablesheet.addCell(new Label(8, row, information.get(row).div_rate));
                    writablesheet.addCell(new Label(9, row, information.get(row).fogn_rate));
                    writablesheet.addCell(new Label(10, row, information.get(row).recommend));
                    writablesheet.addCell(new Label(11, row, information.get(row).cur_price));
                    writablesheet.addCell(new Label(12, row, information.get(row).score));
                    writablesheet.addCell(new Label(13, row, information.get(row).desc));
                    writablesheet.addCell(new Label(14, row, information.get(row).news));
                    writablesheet.addCell(new Label(15, row, information.get(row).fninfo));
                    writablesheet.addCell(new Label(16, row, information.get(row).etfinfo));
                    writablesheet.addCell(new Label(17, row, information.get(row).nav));
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();

    }


    public void writestockinfoCustom(String filename, List<FormatStockInfo> information) {

        WritableSheet writablesheet;
        WritableWorkbook workbook;

        int line, col;
        String PathFile="";
        PathFile = INFODIR+filename+".xls";
        if(file_check(PathFile)) {
            return ;
        }

        java.io.File file1 = new java.io.File(PathFile);

        // 헤더를 붙여준다
        FormatStockInfo info1 = new FormatStockInfo();
        info1.setHeader();
        information.add(0,info1);

        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
            workbook.createSheet("sheet0", 0);
            writablesheet = workbook.getSheet(0);
            //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();
            int size = information.size();
            if(writablesheet != null) {
                for(int row =0;row<size;row++) {
                    writablesheet.addCell(new Label(0, row, information.get(row).stock_code));
                    writablesheet.addCell(new Label(1, row, information.get(row).stock_name));
                    writablesheet.addCell(new Label(2, row, information.get(row).stock_type));
                    writablesheet.addCell(new Label(3, row, information.get(row).ranking));
                    writablesheet.addCell(new Label(4, row, information.get(row).per));
                    writablesheet.addCell(new Label(5, row, information.get(row).expect_per));
                    writablesheet.addCell(new Label(6, row, information.get(row).area_per));
                    writablesheet.addCell(new Label(7, row, information.get(row).pbr));
                    writablesheet.addCell(new Label(8, row, information.get(row).div_rate));
                    writablesheet.addCell(new Label(9, row, information.get(row).fogn_rate));
                    writablesheet.addCell(new Label(10, row, information.get(row).recommend));
                    writablesheet.addCell(new Label(11, row, information.get(row).cur_price));
                    writablesheet.addCell(new Label(12, row, information.get(row).score));
                    writablesheet.addCell(new Label(13, row, information.get(row).desc));
                    writablesheet.addCell(new Label(14, row, information.get(row).news));
                    writablesheet.addCell(new Label(15, row, information.get(row).fninfo));
                    writablesheet.addCell(new Label(16, row, information.get(row).etfinfo));
                    writablesheet.addCell(new Label(17, row, information.get(row).nav));
                    writablesheet.addCell(new Label(18, row, "-"));
                    writablesheet.addCell(new Label(19, row, "-"));
                    writablesheet.addCell(new Label(20, row, "-"));
                    writablesheet.addCell(new Label(21, row, "-"));
                    writablesheet.addCell(new Label(22, row, "-"));
                    writablesheet.addCell(new Label(23, row, "-"));
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();

    }

    public List<FormatStockInfo> readStockinfo(int index, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile="";
        if(index==0) PathFile = INFODIR+"stockinfo"+".xls";
        if(index==1) PathFile = INFODIR+"stockinfo01"+".xls";;
        if(index==2) PathFile = INFODIR+"monitor"+".xls";;
        List<FormatStockInfo> mArrayBuffer = new ArrayList<FormatStockInfo>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                int start = 0;
                if(header != TRUE) start = 1;
                for(int i=start;i<size;i++) {
                    FormatStockInfo temp = new FormatStockInfo();
                    temp.stock_code = sheet.getCell(0, i).getContents();
                    temp.stock_name = sheet.getCell(1, i).getContents();
                    temp.stock_type = sheet.getCell(2, i).getContents();
                    temp.ranking = sheet.getCell(3, i).getContents();
                    temp.per = sheet.getCell(4, i).getContents();
                    temp.expect_per = sheet.getCell(5, i).getContents();
                    temp.area_per = sheet.getCell(6, i).getContents();
                    temp.pbr = sheet.getCell(7, i).getContents();
                    temp.div_rate = sheet.getCell(8, i).getContents();
                    temp.fogn_rate = sheet.getCell(9, i).getContents();
                    temp.recommend = sheet.getCell(10, i).getContents();
                    temp.cur_price = sheet.getCell(11, i).getContents();
                    temp.score = sheet.getCell(12, i).getContents();
                    temp.desc = sheet.getCell(13, i).getContents();
                    temp.news = sheet.getCell(14, i).getContents();
                    temp.fninfo = sheet.getCell(15, i).getContents();
                    temp.etfinfo = sheet.getCell(16, i).getContents();
                    temp.nav = sheet.getCell(17, i).getContents();
                    mArrayBuffer.add(temp);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return mArrayBuffer;
    }

    public List<FormatStockInfo> readStockinfoCustom(String filename, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        List<FormatStockInfo> mArrayBuffer = new ArrayList<FormatStockInfo>();

        int line, col;
        String PathFile="";
        PathFile = INFODIR+filename+".xls";
        if(file_check(PathFile)) {
            FormatStockInfo oneinfo = new FormatStockInfo();
            oneinfo.init();
            mArrayBuffer.add(oneinfo);
            return mArrayBuffer;
        }

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                int start = 0;
                if(header != TRUE) start = 1;
                for(int i=start;i<size;i++) {
                    FormatStockInfo temp = new FormatStockInfo();
                    temp.stock_code = sheet.getCell(0, i).getContents();
                    temp.stock_name = sheet.getCell(1, i).getContents();
                    temp.stock_type = sheet.getCell(2, i).getContents();
                    temp.ranking = sheet.getCell(3, i).getContents();
                    temp.per = sheet.getCell(4, i).getContents();
                    temp.expect_per = sheet.getCell(5, i).getContents();
                    temp.area_per = sheet.getCell(6, i).getContents();
                    temp.pbr = sheet.getCell(7, i).getContents();
                    temp.div_rate = sheet.getCell(8, i).getContents();
                    temp.fogn_rate = sheet.getCell(9, i).getContents();
                    temp.recommend = sheet.getCell(10, i).getContents();
                    temp.cur_price = sheet.getCell(11, i).getContents();
                    temp.score = sheet.getCell(12, i).getContents();
                    temp.desc = sheet.getCell(13, i).getContents();
                    temp.news = sheet.getCell(14, i).getContents();
                    temp.fninfo = sheet.getCell(15, i).getContents();
                    temp.etfinfo = sheet.getCell(16, i).getContents();
                    temp.nav = sheet.getCell(17, i).getContents();
                    mArrayBuffer.add(temp);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return mArrayBuffer;
    }

    public List<FormatMyStock> readSectorinfo(boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = INFODIR+"index_sector"+".xls";;
        List<FormatMyStock> mArrayBuffer = new ArrayList<FormatMyStock>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            int size=0, start = 0;
            if(header != TRUE) start = 1;

            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                size = sheet.getColumn(0).length;
                for(int i=start;i<size;i++) {
                    FormatMyStock temp = new FormatMyStock();
                    temp.stock_code = sheet.getCell(0, i).getContents();
                    temp.stock_name = sheet.getCell(1, i).getContents();
                    mArrayBuffer.add(temp);
                }
            }

            Sheet sheet1 = wb.getSheet(1);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                size = sheet1.getColumn(0).length;
                for(int i=start;i<size;i++) {
                    FormatMyStock temp = new FormatMyStock();
                    temp.stock_code = sheet1.getCell(0, i).getContents();
                    temp.stock_name = sheet1.getCell(1, i).getContents();
                    mArrayBuffer.add(temp);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return mArrayBuffer;
    }

    public List<String> readFogninfo(String stock_code, String group, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        int line, col;
        String PathFile = DATADIR+stock_code+"fogn.xls";;
        if(group.equals("FOGN")) col = 1;
        else if(group.equals("AGENCY")) col = 2;
        else col = 1;

        List<String> Buffer = new ArrayList<>();
        List<String> Buffer_rev = new ArrayList<>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int start = 0;
                if(header != TRUE) start = 1;
                //for(int i=start;i<sheet.getColumn(col).length;i++) {
                for(int i=start;i<20;i++) {
                    Buffer.add(sheet.getCell(col, i).getContents());
                }
                // 주가의 데이터 순서와 맞춰준다
                // 과거의 데이터를 시작으로 해서 최신 데이터가 가장 끝에 오도록 재정렬 해준다
                // 저장할 때 역순으로 먼저 저장해도 되지만
                // 100개를 역순으로 저장하고 20개를 읽으면 20개에 최신 데이터가 포함되지 않는다
                // 최과거 데이터가 0번이기 때문에 최과거 기준으로 20개가 읽히므로
                // 그것을 방지하기 위해 읽는 곳에서 데이터를 역순으로 배열시킨다
                for(int j =Buffer.size()-1;j>=0;j--) {
                    Buffer_rev.add(Buffer.get(j));
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return Buffer_rev;
    }
    public List<String> readTodayFogninfo(String stock_code,boolean header) {

        String PathFile = DATADIR+stock_code+"fogn.xls";;

        List<String> Buffer = new ArrayList<>();


        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int start = 0;
                if(header != TRUE) start = 1;
                // 최신 외국인 매수량
                Buffer.add(sheet.getCell(1, 1).getContents());
                // 최신 기관 매수량
                Buffer.add(sheet.getCell(2, 1).getContents());
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return Buffer;
    }

    public String readTreemap() {

        StringBuilder  sb = new StringBuilder();
        String PathFile = DATADIR+"treemap.xls";;
        InputStream is=null;
        Workbook wb=null;
        String spc10="          ";
        String spc4="    ";

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    int size = sheet.getColumn(0).length;
                    for(int i=0;i<size;i++) {
                        sb.append(spc10 +"[");
                        sb.append("'"+sheet.getCell(0, i).getContents()+"',"+spc4); // name
                        sb.append("'"+sheet.getCell(1, i).getContents()+"',"+spc4); // area
                        sb.append(sheet.getCell(2, i).getContents()+","+spc4); // volume
                        sb.append(sheet.getCell(3, i).getContents()); // change
                        sb.append("],\n");
                    }
                    sb.append(spc10+"]);\n");
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void writehtml(String html_string) {

        String PathFile = "file:///android_asset/" + "treemap" + ".html";
        ;
        try {
            File file = new File(PathFile); // File객체 생성
            FileWriter fw = new FileWriter(file, false);
            fw.write(html_string);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeSimullist(List<String> stocklist) {

        String filename = "simul_list";
        WritableSheet writablesheet;
        String PathFile = INFODIR+filename+".xls";;
        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            workbook.createSheet("sheet1", 0);
            writablesheet = workbook.getSheet(0);

            if(writablesheet != null) {
                int size = stocklist.size();
                for(int row =0;row < size ;row++) {
                    writablesheet.addCell(new Label(0, row, stocklist.get(row)));
                }
            }
            workbook.write();
            workbook.close();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }

    }
    public List<String> readSimullist() {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = INFODIR+"simul_list"+".xls";;
        List<String> stocklist = new ArrayList<String>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for(int i=0;i<size;i++) {
                    stocklist.add(sheet.getCell(0, i).getContents());
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return stocklist;
    }


    public void writeColumn(String filename, List<String> data, int col) {

        WritableSheet writablesheet;
        String PathFile = INFODIR+filename+".xls";;
        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            workbook.createSheet("sheet1", 0);
            writablesheet = workbook.getSheet(0);

            if(writablesheet != null) {
                int size = data.size();
                for(int row =1;row < size ;row++) {
                    writablesheet.addCell(new Label(col, row, data.get(row)));
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();

    }


    public List<FormatMyStock> readMyStockList() {

        String temp=null;
        String PathFile = INFODIR+"mystock"+".xls";;
        List<FormatMyStock> mystocklist = new ArrayList<FormatMyStock>();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for(int i=1;i<size;i++) {
                    FormatMyStock mystock = new FormatMyStock();
                    mystock.stock_code = sheet.getCell(0, i).getContents();
                    mystock.stock_name = sheet.getCell(1, i).getContents();
                    temp = sheet.getCell(2, i).getContents();
                    mystock.quantity = Integer.parseInt(temp);
                    temp = sheet.getCell(3, i).getContents();
                    mystock.buy_price = Integer.parseInt(temp);
                    mystocklist.add(mystock);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return mystocklist;
    }

    public List<FormatAccount> readMyAccount() {

        String temp=null;
        String PathFile = INFODIR+"myaccount"+".xls";;
        List<FormatAccount> accountlist = new ArrayList<FormatAccount>();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for(int i=1;i<size;i++) {
                    FormatAccount account = new FormatAccount();
                    account.stock_code = sheet.getCell(0, i).getContents();
                    account.stock_name = sheet.getCell(1, i).getContents();
                    account.quantity = sheet.getCell(2, i).getContents();
                    account.buyprice = sheet.getCell(3, i).getContents();
                    account.buytarget = sheet.getCell(4, i).getContents();
                    account.selltarget = sheet.getCell(5, i).getContents();
                    account.nowprice = sheet.getCell(6, i).getContents();
                    account.memo_1 = sheet.getCell(7, i).getContents();
                    account.memo_2 = sheet.getCell(8, i).getContents();
                    account.memo_3 = sheet.getCell(9, i).getContents();
                    account.memo_4 = sheet.getCell(10, i).getContents();
                    accountlist.add(account);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return accountlist;
    }


    public void writenaverupjong2(String filename,List<List<String>> upjonglist) {


        WritableSheet writablesheet;
        String PathFile = INFODIR+filename+".xls";

        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            workbook.createSheet("sheet1", 0);
            writablesheet = workbook.getSheet(0);


            if(writablesheet != null) {
                int size = upjonglist.size();
                for(int row =0;row < size ;row++) {
                    List<String> onelist = new ArrayList<>();
                    onelist = upjonglist.get(row);
                    int size2 = onelist.size();
                    for(int j =0;j<size2;j++) {
                        writablesheet.addCell(new Label(j, row, onelist.get(j)));
                    }
                    for(int j =size2;j<size2+20;j++) {
                        writablesheet.addCell(new Label(j, row, ""));
                    }
                }
            }
            workbook.write();
            workbook.close();

        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }

    }
    public void writenaverupjong(String filename,List<FormatUpjongInfo> upjonglist) {


        WritableSheet writablesheet;
        String PathFile = INFODIR+filename+".xls";

        FormatUpjongInfo head = new FormatUpjongInfo();
        upjonglist.add(0,head.gethead());

        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
            workbook.createSheet("sheet1", 0);
            writablesheet = workbook.getSheet(0);
            //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();

            if(writablesheet != null) {
                int size = upjonglist.size();
                for(int row =0;row < size ;row++) {
                    writablesheet.addCell(new Label(0, row, upjonglist.get(row).stock_code));
                    writablesheet.addCell(new Label(1, row, upjonglist.get(row).stock_name));
                    writablesheet.addCell(new Label(2, row, upjonglist.get(row).market_type));
                    writablesheet.addCell(new Label(3, row, upjonglist.get(row).cur_price));
                    writablesheet.addCell(new Label(4, row, upjonglist.get(row).compare_yester));
                    writablesheet.addCell(new Label(5, row, upjonglist.get(row).updown_ratio));
                    writablesheet.addCell(new Label(6, row, upjonglist.get(row).tran_quantity));
                    writablesheet.addCell(new Label(7, row, upjonglist.get(row).callbuy));
                    writablesheet.addCell(new Label(8, row, upjonglist.get(row).tran_money));
                    writablesheet.addCell(new Label(9, row, upjonglist.get(row).market_volume));
                    writablesheet.addCell(new Label(10, row, upjonglist.get(row).profit));
                    writablesheet.addCell(new Label(11, row, upjonglist.get(row).per));
                    writablesheet.addCell(new Label(12, row, upjonglist.get(row).begin_price));
                    writablesheet.addCell(new Label(13, row, upjonglist.get(row).callsell));
                    writablesheet.addCell(new Label(14, row, upjonglist.get(row).preday_tran_quantity));
                    writablesheet.addCell(new Label(15, row, upjonglist.get(row).assets_total));
                    writablesheet.addCell(new Label(16, row, upjonglist.get(row).profit_ratio));
                    writablesheet.addCell(new Label(17, row, upjonglist.get(row).roe));
                    writablesheet.addCell(new Label(18, row, upjonglist.get(row).high_price));
                    writablesheet.addCell(new Label(19, row, upjonglist.get(row).remain_buy_quantity));
                    writablesheet.addCell(new Label(20, row, upjonglist.get(row).fogn_ratio));
                    writablesheet.addCell(new Label(21, row, upjonglist.get(row).debt_total));
                    writablesheet.addCell(new Label(22, row, upjonglist.get(row).quarter_pureprofit));
                    writablesheet.addCell(new Label(23, row, upjonglist.get(row).roa));
                    writablesheet.addCell(new Label(24, row, upjonglist.get(row).low_price));
                    writablesheet.addCell(new Label(25, row, upjonglist.get(row).remain_sell_quantity));
                    writablesheet.addCell(new Label(26, row, upjonglist.get(row).stock_total));
                    writablesheet.addCell(new Label(27, row, upjonglist.get(row).revenue));
                    writablesheet.addCell(new Label(28, row, upjonglist.get(row).pureprofit_per));
                    writablesheet.addCell(new Label(29, row, upjonglist.get(row).pbr));
                    writablesheet.addCell(new Label(30, row, upjonglist.get(row).revenue_ratio));
                    writablesheet.addCell(new Label(31, row, upjonglist.get(row).div_money));
                    writablesheet.addCell(new Label(32, row, upjonglist.get(row).reserved_ratio));
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException | WriteException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }//Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();

    }

    public List<FormatUpjongInfo>  readnaverupjong(String filename) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = INFODIR+filename+".xls";;

        List<FormatUpjongInfo> upjonglist = new ArrayList<>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for(int i=1;i<size;i++) {
                    FormatUpjongInfo one = new FormatUpjongInfo();
                    one.stock_code = sheet.getCell(0, i).getContents();
                    one.stock_name= sheet.getCell(1, i).getContents();
                    one.market_type= sheet.getCell(2, i).getContents();
                    one.cur_price= sheet.getCell(3, i).getContents();
                    one.compare_yester= sheet.getCell(4, i).getContents();
                    one.updown_ratio= sheet.getCell(5, i).getContents();
                    one.tran_quantity= sheet.getCell(6, i).getContents();
                    one.callbuy= sheet.getCell(7, i).getContents();
                    one.tran_money= sheet.getCell(8, i).getContents();
                    one.market_volume= sheet.getCell(9, i).getContents();
                    one.profit= sheet.getCell(10, i).getContents();
                    one.per= sheet.getCell(11, i).getContents();
                    one.begin_price= sheet.getCell(12, i).getContents();
                    one.callsell= sheet.getCell(13, i).getContents();
                    one.preday_tran_quantity= sheet.getCell(14, i).getContents();
                    one.assets_total= sheet.getCell(15, i).getContents();
                    one.profit_ratio= sheet.getCell(16, i).getContents();
                    one.roe= sheet.getCell(17, i).getContents();
                    one.high_price= sheet.getCell(18, i).getContents();
                    one.remain_buy_quantity= sheet.getCell(19, i).getContents();
                    one.fogn_ratio= sheet.getCell(20, i).getContents();
                    one.debt_total= sheet.getCell(21, i).getContents();
                    one.quarter_pureprofit= sheet.getCell(22, i).getContents();
                    one.roa= sheet.getCell(23, i).getContents();
                    one.low_price= sheet.getCell(24, i).getContents();
                    one.remain_sell_quantity= sheet.getCell(25, i).getContents();
                    one.stock_total= sheet.getCell(26, i).getContents();
                    one.revenue= sheet.getCell(27, i).getContents();
                    one.pureprofit_per= sheet.getCell(28, i).getContents();
                    one.pbr= sheet.getCell(29, i).getContents();
                    one.revenue_ratio= sheet.getCell(30, i).getContents();;
                    one.div_money= sheet.getCell(31, i).getContents();
                    one.reserved_ratio= sheet.getCell(32, i).getContents();
                    upjonglist.add(one);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return upjonglist;
    }


    public List<List<String>>  readraw(String filename) {

        String PathFile = INFODIR+filename+".xls";;
        List<List<String>> multilist = new ArrayList<List<String>>();

        try {
            InputStream is =  new FileInputStream(PathFile);
            Workbook wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0);   // 시트 불러오기
            if(sheet != null) {
                // line1, col1에서 contents를 읽는다.
                int size = sheet.getColumn(0).length;
                for(int i=1;i<size;i++) {
                    List<String> onelist = new ArrayList<>();
                    int rowlength = sheet.getRow(0).length;
                    for(int j =0;j<rowlength;j++) {
                        onelist.add(sheet.getCell(j, i).getContents());
                    }
                    multilist.add(onelist);
                }
            }
            wb.close();
            is.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return multilist;
    }

    public void writeraw(String filename,List<List<String>> multilist) {


        WritableSheet writablesheet;
        String PathFile = INFODIR+filename+".xls";

        java.io.File file1 = new java.io.File(PathFile);
        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            WritableWorkbook workbook = Workbook.createWorkbook(file1);
            workbook.createSheet("sheet1", 0);
            writablesheet = workbook.getSheet(0);

            if(writablesheet != null) {

                int size = multilist.size();
                for(int i=1;i<size;i++) {
                    List<String> onelist = multilist.get(i);
                    int rowlength = onelist.size();
                    for(int j =0;j<rowlength;j++) {
                        writablesheet.addCell(new Label(i, j, onelist.get(i)));
                    }
                }
            }
            workbook.write();
            workbook.close();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }

    }

    public void mergenaverupjong(String basefile,String insertfile) {

        List<List<String>> basedata = readraw(basefile);
        List<List<String>> insertdata = readraw(insertfile);

        for (List<String> onelist : insertdata) {
            String stock_name = onelist.get(1);
            int size2 = onelist.size();
            int index = findsamestock(basedata, stock_name);
            for (int j = 0; j < size2; j++) {
                String data = onelist.get(j);
                if (!data.equals("")) basedata.get(index).set(j, data);
            }
        }

        writeraw(basefile,basedata);
    }

    public int findsamestock(List<List<String>> baselist, String stock_name) {
        int size = baselist.size();
        int index = 0;
        for(int i =0;i<size;i++) {
            List<String> onelist = baselist.get(i);
            if(onelist.get(1).equals(stock_name)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
