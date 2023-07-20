package main.java.com.gomu.gomustock;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import main.java.com.gomu.gomustock.format.FormatOHLCV;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.format.FormatTestData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class MyExcel extends MyStat {


    private String ExcelFile;
    private int colTotal, rowTotal;
    String oldfilename=null, oldline=null, oldcol=null;
    private String latest_filename, latest_line;
    private List<String> column00, column01, column02, column03;
    private String STOCKDIR = "D:\\gomustockj\\";;
    private String DOWNLOAD = "D:\\gomustockj\\";
    private ArrayList<String> initInfo;

    List<String> ETF_NO = new ArrayList<String>();
    List<String> ETF_NAME = new ArrayList<String>();

    public MyExcel(String filename) {
        ExcelFile=filename;
        init_maxline();
        column00 = readColumn(filename,0);
        column01 = readColumn(filename,1);
        column02 = readColumn(filename,2);

    }

    public MyExcel() {
        //initInfo = readInitFile();

    }



    /* sheet의 최대 라인수를 읽어서 전역변수에 저장한다. */

    public void init_maxline () {
        InputStream is1 = null;
        Workbook wb1 = null;
        int first_col=0;
        String PathFile = ExcelFile;

        try {
            is1 = new FileInputStream(PathFile);
            wb1 = Workbook.getWorkbook(is1);
            Sheet sheet = wb1.getSheet(0);   // 시트 불러오기

            colTotal = sheet.getColumns();    // 전체 컬럼
            rowTotal = sheet.getColumn(first_col).length; // 라인은 첫째 컬럼 최대치를 기준으론 한다.
            wb1.close();
            is1.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb1.close();
            //is1.close();
        }
    }

    public List<String> readColumn(String excelfile, int col) {

        InputStream is=null;
        Workbook wb=null;
        String contents=null;
        String PathFile = STOCKDIR+excelfile;
        List<String> mArrayBuffer = new ArrayList<String>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    // 현재 컬럼의 내용을 추가한다.
                    int size = sheet.getColumn(col).length;
                    for(int i=0; i < size-1; i++) {
                        contents = sheet.getCell(col, i).getContents();
                        mArrayBuffer.add(contents);
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }
        return mArrayBuffer;
    }

    public int getMaxRow() {
        return rowTotal;
    }
    public int getMaxColumn() {
        return colTotal;
    }

    public String readCell( int col1, int line1) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = ExcelFile;

        line = line1; col=col1;
        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    contents1 = sheet.getCell(col, line).getContents();
                    //colTotal = sheet.getColumns();    // 전체 컬럼
                    //rowTotal = sheet.getColumn(0).length; // 라인은 첫째 컬럼 최대치를 기준으론 한다.
                    //return contents1;
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }
        return contents1;
    }



    public void writeprice( String filename, List<FormatOHLCV> history) {

        WritableSheet writablesheet;
        String PathFile = STOCKDIR+filename+".xls";;
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
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            //Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }

    public void writefogninfo(String filename, List<String> fogn, List<String> agency) {

        WritableSheet writablesheet;
        String PathFile = STOCKDIR+filename+"fogn.xls";;
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
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            //Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }

    public int getTagColumn(String tag) {
        // default 값은  종가는읽게 5로 리턴
        int column=0;
        if(tag.equals("DATE")) column = 0;
        else if(tag.equals("OPEN")) column = 1;
        else if(tag.equals("HIGH")) column = 2;
        else if(tag.equals("LOW")) column = 3;
        else if(tag.equals("CLOSE")) column = 4;
        else if(tag.equals("ADJCLOSE")) column = 5;
        else if(tag.equals("VOLUME")) column = 6;
        else column = 5;

        return column;
    }

    public List<String> oa_readItem(String filename, String tag, boolean header) {

        int column = getTagColumn(tag);
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = STOCKDIR+filename;;
        List<String> pricebuffer = new ArrayList<String>();


        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int start = 0;
                    if(header != true) start = 1;
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }

        return pricebuffer;
    }


    public List<String> readhistory(String stock_code, String tag, int days, boolean header) {

        // data 저장순서는 현재>과거순으이다, 60일치를 읽으려면 0부터 60개를 읽으면 된다
        int column = getTagColumn(tag);
        InputStream is=null;
        Workbook wb=null;
        int maxcol;

        String PathFile = STOCKDIR+stock_code+".xls";;
        List<String> pricebuffer = new ArrayList<String>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int start = 0;
                    if(header != true) start = 1;
                    // -1 : data를 max로 읽으려면 헤더때문에 -1을 해줘야 한다
                    maxcol = sheet.getColumn(column).length-1;
                    // days가 maxcol을 넘어가거나 -1보다 작으면 maxcol로 읽는다
                    if(days >= maxcol || days <= -1) days = maxcol;
                    for(int i=start;i<days+start;i++) {
                        // formatOA class의 구조로 저장된다
                        // 종가는 6번째 컬럼의 값
                        pricebuffer.add(sheet.getCell(column, i).getContents());
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }

        return pricebuffer;
    }


    public List<List<String>> readStockDic() {
        List<String> STOCK_NO = new ArrayList<String>();
        List<String> STOCK_NAME = new ArrayList<String>();
        List<List<String>> diclist = new ArrayList<List<String>>();
        InputStream is=null;
        Workbook wb=null;

        String PathFile = STOCKDIR+"stocktable.xls";;

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int size = sheet.getColumn(0).length;
                    for (int i = 1; i <= size-1; i++) {
                        STOCK_NO.add(sheet.getCell(1, i).getContents());
                        STOCK_NAME.add(sheet.getCell(3, i).getContents());
                    }
                }
                Sheet sheet1 = wb.getSheet(1);   // 시트 불러오기
                if(sheet1 != null) {
                    // line1, col1에서 contents를 읽는다.
                    int size1 = sheet1.getColumn(0).length;
                    for (int i = 1; i <= size1-1; i++) {
                        STOCK_NO.add(sheet1.getCell(1, i).getContents());
                        STOCK_NAME.add(sheet1.getCell(3, i).getContents());
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        diclist.add(STOCK_NO);
        diclist.add(STOCK_NAME);
        return diclist;
    }


    public Boolean file_check(String filename) {

        String PathFile = STOCKDIR + filename;
        Boolean return_flag=false;

        try {
            java.io.File file1 = new java.io.File(PathFile);
            // 1. check if the file exists or not
            boolean isExists = file1.exists();

            if (isExists) {
                return_flag = true;
            } else {
                return_flag = false;
            }
        } catch (Exception e) {

        }
        return return_flag;
    }


    public List<FormatTestData> readtestset(String stock_code, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = STOCKDIR+stock_code+".xls";;
        List<FormatTestData> testdatalist = new ArrayList<FormatTestData>();
        FormatTestData testdata = new FormatTestData();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
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
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }

        return testdatalist;
    }
    public List<FormatTestData> read_testdata(String code, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String filename = code+"_testset";
        String PathFile = STOCKDIR+filename+".xls";;
        List<FormatTestData> testdatalist = new ArrayList<FormatTestData>();
        FormatTestData testdata = new FormatTestData();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }

        return testdatalist;
    }


    public List<FormatTestData> readtestsell(String code, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String filename = code+"_testset";
        String PathFile = STOCKDIR+filename+".xls";;
        List<FormatTestData> testdatalist = new ArrayList<FormatTestData>();
        FormatTestData testdata = new FormatTestData();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int size = sheet.getColumn(0).length;
                    int start = 0;
                    if(header != TRUE) start = 1;
                    for(int i=start;i<size;i++) {
                        // sell quantity를 읽는ㄴ다
                        testdata = new FormatTestData();
                        testdata.date = sheet.getCell(0, i).getContents();
                        testdata.price = sheet.getCell(1, i).getContents();
                        testdata.sell_quantity = sheet.getCell(3, i).getContents();
                        testdatalist.add(testdata);
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }

        return testdatalist;
    }

    public void write_testdata(String code, List<String> date ,List<String> price,List<Integer> buy, List<Integer> sell) {

        String filename = code+"_testset";
        String PathFile = STOCKDIR+filename+".xls";;
        WritableSheet writablesheet;

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
                    // header 때문에 1부터 시작해야 한다
                    int size = buy.size();
                    for(int row =1;row < size+1 ;row++) {
                        writablesheet.addCell(new Label(0, row, date.get(row-1)));
                        writablesheet.addCell(new Label(1, row, price.get(row-1)));
                        writablesheet.addCell(new Label(2, row, String.valueOf(buy.get(row-1))));
                        writablesheet.addCell(new Label(3, row, String.valueOf(sell.get(row-1))));
                    }
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            //Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }


    public List<FormatTestData> removeHeader(List<FormatTestData> input) {
        List<FormatTestData> temp = new ArrayList<FormatTestData>();
        int size = input.size();
        for(int i = 0;i< size-1; i++ ) {
            temp.add(input.get(i+1));
        }
        return temp;
    }

    public void writestockinfo(List<FormatStockInfo> information) {

        WritableSheet writablesheet;
        WritableWorkbook workbook;
        String PathFile = STOCKDIR+"stockinfo"+".xls";;
        java.io.File file1 = new java.io.File(PathFile);

        // 헤더를 붙여준다
        FormatStockInfo info1 = new FormatStockInfo();
        info1.setHeader();
        information.add(0,info1);

        try {
            // 오픈한 파일은 엑셀파일로 바꾸고
            workbook = Workbook.createWorkbook(file1);
            //Toast.makeText(getActivity(), " workbook open ok", Toast.LENGTH_SHORT).show();

            if(workbook != null) {
                //Toast.makeText(getContext(), " write ready ", Toast.LENGTH_SHORT).show();
                workbook.createSheet("sheet0", 0);
                writablesheet = workbook.getSheet(0);
                //Toast.makeText(getContext(), " sheet open ok", Toast.LENGTH_SHORT).show();
                int size = information.size();
                if(writablesheet != null) {
                    for(int row =0;row<size;row++) {
                        writablesheet.addCell(new Label(0, row, information.get(row).stock_code));
                        writablesheet.addCell(new Label(1, row, information.get(row).stock_name));
                        writablesheet.addCell(new Label(2, row, information.get(row).per));
                        writablesheet.addCell(new Label(3, row, information.get(row).per12));
                        writablesheet.addCell(new Label(4, row, information.get(row).area_per));
                        writablesheet.addCell(new Label(5, row, information.get(row).pbr));
                        writablesheet.addCell(new Label(6, row, information.get(row).div_rate));
                        writablesheet.addCell(new Label(7, row, information.get(row).fogn_rate));
                        writablesheet.addCell(new Label(8, row, information.get(row).beta));
                        writablesheet.addCell(new Label(9, row, information.get(row).op_profit));
                        writablesheet.addCell(new Label(10, row, information.get(row).cur_price));
                        writablesheet.addCell(new Label(11, row, information.get(row).score));
                        writablesheet.addCell(new Label(12, row, information.get(row).desc));
                    }
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            //Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }

    public List<FormatStockInfo> readStockinfo(boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = STOCKDIR+"stockinfo"+".xls";;
        List<FormatStockInfo> mArrayBuffer = new ArrayList<FormatStockInfo>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
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
                        temp.per = sheet.getCell(2, i).getContents();
                        temp.per12 = sheet.getCell(3, i).getContents();
                        temp.area_per = sheet.getCell(4, i).getContents();
                        temp.pbr = sheet.getCell(5, i).getContents();
                        temp.div_rate = sheet.getCell(6, i).getContents();
                        temp.fogn_rate = sheet.getCell(7, i).getContents();
                        temp.beta = sheet.getCell(8, i).getContents();
                        temp.op_profit = sheet.getCell(9, i).getContents();
                        temp.cur_price = sheet.getCell(10, i).getContents();
                        temp.score = sheet.getCell(11, i).getContents();
                        temp.desc = sheet.getCell(12, i).getContents();
                        mArrayBuffer.add(temp);
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return mArrayBuffer;
    }

    public List<String> readFogninfo(String stock_code, String group, boolean header) {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = STOCKDIR+stock_code+"fogn.xls";;
        if(group.equals("FOGN")) col = 1;
        else if(group.equals("AGENCY")) col = 2;
        else col = 1;

        List<String> Buffer = new ArrayList<>();
        List<String> Buffer_rev = new ArrayList<>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
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
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return Buffer_rev;
    }

    public String readTreemap() {

        StringBuilder  sb = new StringBuilder();
        String PathFile = STOCKDIR+"treemap.xls";;
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void writehtml(String html_string) {


        String PathFile = "file:///android_asset/" + "treemap" + ".html";
        try {
            File file = new File(PathFile); // File객체 생성
            FileWriter fw = new FileWriter(file, false);
            fw.write(html_string);
            fw.close();
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }
    public void writetext(String csv_string) throws IOException {

        String PathFile = STOCKDIR+"mycsv.txt";

        try {
            File file = new File(PathFile); // File객체 생성
            FileWriter fw = new FileWriter(file, false);
            csv_string = csv_string.replaceAll(" ", "\n");
            fw.write(csv_string);
            fw.close();
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }

    }
    public List<FormatOHLCV> ReadFile() throws IOException {

        List<FormatOHLCV> csvlist = new ArrayList<>();
        try{
            String PathFile = STOCKDIR+"mycsv.txt";
            //파일 객체 생성
            File file = new File(PathFile);
            //입력 스트림 생성
            FileReader filereader = new FileReader(file);
            //입력 버퍼 생성
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            bufReader.readLine();
            bufReader.readLine();
            while((line = bufReader.readLine()) != null){
                //System.out.println(line);
                FormatOHLCV oneline = new FormatOHLCV();
                String[] splitstring = line.split(",");
                oneline.date = splitstring[0];
                oneline.open = splitstring[1];
                oneline.high = splitstring[2];
                oneline.low = splitstring[3];
                oneline.close = splitstring[4];
                oneline.adjclose = splitstring[5];
                oneline.volume = splitstring[6];
                csvlist.add(oneline);
            }
            //.readLine()은 끝에 개행문자를 읽지 않는다.
            bufReader.close();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }catch(IOException e){
            System.out.println(e);
        }
        return csvlist;
    }

    public List<String> read_ohlcv(String stock_code, String tag, int days, boolean header) {
        List<String> result = readhistory( stock_code,  tag,  days,  header);
        return result;
    }


    public void write_ohlcv( String filename, List<FormatOHLCV> src_csvlist) {

        List<FormatOHLCV> csvlist = new ArrayList<>();
        csvlist = src_csvlist;

        WritableSheet writablesheet;
        String PathFile = STOCKDIR+filename+".xls";;
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
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            //Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }


    public void writeSimullist(List<String> stocklist) {

        String filename = "simul_list";
        WritableSheet writablesheet;
        String PathFile = STOCKDIR+filename+".xls";;
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
                    int size = stocklist.size();
                    for(int row =0;row < size ;row++) {
                        writablesheet.addCell(new Label(0, row, stocklist.get(row)));
                    }
                }
            }
            workbook.write();
            workbook.close();
            //Toast.makeText(getContext(), "init excel write ok", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(getContext(), "io error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            //Toast.makeText(getContext(), "write error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }
    public List<String> readSimullist() {
        InputStream is=null;
        Workbook wb=null;
        String contents1=null;
        int line, col;
        String PathFile = STOCKDIR+"simul_list"+".xls";;
        List<String> stocklist = new ArrayList<String>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int size = sheet.getColumn(0).length;
                    for(int i=0;i<size;i++) {
                        stocklist.add(sheet.getCell(0, i).getContents());
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return stocklist;
    }

}
