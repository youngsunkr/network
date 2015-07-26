package com.jarvisef.test;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-06-06.
 */
public class FileUtils {

    /**
     * Open and read a file, and return the lines in the file as a list of
     * Strings.
     */
    public static void readFileAsListOfStrings(String sourceFile) throws Exception {

        FileUtils.setConsoleLog(ConsoleLogType.BGN);

        FileUtils.setBatchLog(
                FileUtils.strCCO_C
                , FileUtils.strProgram_Id
                , "20"
        );

        DBHandler db = new DBHandler();
        db.tibero.setAutoCommit(false);

        List<String> orgRowData = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile),CommonConstants.CHARSET_EUC_KR));

        /* 업무변수 선언 */
        int intSeq = 1;
        boolean bSuccess = false;
        String strFilename = null;
        String strCCO_C = null;
        String strRecode_DV = null;
        StringBuffer sbInsertSql = null;
        boolean o_result_status = false;
        String readLine = null;
        String txtName ="abc";

        int file = sourceFile.lastIndexOf("/");
        int ext = sourceFile.lastIndexOf(".");
        int fileLength = sourceFile.length();
        String filename = sourceFile.substring(file + 1, ext);
        String extname = sourceFile.substring(ext + 1, fileLength);
        String fullfilename = filename + "." + extname;

        String strFileName = sourceFile.substring(sourceFile.lastIndexOf("\\" + 1, sourceFile.length()));
        String strFileNameWithoutExt = strFileName.substring(0, strFileName.lastIndexOf("."));

        System.out.println(strFileName);
        System.out.println(strFileNameWithoutExt);

        Statement stmtIns = null;
        try {
            while ((readLine = reader.readLine()) != null) {

                /*
                * 파일전문 임시테이블 Insert SP 호출
                * */
                ResultSetMetaData rsmd = null;

                Packet data = new Packet();

                if (readLine.substring(0, 1).toUpperCase().contains("H")) {

                    System.out.println(readLine);

                    sbInsertSql = new StringBuffer();
                    sbInsertSql.append("");

                    bSuccess = stmtIns.execute(sbInsertSql.toString());
                } else if (readLine.substring(0, 1).contains("D")) {

                    System.out.println(readLine);






                } else if (readLine.substring(0, 1).toUpperCase().contains("T")) {

                    System.out.println(readLine);

                    sbInsertSql = new StringBuffer();
                    sbInsertSql.append("");

                    bSuccess = stmtIns.execute(sbInsertSql.toString());
                }

                orgRowData.add(readLine);
            }
            reader.close();

//            db.tibero.commit();
//            db.tibero.setTransactionIsolation(db.tibero.TRANSACTION_READ_COMMITTED);
//            if (db.tibero != null) db.tibero.close();

            File readFileName = new File(sourceFile);
            for (String list : orgRowData) {
                // 파일 저장
                FileUtils.writeFile(readFileName.getAbsolutePath(), String.format("%sreaultCode0000", list));
            }

            // 파일 복사
            File source = new File(DirectoryDefine.RCV + "/" + txtName);
            File copyFile = new File(DirectoryDefine.SND + "/" + txtName);
            File target = new File(DirectoryDefine.SND + "/" + txtName + ".tmp");
            File sndBackup = new File(DirectoryDefine.SND_BACKUP + "/" + txtName);

            if (!source.exists()) {
//                source.mkdir();
                copyFile(readFileName, source);
            }
            if (!copyFile.exists()) {
//                copyFile.mkdir();
                copyFile(source, target);
            }

            // 파일명 변경
            target.renameTo(new File(DirectoryDefine.SND + "/" + txtName));

            // 최종 파일 제거
            readFileName.delete();

        } catch (IOException e) {
//            if (db.tibero != null) {
//                db.tibero.rollback();
//                db.tibero.close();
//            }
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        FileUtils.setConsoleLog(ConsoleLogType.END);
    }

    /**
     * Save the given text to the given filename.
     *
     * @param canonicalFilename Like /Users/al/foo/bar.txt
     * @param text              All the text you want to save to the file as one String.
     * @throws IOException
     */
    public static void writeFile(String canonicalFilename, String text)
            throws IOException {


//        FileWriter fw = new FileWriter(fileName);
//        BufferedWriter bw = new BufferedWriter(fw,1024);

        File file = new File(canonicalFilename);
        //BufferedWriter out = new BufferedWriter(new FileWriter(file));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), CommonConstants.CHARSET_EUC_KR));

        try {
            out.newLine();  // line feed
            out.write(text);
            out.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void copyFile(File source, File destination) throws IOException {
        //if (!source.isFile() || !dest.isFile()) return false;

        byte[] buffer = new byte[100000];

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(source));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
            int size;
            while ((size = bufferedInputStream.read(buffer)) > -1) {
                bufferedOutputStream.write(buffer, 0, size);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            // TODO may want to do something more here
            throw e;
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
            } catch (IOException ioe) {
                // TODO may want to do something more here
                throw ioe;
            }
        }
    }

    public static List<String> retFileList = new ArrayList<String>();
    public static boolean isTempInsert = false;
    public static String strStep = null;
    public static String strStatus = null;
    public static String strCCO_C = new String();
    public static String strProgram_Id = null;
    public static String o_result_code = null;
    public static String o_result_args = null;
    public static String o_result_msg = null;
    public static void RECURSIVE_FILE(String source){
        File dir = new File(source);
        File[] fileList = dir.listFiles();
        try {

            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                if (file.isFile()) {
                    if (file.getName().indexOf("HFG") > -1 && file.getName().toUpperCase().endsWith(".SAM")) {
                        // 파일이 있다면 파일 이름 출력
                        System.out.println("\t 파일 이름 = " + file.getName());
                        //retFileList.add(file.getName());
                        retFileList.add(file.getPath());
                    }
                } else if (file.isDirectory()) {
                    if (file.getName().indexOf("HFG") > -1 || file.getName().toUpperCase().equals("RCV")) {
                        System.out.println("디렉토리 이름 = " + file.getName());
                        // 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색
                        RECURSIVE_FILE(file.getCanonicalPath().toString());
                    }
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static final int getByteSizeToComplex(String str) {
        int en = 0;
        int ko = 0;
        int etc = 0;

        char[] string = str.toCharArray();

        for (int j = 0; j < string.length; j++) {
            if (string[j] >= 'A' && string[j] <= 'z') {
                en++;
            } else if (string[j] >= '\uAC00' && string[j] <= '\uD7A3') {
                ko++;
                ko++;
            } else {
                etc++;
            }
        }
        return (en + ko + etc);
    }

    public static void CallProcessSP(String strCco_C, String strRecode_DV, String strDate) {

        setConsoleLog(ConsoleLogType.BGN);

        setConsoleLog(ConsoleLogType.END);

    }

    public static void getBatchStepStatus(Connection tibero, String strCco_c, String strRecode_dv, String strDate) throws SQLException {
        setConsoleLog(ConsoleLogType.BGN);

        DBHandler db = new DBHandler();
        CallableStatement cStmt = null;
        String strProcessSP = null;
        String retFileName = null;

        Statement stmt = db.tibero.createStatement();
        ResultSet rs = null;
        try {

            StringBuffer sbInsertSQL= new StringBuffer();
            sbInsertSQL.append("SELECT ");

            rs = stmt.executeQuery(sbInsertSQL.toString());

            while (rs.next()) {
                retFileName = rs.getString(1);
            }

        } catch (SQLException ex) {
            System.out.println("Error[" + ex.getErrorCode() + "] : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (cStmt != null) cStmt.close();
        }

        setConsoleLog(ConsoleLogType.END);
    }

    public static boolean setBatchLog(String strCcoC, String strProgram_Id, String strCl_st_c) throws SQLException {

        boolean bSuccess = false;

        /*
        * 파일전문 인터페이스 정보 SP호출
        * */
        DBHandler db = new DBHandler();
        CallableStatement cStmt = db.tibero.prepareCall("{call MEM_SYS_S_003(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)}");
        cStmt.registerOutParameter(1, Types.VARCHAR);
        cStmt.registerOutParameter(2, Types.VARCHAR);
        cStmt.registerOutParameter(3, Types.VARCHAR);

        cStmt.setString(4, CommonUtil.getDate(CommonConstants.DATE_MS));
        cStmt.setString(5, "BATCH");
        cStmt.setString(6, CommonUtil.getDate(CommonConstants.DATE_DTTI));
        cStmt.setString(7, "C");
        cStmt.setString(8, strProgram_Id);
        cStmt.setString(9, strCcoC);
        cStmt.setString(10, CommonUtil.getDate(CommonConstants.DATE_DT));
        cStmt.setString(11, CommonUtil.getDate(CommonConstants.DATE_DT));
        cStmt.setString(12, CommonUtil.getDate(CommonConstants.DATE_DT));
        cStmt.setString(13, CommonUtil.getDate(CommonConstants.DATE_DTTI));
        cStmt.setString(14, CommonUtil.getDate(CommonConstants.DATE_DTTI));
        cStmt.setString(15, strCl_st_c);
        cStmt.setString(16, "0");
        cStmt.setString(17, "0");
        cStmt.setString(18, "0");
        cStmt.setString(19, "0");
        cStmt.setString(20, "0");

        try {

            cStmt.executeUpdate();

            o_result_code = cStmt.getString(1);
            o_result_args = cStmt.getString(2);
            o_result_msg = cStmt.getString(3);

            if (o_result_code.equals("00000")) bSuccess = true;

            cStmt.close();
            db.tibero.close();
        } catch (SQLException ex) {
            System.out.println("Error[" + ex.getErrorCode() + "] : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cStmt != null) cStmt.close();
            if (db.tibero != null) db.tibero.close();
        }

        return bSuccess;
    }

    public static void setExecuteLog(String strCCO_C, String strRecode_DV, String strDate) {
        System.out.println(String.format("[EXE] java -jar Batch.jar %s %s %s", strCCO_C, strRecode_DV, strDate));
    }

    public static void setConsoleLog(ConsoleLogType consoleLogType) {
        if (consoleLogType.equals(ConsoleLogType.BGN))
            System.out.println(String.format("[BGN][%s][서비스시작 => %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD), strProgram_Id));

        if (consoleLogType.equals(ConsoleLogType.END))
            System.out.println(String.format("[BGN][%s][서비스종료 => %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD), strProgram_Id));
    }

    public enum STEP {
        TempTableInsert(10),
        CallProcessSP(20),
        FileWrite(30),
        Complete(40);
        private int value;

        private STEP(int value) {
            this.value = value;
        }
    };

    public enum STATUS {
        TempTableInsert(10),
        CallProcessSP(20),
        FileWrite(30),
        Complete(40);
        private int value;

        private STATUS(int value) {
            this.value = value;
        }
    };

    public enum ConsoleLogType {
        BGN,
        END
    }



}

