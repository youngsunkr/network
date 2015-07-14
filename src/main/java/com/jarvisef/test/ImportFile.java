package com.jarvisef.test;

import org.apache.ibatis.type.JdbcType;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-07-10.
 */
public class ImportFile {

    public static List<String> retFileList = new ArrayList<String>();

    public static void main(String[] args) throws Exception {

        DoWork();

        System.exit(1);
    }

    private static void DoWork() throws Exception {

        RECURSIVE_FILE(DirectoryDefine.base_Directory);

        for (String sourceFile : retFileList) {
            System.out.println(String.format("%s", sourceFile));

            readFileAsListOfStrings(sourceFile);
        }

        // 파일 backup 디렉터리로 이동
        String filename = "", txtName = "";
        File readFileName = new File(filename);
//        for (String list : orgRowData) {
//            // 파일 저장
//            FileUtils.writeFile(readFileName.getAbsolutePath(), String.format("%sreaultCode0000", list));
//        }

        // 파일 복사
        File source = new File(DirectoryDefine.RCV + "/" + txtName);
        File copyFile = new File(DirectoryDefine.RCV_BACKUP + "/" + txtName + ".tmp");

        if (!copyFile.exists()) {
//                copyFile.mkdir();
            FileUtils.copyFile(copyFile, source);
        }

        if (copyFile.exists()) {
            // 파일명 변경
            copyFile.renameTo(new File(DirectoryDefine.RCV_BACKUP + "/" + txtName));

            // 최종 파일 제거
            readFileName.delete();
        }
    }

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

    /**
     * Open and read a file, and return the readLines in the file as a list of
     * Strings.
     */
    public static void readFileAsListOfStrings(String sourceFile) throws Exception {

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

        DBHandler db = new DBHandler();
//        db.tibero.setAutoCommit(false);
        Statement stmtIns = null;

        String txtName = null;
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile),CommonConstants.CHARSET_KCB));

        /* 업무변수 선언 */
        int intSeq                      = 1;
        boolean bSuccess                = false;
        String strCCO_C                 = null;
        String strRecode_DV             = null;
        StringBuffer sbInsertData       = null;
        String readLine                 = null;

        int file            = sourceFile.lastIndexOf("/");
        int ext             = sourceFile.lastIndexOf(".");
        int fileLength      = sourceFile.length();
        String filename     = sourceFile.substring(file+1,ext);
        String extname      = sourceFile.substring(ext+1,fileLength);
        String fullfilename = filename+"."+extname;

        String strFileName             = sourceFile.substring( sourceFile.lastIndexOf('\\')+1, sourceFile.length() );
        String strFilenNmeWithoutExtn  = strFileName.substring(0, strFileName.lastIndexOf('.'));

        System.out.println(strFileName);
        System.out.println(strFilenNmeWithoutExtn);


        try {
            while ((readLine = fileReader.readLine()) != null) {

                /*
                * 파일전문 임시테이블 Insert SP 호출
                * */


                if (readLine.substring(0, 1).toUpperCase().contains("H")) {

                    System.out.println(readLine);

                    sbInsertData = new StringBuffer();
                    sbInsertData.append("");

                    bSuccess = stmtIns.execute(sbInsertData.toString());


                    int pos = strFileName.lastIndexOf(".");
                    String fileExt = strFileName.substring(pos + 1).toLowerCase();
                    //txtName = strFileName.substring(0, pos).concat(".").concat(fileExt);
                    strFileName = strFileName.substring(0, pos).concat(".sam");

                } else if (readLine.substring(0, 1).toUpperCase().contains("D")) {

                    System.out.println(readLine);

                    sbInsertData = new StringBuffer();
                    sbInsertData.append("");

                    bSuccess = stmtIns.execute(sbInsertData.toString());

                } else if (readLine.substring(0, 1).toUpperCase().contains("T")) {

                    System.out.println(readLine);

                    sbInsertData = new StringBuffer();
                    sbInsertData.append("");

                    bSuccess = stmtIns.execute(sbInsertData.toString());

                }

            }
            fileReader.close();

            if (bSuccess) {
                db.tibero.commit();
                db.tibero.setTransactionIsolation(db.tibero.TRANSACTION_READ_COMMITTED);
            } else {
                db.tibero.rollback();
            }

            if (stmtIns != null)
            if (db.tibero != null) db.tibero.close();


        } catch (IOException e) {
            if (db.tibero != null) {
                db.tibero.rollback();
            }
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (db.tibero != null) db.tibero.close();
        }

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END);
    }

    private static Packet getFileInfo(String strProgram_nm, String strRecode_dv, String fileRow) throws SQLException {
        DBHandler db = new DBHandler();

        CallableStatement cStmt = null;
        ResultSet cursor = null;
        Packet data = new Packet();
        Packet cryptoData = new Packet();
        int intSeq = 0;

        /*
        * 파일전문 인터페이스 정보 SP호출
        * */
        cStmt = db.tibero.prepareCall("{call MEM_IF_FILE(?,?,?,?,?, ?,?,?,?,?, ?,?)}");
        cStmt.registerOutParameter(1, Types.VARCHAR);
        cStmt.registerOutParameter(2, Types.VARCHAR);
        cStmt.registerOutParameter(3, Types.VARCHAR);
        cStmt.registerOutParameter(4, Types.VARCHAR);
        cStmt.registerOutParameter(5, Types.VARCHAR);
        cStmt.registerOutParameter(6, Types.VARCHAR);
        cStmt.setString(7, strProgram_nm);
        cStmt.setString(8, strRecode_dv);
        cStmt.registerOutParameter(9, JdbcType.CURSOR.TYPE_CODE);

        try {
            cStmt.executeUpdate();

            cursor = (ResultSet)cStmt.getObject(9);
            while (cursor.next()) {
//                System.out.println(cursor.getString(1));
//                System.out.println("\t" + cursor.getString(2));
//                System.out.println("\t" + cursor.getString(3));
//                System.out.println("\t" + cursor.getString(4));
//                System.out.println("\t" + cursor.getString(5));
//                System.out.println("\t" + cursor.getString(6));
//                System.out.println("\t" + cursor.getString(7));
//                System.out.println("\t" + cursor.getString(8));

                data.addItem(Item.create(cursor.getString(5), (int) cursor.getInt(6), null));
                data.parse(fileRow);

                if (cursor.getString(7).toUpperCase().equals("Y")) {
                    String cryptoItem = CommonUtil.getRPadd(CommonUtil.Crypt(data.getItem(cursor.getString(5)).raw()).trim(), 256);
                    cryptoData.addItem(Item.create(cursor.getString(5), 256, cryptoItem));
                } else {    // 암호화 대상 아닌 컬럼
                    cryptoData.addItem(Item.create(cursor.getString(5), (int) cursor.getInt(6), data.getItem(cursor.getString(5)).raw()));
                }

                System.out.println(String.format("[%s][%s][%s] : [%s]"
                        , intSeq
                        , cursor.getString(5)
                        , FileUtils.getByteSizeToComplex(cryptoData.getItem(cursor.getString(5)).raw())
                        , cryptoData.getItem(cursor.getString(5)).raw().trim()));

                intSeq++;
            }
        } catch (SQLException ex) {
            System.out.println("ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage());
            ex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (cStmt != null) cStmt.close();
            if (cursor != null) cursor.close();
            if (db.tibero != null) db.tibero.close();
        }

        return cryptoData;
    }

}
