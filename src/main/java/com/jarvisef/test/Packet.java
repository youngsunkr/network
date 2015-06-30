package com.jarvisef.test;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by youngsunkr on 2015-06-01.
 */
public class Packet {
    private ArrayList<Item> items = new ArrayList<Item>();
    private HashMap<String, Item> nameAccess = new HashMap<String, Item>();

    public void addItem(Item item) {
        this.items.add(item);
        if(nameAccess.containsKey(item.getName())) {
            throw new RuntimeException(
                    "Duplicated item name:["+item.getName()+"]");
        }
        nameAccess.put(item.getName(), item);
    }

    public Item getItem(int index) {
        return this.items.get(index);
    }

    public Item getItem(String name) {
        return nameAccess.get(name);
    }

    public String raw()  throws UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        for(Item item: items) {
            result.append(item.raw());
        }
        return result.toString();
    }

    public void parse(String data) throws UnsupportedEncodingException {
        byte[] bdata = data.getBytes("EUC-KR");
        int pos = 0;
        for(Item item: items) {
            byte[] temp = new byte[item.getLength()];
            System.arraycopy(bdata, pos, temp, 0, item.getLength());
            pos += item.getLength();
            item.setValue(new String(temp, "EUC-KR"));
        }
    }

    private static WorkStep workStep;
    private static CCO_C cco_c;

    public Packet() {

    }
    public Packet(WorkStep workStep) {
        this.workStep = workStep;
    }
    public static void main(String[] args) throws Exception {

        if(args!=null) {
            for(int i=0; i<args.length; i++) {
                System.out.println("args["+i+"]="+args[i]);
            }
        }

//        FileWriter fw = new FileWriter("d:/out.txtt");
//        for (int i = 0; i < 10; i++) {
//            String data = i + 1 + " 번째 줄입니다.\r\n";
//            fw.write(data);
//        }
//        fw.close();

        Packet packet = new Packet();
        Item item1 = Item.create("이름", 10, "홍길동");
        Item item2 = Item.create("전화번호", 11, "01099998888");
        packet.addItem(item1);
        packet.addItem(item2);
        System.out.println("["+packet.raw()+"]");

        Packet recvPacket = new Packet();
        recvPacket.addItem(Item.create("생일", 8, null));
        recvPacket.addItem(Item.create("주소", 30, null));
        recvPacket.parse("19801215서울시 송파구 잠실동 123-3    ");

        System.out.println(recvPacket.getItem(1).raw());
        System.out.println(recvPacket.getItem("주소").raw());
        //System.out.println(recvPacket.getItem("주소").raw());
        //System.out.println(subString(recvPacket.getItem("생일").raw(), 0, 3));

        //System.out.println(StringHelper.subString(recvPacket.getItem("생일").raw(), 0, 3));
        System.out.println(recvPacket.getItem("생일").raw());


//        Scanner s = new Scanner(new File("filepath"));
//        ArrayList<String> list = new ArrayList<String>();
//        while (s.hasNext()){
//            list.add(s.next());
//        }
//        s.close();

        //List<String> readDataList = FileUtils.readFileAsListOfStrings("D:/20150606전문.dat");
        //List<String> readDataList = FileUtils.subDirList(DirectoryDefine.base_Directory);


//        //c:\에 있는 모든 파일
//        File fl=new File(DirectoryDefine.base_Directory);
//
//        FileUtils.RECURSIVE_FILE(fl);

        DBHandler db = new DBHandler();
        db.tibero.setAutoCommit(true);

//        CallableStatement cstmt = db.tibero.prepareCall("{call MEM_IF_FILE(?, ?, ?, ?, ?, ?, ?, ?, ?}");
//        cstmt.registerOutParameter(1, Types.VARCHAR);
//        cstmt.registerOutParameter(2, Types.VARCHAR);
//        cstmt.registerOutParameter(3, Types.VARCHAR);
//        cstmt.registerOutParameter(4, Types.VARCHAR);
//        cstmt.registerOutParameter(5, Types.VARCHAR);
//        cstmt.registerOutParameter(6, Types.VARCHAR);
//        cstmt.setString(7, "적립 파일 전문");
//        cstmt.setString(8, "21");
//        cstmt.registerOutParameter(9, JdbcType.CURSOR.TYPE_CODE);

//        try{
//            cstmt.execute();
//        } catch(SQLException ex) {
//            System.out.println("ERROR[" + ex.getErrorCode() + " : " + ex.getMessage());
//            ex.printStackTrace();
//        }
//
//        String o_result_code = cstmt.getString(1);
//        System.out.printf(o_result_code);
//
//        String o_result_args = cstmt.getString(2);
//        System.out.printf(o_result_args);
//
//        String o_result_msg = cstmt.getString(3);
//        System.out.printf(o_result_msg);

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //3. sql문 처리 - PreparedStatement
            String sql="select * from pd order by no desc";
            ps = db.tibero.prepareStatement(sql);

            //4. 실행
            rs = ps.executeQuery();
            //ResultSet 메타 데이터 받아오기
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount(); // 컬럼갯수
            System.out.println("컬럼수:"+colCount);

            for(int i=1;i<=colCount;i++){
                String colName = rsmd.getColumnName(i); // 컬럼명
                String colTypeName = rsmd.getColumnTypeName(i); // 컬럼의 타입명
                int colDisplaySize = rsmd.getColumnDisplaySize(i); // 디스플레이 사이즈
                int iNull = rsmd.isNullable(i);
                String sNull = iNull==0?"not null":"null";

                System.out.println("컬럼명:"+colName+",컬럼타입:"+colTypeName);
                System.out.println("디스플레이 사이즈:"+colDisplaySize);
                System.out.println("null 허용여부"+iNull+","+sNull);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                if(rs!=null)rs.close();
                if(ps!=null)ps.close();
                if(db.tibero !=null) db.tibero.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        switch (workStep) {
            case Step1:
                // 임시테이블 INSERT
                //FileParse();
                break;
            case Step2:
                // 업무 처리 SP호출
                //CallSP();
                break;
            case Step3:
                // 파일 생성
                //CreateFile();
                break;
            case Step4:
                // 종료
                break;
        }

        switch (args[0].toUpperCase()) {
            case "STEP1":
                // 임시테이블 INSERT
                //FileParse();
                break;
            case "STEP2":
                // 업무 처리 SP호출
                //CallSP();
                break;
            case "STEP3":
                // 파일 생성
                //CreateFile();
                break;
            case "STEP4":
                // 종료
                break;
        }



        FileUtils.RECURSIVE_FILE(DirectoryDefine.base_Directory);
        for (String s : FileUtils.retFileList) {
            System.out.println(String.format("%s", s));

            List<String> readDataList = FileUtils.readFileAsListOfStrings(s);
        }

        //Collections.reverse(FileUtils.retFileList);
        FileUtils.retFileList.clear();
        System.out.println("초기화 완료");
        System.out.println("");
    }

    public enum WorkStep {
        Step1, Step2, Step3, Step4
    }
    public enum CCO_C {
        HFG1000, HFG2000, HFG3000, HFG4000,
        HFG5000, HFG6000, HFG7000
    }

    public static String getCutUTFString(String str, int len,String tail){
        if( str.length() <= len){
            return str;
        }
        StringCharacterIterator sci = new StringCharacterIterator(str);
        StringBuffer buffer = new StringBuffer();
        buffer.append(sci.first());
        for(int i=1; i<len ; i++){
            if( i < len-1){
                buffer.append(sci.next());
            }else{
                char c=sci.next();
                if(c != 32){ //마지막 charater가 공백이 아닐경우
                    buffer.append(c);
                }
            }
        }
        buffer.append(tail);
        return buffer.toString();
    }
}

