package com.jarvisef.test;

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

    public String raw() {
        StringBuffer result = new StringBuffer();
        for(Item item: items) {
            result.append(item.raw());
        }
        return result.toString();
    }

    public void parse(String data) {
        byte[] bdata = data.getBytes();
        int pos = 0;
        for(Item item: items) {
            byte[] temp = new byte[item.getLength()];
            System.arraycopy(bdata, pos, temp, 0, item.getLength());
            pos += item.getLength();
            item.setValue(new String(temp));
        }
    }

    public static void main(String[] args) throws Exception {
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

        // 1. Head
        // 2. Data
        // 3. Tail

        // Common("Head", Parsing);
        // Common (Parsing function);
        // Common("Tail", Parsing);

        //

        //System.out.println(recvPacket.getItem("주소").raw());

        //System.out.println(subString(recvPacket.getItem("생일").raw(), 0, 3));

        System.out.println(StringHelper.subString(recvPacket.getItem("생일").raw(), 0, 3));



//        Scanner s = new Scanner(new File("filepath"));
//        ArrayList<String> list = new ArrayList<String>();
//        while (s.hasNext()){
//            list.add(s.next());
//        }
//        s.close();

        List<String> readDataList = FileUtils.readFileAsListOfStrings("D:/20150606전문.dat");

        //FileUtils.readFileByLines("D:/20150606전문.dat");

        /*
        * 문자열 검색
        * case1 : indexOf
        * case2 : contains
        * case3 : matches
        * */
//        if (readDataList.get(0).toUpperCase().indexOf("[HEAD]") > -1) {
//            System.out.println(readDataList.get(0).toUpperCase().indexOf("[HEAD]"));
//        }
//
//        if (readDataList.get(0).toUpperCase().contains("[HEAD]")) {
//            System.out.println(readDataList.get(0).toUpperCase());
//        }

//        if (readDataList.get(0).matches(".*[HEAD].*")) {
//            System.out.println(readDataList.get(0).toUpperCase());
//        }

        //System.out.println(Arrays.toString(list.toArray()));
        //System.out.println(Arrays.toString(readDataList.toArray()));

//        for (String s : readDataList) {
//            System.out.println(String.format("%s aaaa", s));
//
//            FileUtils.writeFile("d:/123.dat", String.format("%s aaaa", s));
//        }


    }


    public String getItem(String div, String data) {

        String retVal = null;

        if (div.toUpperCase().equals("HEAD")) {
            // Head

        } else if (div.toUpperCase().equals("TAIL")) {
            // Tail

        } else {
            // Data

        }
        return retVal.toString();
    }

    public String getItem(String div, String data, int length) {

        String retVal = null;

        if (div.toUpperCase().equals("HEAD")) {
            // Head

        } else if (div.toUpperCase().equals("TAIL")) {
            // Tail

        } else {
            // Data

        }

        return retVal.toString();
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

