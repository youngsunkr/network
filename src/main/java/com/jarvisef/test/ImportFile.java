package com.jarvisef.test;

/********************************************************
 * 하나금융그룹 통합 멤서십 프로젝트
 ********************************************************
 *
 * 1. 클래스 명 : ImportFile.java
 * 2. Description : 배치전문 임시테이블 Insert 클래스
 * 3. 관련테이블
 * 4. Author : 노영선
 * 5. 작성일 : 2015-07-10
 * 6. 수정일 :
 * <날짜>    : <수정내용>(<개발자명>)
 ********************************************************/

import org.apache.commons.lang3.time.StopWatch;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-07-10.
 */
public class ImportFile {

    public static List<String> cop_retFileList = new ArrayList<String>();
    public static List<String> retFileList = new ArrayList<String>();

    public static void main(String[] args) throws Exception {

        StopWatch stopWatch = new StopWatch();

        System.out.println("=====================================");
        System.out.println("[START] 배치파일 등록 업무 시작 [START]");
        System.out.println(String.format("[START TIME][%s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)));
        try {

            DoWork();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        /* 수행시간 출력 */
        stopWatch.toString();

        System.out.println(String.format("[END TIME][%s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)));
        System.out.println("[END] 배치파일 등록 업무 종료 [END]");
        System.out.println("=====================================");

        System.exit(0);
    }

    private static void DoWork() throws Exception {

        COP_RECURSIVE_FILE(DirectoryDefine.cop_base_Directory);

        for (String sourceFile : cop_retFileList) {
//            System.out.println(String.format("%s", sourceFile));

            if (IsInsertData(sourceFile)) {
                readFileAsListOfStrings(sourceFile, true);
            }
        }
        cop_retFileList.clear();

        RECURSIVE_FILE(DirectoryDefine.base_Directory);

        for (String sourceFile : retFileList) {
//            System.out.println(String.format("%s", sourceFile));

            if (IsInsertData(sourceFile)) {
                readFileAsListOfStrings(sourceFile, false);
            }
        }
        retFileList.clear();
    }

    public static void COP_RECURSIVE_FILE(String source){
        File dir = new File(source);
        File[] fileList = dir.listFiles();
        try {

            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                if (file.isFile()) {
                    if (file.getName().startsWith("COP") && (file.getName().toUpperCase().endsWith(".END"))) {

                        int ext = file.getPath().lastIndexOf(".");
                        String strFilePath = file.getPath().substring(0, ext);
                        File isFile = new File(strFilePath);
                        if (isFile.exists()) {
                            // 파일이 있다면 파일 이름 출력
                            System.out.println("\t 파일 이름 = " + file.getName());
                            //retFileList.add(file.getName());
                            cop_retFileList.add(strFilePath.replace('\\', '/'));
                        }
                    }
                } else if (file.isDirectory()) {
                    if (file.getName().startsWith("COP") || file.getName().toUpperCase().equals("RCV")) {
                        System.out.println("디렉토리 이름 = " + file.getName());
                        // 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색
                        COP_RECURSIVE_FILE(file.getCanonicalPath().toString());
                    }
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void RECURSIVE_FILE(String source){
        File dir = new File(source);
        File[] fileList = dir.listFiles();
        try {

            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                if (file.isFile()) {
                    if (file.getName().startsWith("HFG") && (file.getName().toUpperCase().endsWith(".END"))) {

                        int ext = file.getPath().lastIndexOf(".");
                        String strFilePath = file.getPath().substring(0, ext);
                        File isFile = new File(strFilePath);
                        if (isFile.exists()) {
                            // 파일이 있다면 파일 이름 출력
                            System.out.println("\t 파일 이름 = " + file.getName());
                            //retFileList.add(file.getName());
                            retFileList.add(strFilePath.replace('\\', '/'));
                        }
                    }
                } else if (file.isDirectory()) {
                    if (file.getName().startsWith("HFG") || file.getName().toUpperCase().equals("RCV")) {
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
    public static void readFileAsListOfStrings(String sourceFile, boolean IsCCO) throws Exception {

        String strFileName = sourceFile.substring(sourceFile.lastIndexOf("/") + 1 ,sourceFile.length());
        System.out.println("\t 파일명 : " + strFileName);
        FileUtils.getProgramId(strFileName);
        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

        DBHandler db = new DBHandler();
        db.tibero.setAutoCommit(false);

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile),CommonConstants.CHARSET_EUC_KR));

        /* 업무변수 선언 */
        int intSeq                      = 1;
        boolean bSuccess                = false;
        String strCCO_C                 = strFileName.split("_")[0];
        String strRecord_DV             = strFileName.split("_")[1];
        String strDate                  = strFileName.split("_")[2];
        StringBuffer sbInsertSql        = null;
        boolean o_result_status         = false;
        String readLine                 = null;
        String strMaskingData           = null;
        List<String> orgRowData = new ArrayList<String>();

        // 파일명을 기준으로 배치 업무 구분을 가지고 온다.
        // 배치파일명 구조 (관계사코드(7)_배치업무구분(2)_년월일(8)000000_송수신구분(2).sam)
        int file            = sourceFile.lastIndexOf("/");
        int ext             = sourceFile.lastIndexOf(".");
        int fileLength      = sourceFile.length();
        String filename     = sourceFile.substring(file+1,ext);
        String extname      = sourceFile.substring(ext+1,fileLength);
        String fullfilename = filename+"."+extname;
        String strFilenNmeWithoutExtn  = strFileName.substring(0, strFileName.lastIndexOf('.'));

        System.out.println(strFileName);
        System.out.println(strFilenNmeWithoutExtn);

        PreparedStatement pStmtIns = null;

        try {
            while ((readLine = fileReader.readLine()) != null) {

                /*
                * 파일전문 임시테이블 Insert SP 호출
                * */

                if (readLine.substring(0, 1).toUpperCase().contains("H")) {

//                    System.out.println(readLine);

                    sbInsertSql = new StringBuffer();
                    sbInsertSql.append("");

                    sbInsertSql.append("INSERT INTO MEM_FILE_BAT_RG                 ");
                    sbInsertSql.append("(                                           ");
                    sbInsertSql.append("FILE_NM, RECORD_DV, SEQ, DTA, BAT_PC_ST_C   ");
                    sbInsertSql.append(", REG_ID, RG_DTTI, CHP_ID, CH_DTTI          ");
                    sbInsertSql.append(") VALUES (                                  ");
                    sbInsertSql.append("?,?,?,?,?, ?,?,?,?                          ");
                    sbInsertSql.append(")                                           ");

                    pStmtIns = db.tibero.prepareStatement(sbInsertSql.toString());
                    pStmtIns.setString(1, strFileName);
                    pStmtIns.setString(2, "H");
                    pStmtIns.setInt(3, 0);
                    pStmtIns.setString(4, readLine);
                    pStmtIns.setString(5, "U");

                    pStmtIns.setString(6, "JAVA");
                    pStmtIns.setString(7, CommonUtil.getToday());
                    pStmtIns.setString(8, "JAVA");
                    pStmtIns.setString(9, CommonUtil.getToday());

                    pStmtIns.executeQuery();

                    strMaskingData = readLine;

                } else if (readLine.substring(0, 1).toUpperCase().contains("D")) {

//                    System.out.println(readLine);

                    sbInsertSql = new StringBuffer();
                    sbInsertSql.append("");

                    sbInsertSql.append("INSERT INTO MEM_FILE_BAT_RG                 ");
                    sbInsertSql.append("(                                           ");
                    sbInsertSql.append("FILE_NM, RECORD_DV, SEQ, DTA, BAT_PC_ST_C   ");
                    sbInsertSql.append(", REG_ID, RG_DTTI, CHP_ID, CH_DTTI          ");
                    sbInsertSql.append(") VALUES (                                  ");
                    sbInsertSql.append("?,?,?,?,?, ?,?,?,?                          ");
                    sbInsertSql.append(")                                           ");

                    pStmtIns = db.tibero.prepareStatement(sbInsertSql.toString());
                    pStmtIns.setString(1, strFileName);
                    pStmtIns.setString(2, "D");
                    pStmtIns.setInt(3, intSeq);
                    pStmtIns.setString(4, readLine);
                    pStmtIns.setString(5, "U");

                    pStmtIns.setString(6, "JAVA");
                    pStmtIns.setString(7, CommonUtil.getToday());
                    pStmtIns.setString(8, "JAVA");
                    pStmtIns.setString(9, CommonUtil.getToday());

                    pStmtIns.executeQuery();

                    intSeq++;

                    byte[] readByte = null;
                    int iLength = 0;
                    if (strRecord_DV.equals("10")) {

                        System.out.println(String.format("[IF_PT_006(10) 전문 데이터 길이 체크][%s]", readLine.length()));
                        readByte = readLine.getBytes(CommonConstants.CHARSET_EUC_KR);
                        iLength = readByte.length;

                        if (iLength == 800) {       // 전문 길이 체크후 masking 작업 시작

                            strMaskingData = String.format("%s%s%s%s%s",
                                    StringHelper.subString(readLine, 0, 100)                // 카드번호
                                    , CommonUtil.getBlank(20, "*")                          //
                                    , StringHelper.subString(readLine, 120, 430 - 120)      // 비밀번호
                                    , CommonUtil.getBlank(4, "*")                           //
                                    , StringHelper.subString(readLine, 434, 800 - 434)
                            );
                            System.out.println(strMaskingData);

                        } else {
                            strMaskingData = readLine;
                        }

                    } else if (strRecord_DV.equals("20")) {

                        System.out.println(String.format("[IF_PT_007(20) 전문 데이터 길이 체크][%s]", readLine.length()));
                        readByte = readLine.getBytes(CommonConstants.CHARSET_EUC_KR);
                        iLength = readByte.length;

                        if (iLength == 800) {       // 전문 길이 체크후 masking 작업 시작

                            strMaskingData = String.format("%s%s%s%s%s",
                                    StringHelper.subString(readLine, 0, 90)                 // 카드번호
                                    , CommonUtil.getBlank(20, "*")                          //
                                    , StringHelper.subString(readLine, 110, 334 - 110)      // 비밀번호
                                    , CommonUtil.getBlank(4, "*")                           //
                                    , StringHelper.subString(readLine, 338, 800 - 338)
                            );
                            System.out.println(strMaskingData);

                        } else {
                            strMaskingData = readLine;
                        }

                    } else if (strRecord_DV.equals("21")) {

                        System.out.println(String.format("[IF_PT_013(21) 전문 데이터 길이 체크][%s]", readLine.length()));
                        readByte = readLine.getBytes(CommonConstants.CHARSET_EUC_KR);
                        iLength = readByte.length;

                        if (iLength == 500) {       // 전문 길이 체크후 masking 작업 시작

                            strMaskingData = String.format("%s%s%s%s%s",
                                    StringHelper.subString(readLine, 0, 90)                 // 카드번호
                                    , CommonUtil.getBlank(20, "*")                          //
                                    , StringHelper.subString(readLine, 110, 334 - 110)      // 비밀번호
                                    , CommonUtil.getBlank(4, "*")                           //
                                    , StringHelper.subString(readLine, 338, 800 - 338)
                            );
                            System.out.println(strMaskingData);

                        } else {
                            strMaskingData = readLine;
                        }

                    }

                } else if (readLine.substring(0, 1).toUpperCase().contains("T")) {

//                    System.out.println(readLine);

                    sbInsertSql = new StringBuffer();
                    sbInsertSql.append("");

                    sbInsertSql.append("INSERT INTO MEM_FILE_BAT_RG                 ");
                    sbInsertSql.append("(                                           ");
                    sbInsertSql.append("FILE_NM, RECORD_DV, SEQ, DTA, BAT_PC_ST_C   ");
                    sbInsertSql.append(", REG_ID, RG_DTTI, CHP_ID, CH_DTTI          ");
                    sbInsertSql.append(") VALUES (                                  ");
                    sbInsertSql.append("?,?,?,?,?, ?,?,?,?                          ");
                    sbInsertSql.append(")                                           ");

                    pStmtIns = db.tibero.prepareStatement(sbInsertSql.toString());
                    pStmtIns.setString(1, strFileName);
                    pStmtIns.setString(2, "T");
                    pStmtIns.setString(3, "999999999");
                    pStmtIns.setString(4, readLine);
                    pStmtIns.setString(5, "U");

                    pStmtIns.setString(6, "JAVA");
                    pStmtIns.setString(7, CommonUtil.getToday());
                    pStmtIns.setString(8, "JAVA");
                    pStmtIns.setString(9, CommonUtil.getToday());

                    pStmtIns.executeQuery();

                    strMaskingData = readLine;

                }

                orgRowData.add(strMaskingData);

            }

            if (pStmtIns != null) pStmtIns.close();
            if (fileReader != null) fileReader.close();

            // 임시테이블 INSERT 성공시 Commit
            db.tibero.commit();
            //db.tibero.setTransactionIsolation(db.tibero.TRANSACTION_READ_COMMITTED);

            CreateBackupFile(orgRowData, sourceFile, IsCCO);

            if (!orgRowData.isEmpty()) orgRowData.clear();

        } catch (IOException ex) {
            try {

                if (db.tibero != null) db.tibero.rollback();
                if (fileReader != null) fileReader.close();
                if (pStmtIns != null) pStmtIns.close();
                if (db.tibero != null) db.tibero.close();

                System.out.println(String.format("[END][%s][서비스강제종료(IOException) => 임시테이블 등록 실패 파일명 : %s"
                        , CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , strFileName
                ));

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }
        } catch (SQLException ex) {

            try {

                if (db.tibero != null) db.tibero.rollback();
                if (fileReader != null) fileReader.close();
                if (pStmtIns != null) pStmtIns.close();
                if (db.tibero != null) db.tibero.close();

                System.out.println(String.format("[END][%s][서비스강제종료(IOException) => 임시테이블 등록 실패 파일명 : %s"
                        , CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , strFileName
                ));

                System.out.println("ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage());
                ex.printStackTrace();

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }

        } catch (Exception ex) {

            try {

                if (db.tibero != null) db.tibero.rollback();
                if (fileReader != null) fileReader.close();
                if (pStmtIns != null) pStmtIns.close();
                if (db.tibero != null) db.tibero.close();

                System.out.println(String.format("[END][%s][서비스강제종료(IOException) => 임시테이블 등록 실패 파일명 : %s"
                        , CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , strFileName
                ));

                ex.printStackTrace();

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }

        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (pStmtIns != null) pStmtIns.close();
            if (db.tibero != null) db.tibero.close();
        }

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END);
    }

    private static void CreateBackupFile(List<String> orgRowData, String sourceFile, boolean isCCO) throws IOException {

        String strFileName = sourceFile.substring(sourceFile.lastIndexOf("/") + 1, sourceFile.length());
        String strCCO_C = strFileName.split("_")[0];
        BufferedWriter out = null;

        try {

            // 직제휴사의 Case
            if (isCCO) {

                File mkFolder = new File(
                        DirectoryDefine.COP_DirPath(
                                strCCO_C
                                , File.separator + DirectoryDefine.RCV + File.separator + DirectoryDefine.BackupDir + File.separator + CommonUtil.getTodayDt()
                        )
                );

                if (!mkFolder.exists()) {
                    mkFolder.mkdirs();
                }

                File fws = new File(
                        DirectoryDefine.COP_DirPath(
                                strCCO_C
                                , File.separator + DirectoryDefine.RCV + File.separator + DirectoryDefine.BackupDir + File.separator + CommonUtil.getTodayDt() + File.separator + strFileName
                        )
                );
                if (!fws.exists()) {
                    for (String line : orgRowData) {
                        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fws, true), CommonConstants.CHARSET_EUC_KR));
                        out.write(line);
                        out.newLine();
                        out.close();
                    }

                    System.out.println(String.format("백업파일 경로 %s", fws.getPath()));
                }

                // 임시테이블 성공시 수신된 최종 파일 제거
                File readFileName = new File(sourceFile);
                if (readFileName.exists()) {
                    readFileName.delete();

                    File readENDFileName = new File(sourceFile + ".END");
                    readENDFileName.delete();
                }

            } else {

                File mkFolder = new File(
                        DirectoryDefine.CCO_DirPath(
                                strCCO_C
                                , File.separator + DirectoryDefine.RCV + File.separator + DirectoryDefine.BackupDir + File.separator + CommonUtil.getTodayDt()
                        )
                );
                if (!mkFolder.exists()) {
                    mkFolder.mkdirs();
                }

                File fws = new File(
                        DirectoryDefine.CCO_DirPath(
                                strCCO_C
                                , File.separator + DirectoryDefine.RCV + File.separator + DirectoryDefine.BackupDir + File.separator + CommonUtil.getTodayDt() + File.separator + strFileName
                        )
                );
                if (!fws.exists()) {
                    for (String line : orgRowData) {
                        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fws, true), CommonConstants.CHARSET_EUC_KR));
                        out.write(line);
                        out.newLine();
                        out.close();
                    }

                    System.out.println(String.format("백업파일 경로 %s", fws.getPath()));
                }

                // 임시테이블 성공시 수신된 최종 파일 제거
                File readFileName = new File(sourceFile);
                if (readFileName.exists()) {
                    readFileName.delete();

                    File readENDFileName = new File(sourceFile + ".END");
                    readENDFileName.delete();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) out.close();
        }
    }

    private static boolean IsInsertData(String sourceFile) {

        boolean bSuccess = true;

        String strFileName = sourceFile.substring(sourceFile.lastIndexOf("\\") + 1 ,sourceFile.length());

        int file            = sourceFile.lastIndexOf("/");
        int ext             = sourceFile.lastIndexOf(".");
        int fileLength      = sourceFile.length();
        String filename     = sourceFile.substring(file+1,ext);
        String extname      = sourceFile.substring(ext+1,fileLength);
        strFileName = filename+"."+extname;

        DBHandler db = new DBHandler();
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {

            StringBuffer sbSelectSQL = new StringBuffer();
            sbSelectSQL.append("SELECT COUNT(FILE_NM), FILE_NM  ");
            sbSelectSQL.append("FROM MEM_FILE_BAT_RG            ");
            sbSelectSQL.append("WHERE FILE_NM = ?               ");
            sbSelectSQL.append("GROUP BY FILE_NM                ");

            pStmt = db.tibero.prepareStatement(sbSelectSQL.toString());
            pStmt.setString(1, strFileName);

            rs = pStmt.executeQuery();
            while (rs.next()) {
                int cnt = rs.getInt(1);
                if (cnt == 0) {
                    bSuccess = true;
                } else {
                    String existsFileName = rs.getString(2);
                    System.out.println(String.format("[파일 전문 임심테이블 등록 오류] 기 등록된 파일명 : [%s]", existsFileName));
                    bSuccess = false;
                }
            }

        } catch (SQLException ex) {

            System.out.println("ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage());
            ex.printStackTrace();

        } finally {

            try {
                if (rs != null) rs.close();
                if (pStmt != null) pStmt.close();
                if (db.tibero != null) db.tibero.close();
            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }
        }

        return bSuccess;
    }

}
