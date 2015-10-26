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

//        FileUtils.setBatchLog(
//                FileUtils.strCCO_C
//                , FileUtils.strProgram_Id
//                , "20"
//        );

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
            File sndBackup = new File(DirectoryDefine.BackupDir + "/" + txtName);

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
    public static String strCCO_C = null;
    public static String strProgram_Id = null;
    public static String o_result_code = null;
    public static String o_result_args = null;
    public static String o_result_msg = null;

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


    /**
     * 배치 로그 생성 및 결과 return
     * @param strCcoC : 관계사코드
     * @param strProgram_Id : 프로그램_ID
     * @param strCl_st_c : 마감_상태_코드
     * @param strGetToday : 변경일자
     * @param v_oj_ct : 대상_건수
     * @param v_pc_ct : 처리_건수
     * @param v_err_ct : 오류건수
     * @param err_Msg : 오류메시지
     * @return
     * @throws SQLException
     */
    public static boolean setBatchLog(String strCcoC
                                      , String strProgram_Id
                                      , String strCl_st_c
                                      , String strGetToday
                                      , String v_oj_ct
                                      , String v_pc_ct
                                      , String v_err_ct
                                      , String err_Msg

    ) throws SQLException {

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
        cStmt.setString(5, "JAVA");
        cStmt.setString(6, CommonUtil.getDate(CommonConstants.DATE_DTTI));
        cStmt.setString(7, "D");

        cStmt.setString(8, strProgram_Id);                      /* 프로그램_ID */
        cStmt.setString(9, strCcoC);                            /* 관계사코드 */
        cStmt.setString(10, strGetToday.substring(0, 8));       /* 마감_일자 */
        cStmt.setString(11, strGetToday.substring(0, 8));       /* 마감_조건_시작일자 */
        cStmt.setString(12, strGetToday.substring(0, 8));       /* 마감_조건_종료일자 */
        cStmt.setString(13, strGetToday);                       /* 마감_시작_일자(배치시작시간) */
        cStmt.setString(14, CommonUtil.getToday());             /* 마감_종료_일자(배치종료시간) */
        cStmt.setString(15, strCl_st_c);                        /* 마감_상태_코드 */
        cStmt.setString(16, "00");                              /* 배치파일IF 처리_단계 */
        cStmt.setString(17, v_oj_ct);                           /* 대상_건수 */
        cStmt.setString(18, v_pc_ct);                           /* 처리_건수 */
        cStmt.setString(19, v_err_ct);                          /* 오류_건수 */
        cStmt.setString(20, err_Msg);                           /* 오류_내용*/

        try {

            cStmt.executeUpdate();

            o_result_code = cStmt.getString(1);
            o_result_args = cStmt.getString(2);
            o_result_msg = cStmt.getString(3);

            if (o_result_code.equals("00000")) {
                bSuccess = true;
            } else {
                System.out.println("Error[" + o_result_code + "] : " + o_result_msg);
            }

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

    public static void setConsoleLog(ConsoleLogType consoleLogType, String spName) {
        if (consoleLogType.equals(ConsoleLogType.BGN))
            System.out.println(String.format("[BGN][%s][서비스시작 => %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD), spName));

        if (consoleLogType.equals(ConsoleLogType.END))
            System.out.println(String.format("[BGN][%s][서비스종료 => %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD), spName));
    }

    public static void setConsoleLog(ConsoleLogType consoleLogType, String strCCO_c, String strRecord_dv) {
        if (consoleLogType.equals(ConsoleLogType.BGN))
            System.out.println(String.format("[BGN][%s][서비스시작 => %s] 관계사 : %s, 업무구분 : %s", CommonUtil.getDate(CommonConstants.DATE_STANDARD), strProgram_Id, strCCO_c, strRecord_dv));

        if (consoleLogType.equals(ConsoleLogType.END))
            System.out.println(String.format("[BGN][%s][서비스종료 => %s] 관계사 : %s, 업무구분 : %s", CommonUtil.getDate(CommonConstants.DATE_STANDARD), strProgram_Id, strCCO_c, strRecord_dv));
    }

    public static void getProgramId(String sourceFile) {

        strCCO_C = sourceFile.split("_")[1];                // 관계사코드
        String strRecord_DV = sourceFile.split("_")[2];     // 배치업무구분

        switch (strRecord_DV) {
            case "10" : // 코인 적립 배치 등록(IF_PT_006)
                strProgram_Id = "MEM_PT_B_009";
                break;
            case "20" : // 코인 사용 배치 등록(IF_PT_006)
                strProgram_Id = "MEM_PT_B_010";
                break;
            case "21" : // 고객 가용/소멸예정코인 조회(IF_PT_013)
                strProgram_Id = "MEM_PT_B_012";
                break;
            case "30" : // 코인 적립/사용 거래 대사(IF_PT_008)
                strProgram_Id = "MEM_PT_B_011";
                break;
            case "31" : // 코인 적립/사용 거래 대사(전체 TR : 하나은행, 외환은행, 하나대투)(IF_PT_108)
                strProgram_Id = "MEM_PT_B_011";
                break;
            case "40" : // 멤버십 거래내역(IF_PT_900)
                strProgram_Id = "MEM_PT_B_008";
                break;
            case "50" : // 관계사 일정산내역(IF_ADJ_001)
                strProgram_Id = "MEM_ADJ_S_005";
                break;
            case "51" : // 관계사 월정산내역(IF_ADJ_002)
                strProgram_Id = "MEM_ADJ_S_010";
                break;
            case "52" : // 코인사용 거래내역(IF_ADJ_005)
                strProgram_Id = "MEM_ADJ_S_009";
                break;
            case "53" : // 외환은행 정산내역 송신
                strProgram_Id = "MEM_ADJ_S_011";
                break;
            case "54" : // ERP 일정산 내역 송신
                strProgram_Id = "MEM_ADJ_S_012";
                break;
            case "55" : // ERP 월정산 내역 송신
                strProgram_Id = "MEM_ADJ_S_013";
                break;
            case "60" : // 제휴사가맹점마스터 일괄 등록(사용안함)
                strProgram_Id = "MEM_COP_B_001";
                break;
            case "70" : // 소멸 적립 내역(IF_ADJ_003)
                strProgram_Id = "MEM_ADJ_S_007";
                break;
            case "80" : // 거래사유별 거래 집계 대사(정산)(IF_ADJ_004)
                strProgram_Id = "MEM_ADJ_S_008";
                break;
            case "90" : // 온라인 로그인정보(IF_MOB_002)
                strProgram_Id = "MEM_MBR_S_036";
                break;
            case "91" : // 고객혜택정보 등록(IF_MBR_910)
                strProgram_Id = "MEM_MBR_S_037";
                break;
            case "92" : // 멤버십 신청서 가입 정보(IF_MBR_902)
                strProgram_Id = "MEM_MBR_S_038";
                break;
            default:
                strProgram_Id = "TEMP_TABLE_INSERT";
                break;
        }
    }

    public enum STEP {
        TempTableInsert(10),
        CallProcessSP(20),
        FileWrite(30),
        Complete(40);
        private int value;

        STEP(int value) {
            this.value = value;
        }
    }

    public enum STATUS {
        TempTableInsert(10),
        CallProcessSP(20),
        FileWrite(30),
        Complete(40);
        private int value;

        STATUS(int value) {
            this.value = value;
        }
    }

    public enum ConsoleLogType {
        BGN,
        END
    }



}

