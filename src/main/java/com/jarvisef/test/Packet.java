package com.jarvisef.test;

import java.io.*;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;

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

    public Packet() {

    }

    public static void main(String[] args) throws Exception {

        if(args!=null) {
            for(int i=0; i<args.length; i++) {
                System.out.println("args["+i+"]="+args[i]);
            }
        }

        String strCco_c = args[0];
        String strRecode_DV = args[1];
        String strDate = args[2];

        String o_result_code = null;

//        FileWriter fw = new FileWriter("d:/out.txtt");
//        for (int i = 0; i < 10; i++) {
//            String data = i + 1 + " 번째 줄입니다.\r\n";
//            fw.write(data);
//        }
//        fw.close();

        File fws = new File("D:/전문파일테스트/HFG9000_10_20150706141414_10.sam");
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fws), CommonConstants.CHARSET_EUC_KR));

        String Head = "H000000000HFG900" + CommonUtil.getRPadd("HFG9000_10_20150706141414_10.sam", 35) + CommonUtil.getDate(CommonConstants.DATE_DTTI) + CommonUtil.getBlank(434);

        int seqNo;
        for (seqNo = 1; seqNo < 300000; seqNo++) {
            String data = "D" + CommonUtil.getLPadd(String.valueOf(seqNo), 9, "0") + "HFG000001036201506YNNNNYN201507061414200";
            out.newLine();
            out.write(data);
        }
        String Tail = "H999999999HFG900" + CommonUtil.getLPadd(String.valueOf(seqNo - 1), 7) + CommonUtil.getBlank(476);
        out.newLine();
        out.write(Tail);

        out.close();
        System.exit(1);


        DBHandler db = new DBHandler();
        db.tibero.setAutoCommit(true);

       FileUtils.getBatchStepStatus(db.tibero, strCco_c, strRecode_DV, strDate);

        if (strRecode_DV != null) {

            boolean bSuccess  = FileUtils.setBatchLog(strCco_c, strRecode_DV, strDate);

            FileUtils.RECURSIVE_FILE(DirectoryDefine.CCO_DirPath(strCco_c, ""));

            FileUtils.strStep = "10";
//            WorkStep.Step1 = 10;

            switch (FileUtils.strStep) {
                case "10" :
                    // 임시테이블 INSERT
                    //FileParse();


                    for (String sourceFile : FileUtils.retFileList) {
                        System.out.println(String.format("%s", sourceFile));

                        FileUtils.readFileAsListOfStrings(sourceFile);
                    }

                    //Collections.reverse(FileUtils.retFileList);
                    FileUtils.retFileList.clear();
                    System.out.println("초기화 완료");
                    System.out.println("");

                    System.exit(1);
                    break;
                case "20":
                    // 업무 처리 SP호출
                    //CallSP();


                    FileUtils.CallProcessSP(strCco_c, strRecode_DV, strDate);

                    System.exit(1);

                    break;
                case "30":
                    // 파일 생성
                    //CreateFile();
                    FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

                    FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END);
                    System.exit(1);
                    break;
                case "40":
                    // 종료
                    FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

                    FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END);
                    System.exit(1);
                    break;
            }
        }
    }

    public enum WorkStep {
        Step1(10), Step2(20), Step3(30), Step4(40);
        private int value;

        private WorkStep(int value) {
            this.value = value;
        }
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

