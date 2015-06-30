package com.jarvisef.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by youngsunkr on 2015-06-15.
 */
public class HANACARD_Batch implements Runnable {
    private static boolean isRun = true;
    private static boolean isLocking = false;
    private static String devOrReal = "D";
    public HANACARD_Batch() {
        isRun = true;
        isLocking = false;
        new Thread(this).start();
    }

    public enum CCO_C {
        HFG1000, HFG2000, HFG3000, HFG4000,
        HFG5000, HFG6000, HFG7000
    }

    public static void main(String[] args) throws Exception {

        if(args!=null) {
            for(int i=0; i<args.length; i++) {
                System.out.println("args["+i+"]="+args[i]);
            }
        }

        HANACARD_Batch processor = new HANACARD_Batch();
        System.out.println("시작");

        processor.stop();
    }

    public void run() {
        while (isRun) {

            try {


                //모듈실행 (lock 이 걸려있지 않다면)
                if(!isLocking) {
                    isLocking = true;
                    //SmcrdMakePrcsKB smcrdMP = new SmcrdMakePrcsKB();
                    String fileName = ReceiveDataFileList();
                    String cco_cd = "";
                    String in_user = "";
                    String in_ip = "";

                    if (fileName.length() > 0) {

                        String classpathDir = "/jeus/jeus5/webhome/app_home/gsbonus/WEB-INF/lib";

                        String classpath = classpathDir+"/ojdbc14.jar:"+
                                classpathDir+"/jwork.jar:"+
                                classpathDir+"/commons-configuration-1.1.jar:"+
                                classpathDir+"/commons-digester-1.7.jar:"+
                                classpathDir+"/commons-lang-2.2.jar:"+
                                classpathDir+"/commons-logging-1.1.jar:"+
                                classpathDir+"/commons-collections-3.2.jar:"+
                                classpathDir+"/commons-beanutils-1.7.0.jar:"
                                ;

                        cco_cd = "5200";
                        in_user = "system";
                        in_ip = "127.0.0.1";

                        String command = " /opt/java1.7/bin/java -Dfile.encoding=KSC5601 -classpath "+classpath+"/jeus/jeus5/webhome/app_home/gsbonus/WEB-INF/classes/ " +
                                "SmcrdMakePrcs"+" "+fileName+" " +cco_cd+" " +in_user+" " +in_ip
                                ;

                        System.out.println("command : "+command);

//                        Runtime operator = Runtime.getRuntime();
//                        Process process = operator.exec(command);

                        /*  자바 1.4 이하에서 동작 방식 */
                        Process process = Runtime.getRuntime().exec(command);

                        /*  자바 1.5 이상에서 동작 방식 */
//                        List<String> cmd = new ArrayList<>();
//                        cmd.add("java");
//                        cmd.add("-classpath");
//                        cmd.add("\"d:/workspace/some project/lib/something.jar\"");
//                        cmd.add("blah.blah.SomeClass");
//                        cmd.add("arg1");
//                        cmd.add("arg2");
//                        //Process process = new ProcessBuilder(cmd).start();
//                        ProcessBuilder process = new ProcessBuilder(cmd);
//                        process.directory(new File("d:/workspace/some project"));
//                        process.start();


                        BufferedReader br = null;
                        br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String temp;
                        System.out.println("--------------------------------------------");
                        System.out.println("  process[InputStream START]");
                        System.out.println("--------------------------------------------");
                        while( (temp = br.readLine()) != null ){
                            System.out.println(temp);
                        }
                        System.out.println("");
                        System.out.println("--------------------------------------------");
                        System.out.println("  process[InputStream END]");
                        System.out.println("--------------------------------------------");
                        System.out.println("");
                        if(br != null){
                            br.close();
                        }

                    }


                    isLocking = false;


                }
                //Thread.sleep(3600000*24);//24시간마다 실행
                Thread.sleep(5 * 1000);// 5초 마다 실행
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRun = false;
        System.out.println("종료");
    }

    public String ReceiveDataFileList() throws Exception {

        Hashtable ruleHt = new Hashtable();
//		String dir = "5200";

        System.out.println("경로:" +DirectoryDefine.DIRECTORY_HANABANK_RCV +"/5200/incoming/");
        String pathname = DirectoryDefine.DIRECTORY_HANABANK_RCV +"/5200/incoming/";

        //incoming디렉토리의 정보를 가져온다.
        File dir = new File(pathname);
        FileInputStream inputStream = null;
        String fileName = "";
        HashMap queryMap = new HashMap();
        try{
            File[] list = dir.listFiles();
            if (list == null)
                return null;
            ArrayList fileList = new ArrayList();

            for (int i=0; i<list.length; i++) {
                if(list[i].isFile()) {
                    HashMap map = new HashMap();
                    //파일명이 F001+ ~~~~ 인 파일만 찾는다
                    //F001080801 , F001080802

                    //2010.01.13
                    //파일명이 F001+ ~~~~ 인 파일중 파일의 송수신상태체크 정상인 것만 파일명 리턴처리함
                    //if((list[i].getName().substring(0, 4).equals("F001"))){
                    if (list[i].getName().indexOf("F001") > -1 && list[i].getName().indexOf("_END") < 0 && list[i].getName().indexOf("tmp") < 0) {
                        fileName = list[i].getName();
                        System.out.println("fileName      :" + fileName );
                    }
                }
            }
        }catch(Exception e){
            throw e;
        }finally{
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return fileName;
    }
}


