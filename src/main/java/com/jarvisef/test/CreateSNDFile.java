package com.jarvisef.test;

/********************************************************
 * 하나금융그룹 통합 멤서십 프로젝트
 ********************************************************
 *
 * 1. 클래스 명 : CreateSNDFile.java
 * 2. Description : 배치전문 파일생성 클래스
 * 3. 관련테이블
 * 4. Author : 노영선
 * 5. 작성일 : 2015-07-10
 * 6. 수정일 :
 * <날짜>    : <수정내용>(<개발자명>)
 ********************************************************/

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-07-10.
 */
public class CreateSNDFile {

    public static List<String> retFileList = new ArrayList<String>();
    public static boolean isLocking = false;

    public static void main(String[] args) throws Exception {

        StopWatch stopWatch = new StopWatch();

        System.out.println("=====================================");
        System.out.println("[START] 배치파일 생성 업무 시작 [START]");
        System.out.println(String.format("[START TIME][%s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)));

        try {
            if (args != null) {
                for (int i=0; i<args.length; i++) {
                    System.out.println("args[" + i + "]=" + args[i]);
                }
            }

            if (args.length < 2) {
                System.out.println("입력 파라메터 오류");
            }

            // 재작업 처리를 위한 날짜 args 유무 체크후 실행
            String strDate = CommonUtil.getTodayDt();
            if (args.length > 2) {
                DoWork(args[0], args[1], StringHelper.SetReplace(args[2]));
            } else {
                DoWork(args[0], args[1], strDate);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }

        /* 수행시간 출력 */
        stopWatch.toString();

        System.out.println(String.format("[END TIME][%s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)));
        System.out.println("[END] 배치파일 생성 업무 종료 [END]");
        System.out.println("=====================================");

        System.exit(0);
    }

    private static void DoWork(String strCCO_C, String strRecord_DV, String strDate) throws SQLException, IOException {

        String sourceFile = String.format("%s_%s_%s", strCCO_C, strRecord_DV, strDate);

        switch (strRecord_DV) {
            case "10" : // 코인 적립 배치 업무 실행
            case "20" : // 코인 사용 배치 업무 실행
            case "21" : // 고객 가용/소멸예정코인 조회
                if (IsProcessing(strCCO_C, strRecord_DV)) {
                    for (String strFileName : retFileList) {
                        if (!isLocking) {
                            readTableToFile(strFileName);
                        }
                    }
                }
            case "30" : // 코인 적립/사용 거래 대사
            case "31" : // 코인 적립/사용 거래 대사(전체 TR)
            case "40" : // 멤버십 거래내역
            case "50" : // 관계사 일정산내역
            case "51" : // 관계사 월정산내역
            case "52" : // 코인사용 거래내역
            case "53" : // 외환은행 정산내역 송신
            case "54" : // ERP 일정산 내역 송신
            case "55" : // ERP 월정산 내역 송신
            case "70" : // 소멸 적립 내역
            case "80" : // 거래사유별 거래 집계 대사(정산)
            case "90" : // 온라인 로그인정보
            case "92" : // 멤버십 신청서 가입 정보
                if (!isLocking) {
                    sndFile(sourceFile);
                }
        }
    }


    private static void readTableToFile(String sourceFile) throws SQLException, IOException {

        isLocking = true;

//        FileUtils.getProgramId(sourceFile);

        String strCCO_C = sourceFile.split("_")[0];
        String strRecord_DV = sourceFile.split("_")[1];
        String strDate = sourceFile.split("_")[2];
        String strSndFlag = "20";
        String strFileName = String.format("%s_%s_%s_%s.sam", strCCO_C, strRecord_DV, strDate, strSndFlag);

        /* 임시테이블 파일생성 프로그램 ID */
        switch (strRecord_DV) {
            case "10" : // 코인 적립 배치 등록(IF_PT_006) 파일 생성
                FileUtils.strProgram_Id = "MEM_PT_B_009_FILE";
                break;
            case "20" : // 코인 사용 배치 등록(IF_PT_007) 파일 생성
                FileUtils.strProgram_Id = "MEM_PT_B_010_FILE";
                break;
            case "21" : // 고객 가용/소멸예정코인 조회(IF_PT_013) 파일 생성
                FileUtils.strProgram_Id = "MEM_PT_B_012_FILE";
                break;
        }

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

        int intCnt = 0;
        String strGetToday = CommonUtil.getToday();
        DBHandler db = new DBHandler();
        PreparedStatement pStmtCheck = null;
        PreparedStatement pStmtData = null;
        PreparedStatement pStmtIns = null;
        ResultSet CheckRs = null;
        ResultSet DataRs = null;

        /* 파일관련 인스턴스 */
        BufferedWriter out = null;

        try {

            StringBuffer sbCheckSQL = new StringBuffer();
            sbCheckSQL.append("SELECT PGM_ID            ");
            sbCheckSQL.append("FROM MEM_CL_INF_LST AS A ");
            sbCheckSQL.append("WHERE CCO_C = ?          ");
            sbCheckSQL.append("  AND PGM_ID = ?         ");
            sbCheckSQL.append("  AND CL_DT = ?          ");
            sbCheckSQL.append("  AND BAT_FILE_NM = ?    ");

            pStmtCheck = db.tibero.prepareStatement(sbCheckSQL.toString());
            pStmtCheck.setString(1, strCCO_C);
            pStmtCheck.setString(2, FileUtils.strProgram_Id);
            pStmtCheck.setString(3, strDate.substring(0, 8));
            pStmtCheck.setString(4, sourceFile);
            CheckRs = pStmtCheck.executeQuery();

            String tmpFileName = null;
            boolean bSuccess = false;
            while (CheckRs.next()) {
                tmpFileName = StringHelper.SetReplace(CheckRs.getString(1));
                bSuccess = true;
                System.out.println(String.format("등록된 파일명 : [%s]", tmpFileName));
            }

            System.out.println("==============================");
            System.out.println("=============== 마감테이블 등록 파일 정보 유무 " + bSuccess + " ===============");
            System.out.println("==============================");

            if (!bSuccess) {
                StringBuffer sbInsertSQL = new StringBuffer();
                sbInsertSQL.append("INSERT INTO MEM_CL_INF_LST( ");
                sbInsertSQL.append("CCO_C                       ");
                sbInsertSQL.append(", PGM_ID                    ");
                sbInsertSQL.append(", CL_DT                     ");
                sbInsertSQL.append(", CL_CND_SDT                ");
                sbInsertSQL.append(", CL_CND_EDT                ");
                sbInsertSQL.append(", CL_ST_C                   ");
                sbInsertSQL.append(", BAT_FILE_NM               ");
                sbInsertSQL.append(", LV                        ");
                sbInsertSQL.append(", RMK                       ");
                sbInsertSQL.append(", REG_ID                    ");
                sbInsertSQL.append(", RG_DTTI                   ");
                sbInsertSQL.append(", CHP_ID                    ");
                sbInsertSQL.append(", CH_DTTI                   ");
                sbInsertSQL.append(") VALUES (                  ");
                sbInsertSQL.append("?                           ");
                sbInsertSQL.append(", ?                         ");
                sbInsertSQL.append(", ?                         ");
                sbInsertSQL.append(", null                      ");
                sbInsertSQL.append(", null                      ");
                sbInsertSQL.append(", 20                        ");
                sbInsertSQL.append(", ?                         ");
                sbInsertSQL.append(", 0                         ");
                sbInsertSQL.append(", null                      ");
                sbInsertSQL.append(", ?                         ");
                sbInsertSQL.append(", ?                         ");
                sbInsertSQL.append(", ?                         ");
                sbInsertSQL.append(", ?                         ");
                sbInsertSQL.append(")                           ");

                System.out.println("마감일자 : " + strDate.substring(0, 8));

                pStmtIns = db.tibero.prepareStatement(sbInsertSQL.toString());

                pStmtIns.setString(1, strCCO_C);
                pStmtIns.setString(2, FileUtils.strProgram_Id);
                pStmtIns.setString(3, strDate.substring(0, 8));
                pStmtIns.setString(4, sourceFile);

                pStmtIns.setString(5, "JAVA");
                pStmtIns.setString(6, CommonUtil.getToday());
                pStmtIns.setString(7, "JAVA");
                pStmtIns.setString(8, CommonUtil.getToday());

                pStmtIns.executeQuery();
                System.out.println("마감테이블 등록 완료");

                StringBuffer sbDataSQL = new StringBuffer();
                sbDataSQL.append("SELECT DTA");
                sbDataSQL.append("FROM MEM_FILE_BAT_RG      ");
                sbDataSQL.append("WHERE FILE_NMF = ?        ");
                sbDataSQL.append("  AND BAT_PC_ST_C - 'P'   ");
                sbDataSQL.append("ORDER BY SEQ              ");


                /********************
                 * 업무 SP 실행
                 ********************/
                File mkFolder = new File(DirectoryDefine.CCO_DirPath(strCCO_C, File.separator + DirectoryDefine.SND + File.separator + DirectoryDefine.BackupDir));
                if (!mkFolder.exists()) {
                    mkFolder.mkdirs();
                }

                File fws = new File(DirectoryDefine.CCO_DirPath(strCCO_C, File.separator + DirectoryDefine.SND + File.separator + strFileName));
                if (!fws.exists()) {

                    //System.out.println(sbDataSQL.toString());

                    pStmtData = db.tibero.prepareStatement(sbDataSQL.toString());
                    pStmtData.setString(1, sourceFile);
                    DataRs = pStmtData.executeQuery();

                    while (DataRs.next()) {
                        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fws, true), CommonConstants.CHARSET_EUC_KR));

                        ResultSet Rs = DataRs;
                        String data = Rs.getString(1);

                        out.write(data);
                        out.newLine();

                        intCnt++;

                        out.close();
                    }

                    if (fws.exists()) {
                        // 파일명 변경
                        File sndBackup = new File(DirectoryDefine.CCO_DirPath(strCCO_C, File.separator + DirectoryDefine.SND + File.separator + DirectoryDefine.BackupDir + File.separator + strFileName));
                        if (!sndBackup.exists()) {
                            FileUtils.copyFile(new File(DirectoryDefine.CCO_DirPath(FileUtils.strCCO_C, File.separator + DirectoryDefine.SND + File.separator + strFileName))
                            , sndBackup);
                        }

                        // rename
                        //fws.renameTo(new File(DirectoryDefine.CCO_DirPath(FileUtils.strCCO_C, File.separator + DirectoryDefine.SND + File.separator + strFileName)));

                        // END 파일 생성
                        FileOutputStream completeFile = new FileOutputStream(DirectoryDefine.CCO_DirPath(FileUtils.strCCO_C, File.separator + DirectoryDefine.SND + File.separator + strFileName + ".END"));
                        completeFile.close();

                        // SND 파일 생성 완료시 데이터 UPDATE (배치 상태 P -> D)
                        DownloadComplete(sourceFile);

                        System.out.println(String.format("생성파일명 : [%s]", strFileName));
                    }
                }

                if (intCnt == 2) {
                    intCnt = 0;             // 데이터부에 0건만 있을경우 대상건수, 처리건수 0건으로 고정시킴
                } else if (intCnt == 3) {
                    intCnt = 1;             // 데이터부에 1건만 있을경우 대상건수, 처리건수 1건으로 고정시킴
                } else if (intCnt > 3) {
                    intCnt = intCnt - 2;    // 헤더(H), 테일(T) 레코드 제거후 카운트 집계
                }

                // 배치로그 생성
                FileUtils.setBatchLog(
                        FileUtils.strCCO_C
                        , FileUtils.strProgram_Id
                        , "30"
                        , strDate.substring(0, 8)
                        , String.valueOf(intCnt)
                        , String.valueOf(intCnt)
                        , "0"
                        , "JAVA Program Success"
                );

            } else {

                System.out.println("=========== 테이블명 : MEM_CL_INF_LST ===========");
                System.out.println("=========== 관계사코드 : " + strCCO_C + " ===========");
                System.out.println("=========== PGM_ID : " + FileUtils.strProgram_Id  + " ===========");
                System.out.println("=========== CL_DT : " + strDate.substring(0, 8) + " ===========");
                System.out.println("=========== BAT_FILE_NM : " + sourceFile+ " ===========");

            }

        } catch(IOException ex) {
            try {
                if (out != null) out.close();
                if (db.tibero != null) db.tibero.close();

                // 배치실패로그 생성
                FileUtils.setBatchLog(
                        FileUtils.strCCO_C
                        , FileUtils.strProgram_Id
                        , "40"
                        , strDate.substring(0, 8)
                        , String.valueOf(intCnt)
                        , String.valueOf(intCnt)
                        , "1"
                        , ex.toString()
                );

                System.out.println(String.format("[END][%s][서비스강제종료 => (%s) : %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , FileUtils.o_result_code
                        , FileUtils.o_result_msg));

                ex.printStackTrace();

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }

            System.exit(1);

        } catch(SQLException ex) {
            try {
                if (out != null) out.close();
                if (db.tibero != null) db.tibero.close();

                // 배치실패로그 생성
                FileUtils.setBatchLog(
                        FileUtils.strCCO_C
                        , FileUtils.strProgram_Id
                        , "40"
                        , strDate.substring(0, 8)
                        , String.valueOf(intCnt)
                        , String.valueOf(intCnt)
                        , "1"
                        , "ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage()
                );

                System.out.println(String.format("[END][%s][서비스강제종료 => (%s) : %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , FileUtils.o_result_code
                        , FileUtils.o_result_msg));

                ex.printStackTrace();

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }

            System.exit(1);

        } finally {
            try {
                if (out != null) out.close();
                if (DataRs != null) DataRs.close();
                if (pStmtData != null) pStmtData.close();
                if (db.tibero != null) db.tibero.close();
            } catch(Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }
        }

        isLocking = false;

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END);
    }

    private static void sndFile(String sourceFile) throws SQLException, IOException {
        isLocking = true;

        FileUtils.getProgramId(sourceFile);

        String strCCO_C = sourceFile.split("_")[0];         // 관계사 코드
        String strRecord_DV = sourceFile.split("_")[1];     // 배치업무구분
        String strDate = sourceFile.split("_")[2];          // 년월일시
        String strSndFlag = "20";                           // 송수신구분
        String strFileName = String.format("%s_%s_%s000000_%s.sam", strCCO_C, strRecord_DV, strDate, strSndFlag);

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

        DBHandler db = new DBHandler();
        CallableStatement cStmt = null;
        ResultSet cursor = null;
        int intCnt = 0;
        int fileIndex = 0;
        int cursorIndex = 0;
        int headerFiller = 0;
        int tailFiller = 0;
        String strGetToday = CommonUtil.getToday();

        /* 파일관련 인스턴스 */
        BufferedWriter out = null;

        try {

            String strProcessSP = null;

            if (CommonUtil.isNotNull(strFileName)) {
                switch (strRecord_DV) {
                    case "30" : // 코인 적립/사용 거래 대사
                    case "31" : // 코인 적립/사용 거래 대사(전체TR : 하나은행, 외환은행, 하나대투)
                        strProcessSP = "{call MEM_PT_B_011(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "40" : // 멤버십 거래내역
                        strProcessSP = "{call MEM_PT_B_008(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 834;
                        tailFiller = 876;
                        break;
                    case "50" : // 관계사 일 정산내역 I/F 등록
                        strProcessSP = "{call MEM_ADJ_S_005(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "51" : // 관계사 일 정산내역 I/F 등록
                        strProcessSP = "{call MEM_ADJ_S_010(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "52" : // 코인 사용 거래 내역
                        strProcessSP = "{call MEM_ADJ_S_009(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "53" : // 외환은행 정산내역 송신
                        strProcessSP = "{call MEM_ADJ_S_011(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "54" : // ERP 일정산 내역 송신
                        strProcessSP = "{call MEM_ADJ_S_012(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "55" : // ERP 월정산 내역 송신
                        strProcessSP = "{call MEM_ADJ_S_013(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";


                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "70" : // 소멸 적립 내역 I/F 등록
                        strProcessSP = "{call MEM_ADJ_S_007(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "80" : // 거래사유별 거래 집계 대사(정산) I/F 등록
                        strProcessSP = "{call MEM_ADJ_S_008(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "90" : // 온라인 로그인 정보(IF_MOB_002)
                        strProcessSP = "{call MEM_MBR_S_036(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                    case "92" : // 멤버십 가입신청서(IF_MBR_902)
                        strProcessSP = "{call MEM_MBR_S_038(?,?,?,?,?,?, ?,?,?,?,?,?,?)}";

                        fileIndex = 11;
                        cursorIndex = 13;
                        headerFiller = 534;
                        tailFiller = 576;
                        break;
                }

                if (cStmt != null) {
                    cStmt.registerOutParameter(1, Types.VARCHAR);
                    cStmt.registerOutParameter(2, Types.VARCHAR);
                    cStmt.registerOutParameter(3, Types.VARCHAR);
                    cStmt.setString(4, CommonUtil.getTodayDt());
                    cStmt.setString(5, "JAVA");
                    cStmt.setString(6, strGetToday);

                    cStmt.setString(7, FileUtils.strProgram_Id);
                    cStmt.setString(8, strRecord_DV);
                    cStmt.setString(9, strCCO_C);
                    cStmt.setString(10, strDate);
                    cStmt.registerOutParameter(11, Types.VARCHAR);
                    cStmt.registerOutParameter(12, Types.VARCHAR);
//                    cStmt.registerOutParameter(13, JDBCType.REF_CURSOR);        // JdbcType.TB_CURSOR.TYPE_CODE(-17)
                    cStmt.registerOutParameter(13, JdbcType.TB_CURSOR.TYPE_CODE);
                }

                /* 업무 SP 실행 */
                try {

                    if (cStmt != null) cStmt.executeUpdate();
                    String o_file_nm =  StringHelper.SetReplace(cStmt.getString(fileIndex));
                    cursor = (ResultSet)cStmt.getObject(cursorIndex);

                    File mkFolder = new File(DirectoryDefine.CCO_DirPath(strCCO_C, File.separator + DirectoryDefine.SND + File.separator + DirectoryDefine.BackupDir));
                    if (!mkFolder.exists()) {
                        mkFolder.mkdirs();
                    }

                    File fws = new File(DirectoryDefine.CCO_DirPath(strCCO_C, File.separator + DirectoryDefine.SND + File.separator + o_file_nm));
                    if (!fws.exists()) {

                        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fws), CommonConstants.CHARSET_EUC_KR));

                        String strHead = String.format(
                                "H000000000%s%s%s%s"
                                , strCCO_C
                                , CommonUtil.getRPadd(o_file_nm, 35)
                                , CommonUtil.getRPadd(CommonUtil.getDate(CommonConstants.DATE_DTTI), 14)
                                , CommonUtil.getBlank(headerFiller)
                        );
                        out.write(strHead);

                        while (cursor.next()) {

                            String data = cursor.getString(1);
                            out.newLine();
                            out.write(data);

                            intCnt++;
                        }
                        if (cursor != null) cursor.close();

                        String strTail = String.format(
                                "T999999999%s%s%s"
                                , strCCO_C
                                , CommonUtil.getLPadd(String.valueOf(intCnt), 7, "0")
                                , CommonUtil.getBlank(tailFiller)
                        );
                        out.newLine();
                        out.write(strTail);
                        out.close();

                        if (fws.exists()) {
                            // 파일명 변경
                            File sndBackup = new File(DirectoryDefine.CCO_DirPath(strCCO_C, File.separator + DirectoryDefine.SND + File.separator + DirectoryDefine.BackupDir + File.separator + o_file_nm));
                            if (!sndBackup.exists()) {
                                FileUtils.copyFile(new File(DirectoryDefine.CCO_DirPath(FileUtils.strCCO_C, File.separator + DirectoryDefine.SND + File.separator + o_file_nm))
                                        , sndBackup);
                            }

                            // rename
                            //fws.renameTo(new File(DirectoryDefine.CCO_DirPath(FileUtils.strCCO_C, File.separator + DirectoryDefine.SND + File.separator + o_file_nm)));

                            // END 파일 생성
                            FileOutputStream completeFile = new FileOutputStream(DirectoryDefine.CCO_DirPath(FileUtils.strCCO_C, File.separator + DirectoryDefine.SND + File.separator + o_file_nm + ".END"));
                            completeFile.close();

                            System.out.println(String.format("SND 파일생성완료 : [%s]", fws.getPath()));
                        }
                    }

                    if (intCnt == 2) {
                        intCnt = 1;                 // 데이터부에 1건만 있을경우 대상건수, 처리건수 1건으로 고정시킴
                    } else if (intCnt == 1) {
                        intCnt = 0;                 // 데이터부에 1거만 있을경우 대상건수, 처리건수 0건으로 고정시킴
                    }

                    // 배치로그 생성
                    FileUtils.setBatchLog(
                            FileUtils.strCCO_C
                            , FileUtils.strProgram_Id
                            , "30"
                            , strGetToday
                            , String.valueOf(intCnt)
                            , String.valueOf(intCnt)
                            , "0"
                            , "JAVA Program Success"
                    );
                } catch (SQLException ex) {

                    try {
                        if (out != null) out.close();
                        if (cursor != null) cursor.close();
                        if (cStmt != null) cStmt.close();
                        if (db.tibero != null) db.tibero.close();

                        // 배치실패로그 생성
                        FileUtils.setBatchLog(
                                FileUtils.strCCO_C
                                , FileUtils.strProgram_Id
                                , "40"
                                , strGetToday
                                , String.valueOf(intCnt)
                                , String.valueOf(intCnt)
                                , "1"
                                , "ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage()
                        );

                        System.out.println(String.format("[END][%s][서비스강제종료 => (%s) : %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                                , FileUtils.o_result_code
                                , FileUtils.o_result_msg));

                        System.out.println("ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage());
                        ex.printStackTrace();

                    } catch (Exception e) {
                        System.out.println("Exception occured : " + e.toString());
                    }

                    System.exit(1);

                } finally {
                    try {
                        if (out != null) out.close();
                        if (cursor != null) cursor.close();
                        if (cStmt != null) cStmt.close();
                        if (db.tibero != null) db.tibero.close();
                    } catch (Exception e) {
                        System.out.println("Exception occured : " + e.toString());
                    }
                }
            }
        } catch (IOException ex) {
            try {
                if (out != null) out.close();
                if (cursor != null) cursor.close();
                if (cStmt != null) cStmt.close();
                if (db.tibero != null) db.tibero.close();

                // 배치실패로그 생성
                FileUtils.setBatchLog(
                        FileUtils.strCCO_C
                        , FileUtils.strProgram_Id
                        , "40"
                        , strGetToday
                        , String.valueOf(intCnt)
                        , String.valueOf(intCnt)
                        , "1"
                        , ex.toString()
                );

                System.out.println(String.format("[END][%s][서비스강제종료 => (%s) : %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , FileUtils.o_result_code
                        , FileUtils.o_result_msg));

                ex.printStackTrace();

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }

            System.exit(1);

        } catch (SQLException ex) {
            try {
                if (out != null) out.close();
                if (cursor != null) cursor.close();
                if (cStmt != null) cStmt.close();
                if (db.tibero != null) db.tibero.close();

                // 배치실패로그 생성
                FileUtils.setBatchLog(
                        FileUtils.strCCO_C
                        , FileUtils.strProgram_Id
                        , "40"
                        , strGetToday
                        , String.valueOf(intCnt)
                        , String.valueOf(intCnt)
                        , "1"
                        , "ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage()
                );

                System.out.println(String.format("[END][%s][서비스강제종료 => (%s) : %s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , FileUtils.o_result_code
                        , FileUtils.o_result_msg));

                System.out.println("ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage());
                ex.printStackTrace();

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }

            System.exit(1);

        } finally {
            try {
                if (out != null) out.close();
                if (cursor != null) cursor.close();
                if (cStmt != null) cStmt.close();
                if (db.tibero != null) db.tibero.close();
            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }
        }

        isLocking  = false;

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END);
    }


    private static boolean IsProcessing(String strCCO_C, String strRecord_dv) {

        boolean bSuccess = false;

        DBHandler db = new DBHandler();
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            StringBuffer sbSelectSQL = new StringBuffer();
            sbSelectSQL.append("SELECT");
            sbSelectSQL.append("FROM MEM_FILE_BAT_RG AS A");
            sbSelectSQL.append("WHERE FILE_NM IN (SELECT FILE_NM                    ");
            sbSelectSQL.append("                    FROM MEM_FILE_BAT_RG AS B       ");
            sbSelectSQL.append("                   WHERE BAT_PC_ST_C IN ('P')       ");
            sbSelectSQL.append("                     AND RECORD_dV = 'T'            ");
            sbSelectSQL.append("                   GROUP BY FILE_NM                 ");
            sbSelectSQL.append(")                                                   ");
            sbSelectSQL.append("AND RECORD_DV = 'T'                                 ");
            sbSelectSQL.append("AND FILE_NM LIKE 'HFG%'                             ");
            sbSelectSQL.append("AND SUBSTR(FILE_NM, 0, 7) = ?                       ");
            sbSelectSQL.append("AND SUBSTR(FILE_NM, 9, 2) = ?                       ");
            sbSelectSQL.append("AND SUBSTR(FILE_NM, 9, 2) NOT IN ('60', '92')       ");

            pStmt = db.tibero.prepareStatement(sbSelectSQL.toString());

            pStmt.setString(1, strCCO_C);
            pStmt.setString(1, strRecord_dv);
            rs = pStmt.executeQuery();

            String tmpFileName = null;
            while (rs.next()) {
                tmpFileName = StringHelper.SetReplace(rs.getString(1));

                retFileList.add(tmpFileName);
                bSuccess = true;
                System.out.println(String.format("처리 파일명 : [%s]", tmpFileName));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage());
            ex.printStackTrace();
        } finally {

            try {
                if (rs!= null) rs.close();
                if (pStmt != null) pStmt.close();
                if (db.tibero != null) db.tibero.close();
            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }
        }

        return bSuccess;
    }

    private static void DownloadComplete(String sourceFile) {
        boolean bSuccess = false;

        int file = sourceFile.lastIndexOf("/");
        int ext = sourceFile.lastIndexOf(".");
        int fileLength = sourceFile.length();
        String fileName = sourceFile.substring(file +1, ext);
        String extName = sourceFile.substring(ext +1, fileLength);
        String strFileName = fileName + "." + extName;

        System.out.println(strFileName);

        DBHandler db = new DBHandler();
        PreparedStatement pStmt = null;

        try {

            StringBuffer sbSelectSQL = new StringBuffer();
            sbSelectSQL.append("UPDATE MEM_FILE_BAT_RG      ");
            sbSelectSQL.append("SET BAT_PC_ST_C = 'D'       ");
            sbSelectSQL.append(", CH_DTTI = ?               ");
            sbSelectSQL.append("WHERE FILE_NM = ?           ");
            sbSelectSQL.append("  AND BAT_PC_ST_C = 'P'     ");

            pStmt = db.tibero.prepareStatement(sbSelectSQL.toString());
            pStmt.setString(1, CommonUtil.getToday());
            pStmt.setString(2, strFileName);
            pStmt.executeQuery();

        } catch (SQLException ex) {
            System.out.println("ERROR[" + ex.getErrorCode() + "] : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (pStmt != null) pStmt.close();
                if (db.tibero != null) db.tibero.close();
            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }
        }
    }
}
