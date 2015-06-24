package com.jarvisef.test;

import java.io.UnsupportedEncodingException;
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

    public static void main(String[] args) throws Exception {
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

//        DBHandler db = new DBHandler();
//        db.tibero.setAutoCommit(true);

//        CallableStatement cstmt = db.tibero.prepareCall("{call MEM_IF_FILE(?, ?, ?, ?, ?, ?, ?, ?, ?}");
//        cstmt.registerOutParameter(1, Types.VARCHAR);
//        cstmt.registerOutParameter(2, Types.VARCHAR);
//        cstmt.registerOutParameter(3, Types.VARCHAR);
//        cstmt.registerOutParameter(4, Types.VARCHAR);
//        cstmt.registerOutParameter(5, Types.VARCHAR);
//        cstmt.registerOutParameter(6, Types.VARCHAR);
//        cstmt.setString(7, "적립 파일 전문");
//        cstmt.setString(8, "21");
//        cstmt.registerOutParameter(9, DdataType);

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


//    public static String subString(String str, int startIndex, int length)
//    {
//        byte[] b1 = null;
//        byte[] b2 = null;
//
//        try
//        {
//            if (str == null)
//            {
//                return "";
//            }
//
//            b1 = str.getBytes();
//            b2 = new byte[length];
//
//            if (length > (b1.length - startIndex))
//            {
//                length = b1.length - startIndex;
//            }
//
//            System.arraycopy(b1, startIndex, b2, 0, length);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return new String(b2);
//    }

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

