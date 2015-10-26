package com.jarvisef.test;

/********************************************************
 * 하나금융그룹 통합 멤서십 프로젝트
 ********************************************************
 *
 * 1. 클래스 명 : ProcessingSP.java
 * 2. Description : 배치전문 업무처리 SP호출 클래스
 * 3. 관련테이블
 * 4. Author : 노영선
 * 5. 작성일 : 2015-07-10
 * 6. 수정일 :
 * <날짜>    : <수정내용>(<개발자명>)
 ********************************************************/

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-07-10.
 */
public class ProcessingSP {

    public static List<String> retFileList = new ArrayList<String>();
    public static String result_code = "99999";

    public static void main(String[] args) throws Exception {

        StopWatch stopWatch = new StopWatch();

        System.out.println("=====================================");
        System.out.println("[START] 배치처리 SP 호출 업무 시작 [START]");
        System.out.println(String.format("[START TIME][%s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)));

        try {

            DoWork();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /* 수행시간 출력 */
        stopWatch.toString();

        System.out.println(String.format("[END TIME][%s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)));
        System.out.println("[END] 배치처리 SP 호출 업무 종료 [END]");
        System.out.println("=====================================");

        if (result_code.equals("00000")) {
            System.exit(0);
        } else {
            System.exit(1);
        }

    }

    private static void DoWork() throws SQLException {
        if (IsProcessing()) {

            for (String strFileName : retFileList) {
                CallProcedure(strFileName);
            }

            retFileList.clear();
        }
    }

    private static void CallProcedure(String sourceFile) throws SQLException {
        String strCCO_C = sourceFile.split("_")[0];             // 관계사 코드
        String strRecord_DV = sourceFile.split("_")[1];         // 배치 업무구분
        String strDate = sourceFile.split("_")[2];              // 년월일
        String strSndFlag = "10";                               // 송수신구분
        String strFileName = String.format("%s_%s_%s_%s.sam", strCCO_C, strRecord_DV, strDate, strSndFlag);

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

        DBHandler db = new DBHandler();
        CallableStatement cStmt = null;
        String strProcessSP = null;


        switch (strRecord_DV) {
            case "10" : // 코인 적립 배치 등록(IF_PT_006)
                strProcessSP = "{call MEM_PT_B_009(?,?,?,?,?,?,?, ?,?,?,?,?)}";
                cStmt = db.tibero.prepareCall(strProcessSP);
                break;
            case "20" : // 코인 사용 배치 등록(IF_PT_007)
                strProcessSP = "{call MEM_PT_B_010(?,?,?,?,?,?,?, ?,?,?,?,?)}";
                cStmt = db.tibero.prepareCall(strProcessSP);
                break;
            case "21" : // 고객 가용/소멸예정코인 조회
                strProcessSP = "{call MEM_PT_B_012(?,?,?,?,?,?,?, ?,?,?,?,?)}";
                cStmt = db.tibero.prepareCall(strProcessSP);
                break;
            case "60" : // 코인 적립 배치 등록(IF_PT_006)
                strProcessSP = "{call MEM_COP_B_001(?,?,?,?,?,?,?, ?,?,?,?,?)}";
                break;
            case "91" : // 코인 적립 배치 등록(IF_PT_006)
                strProcessSP = "{call MEM_MBR_S_037(?,?,?,?,?,?, ?,?,?,?,?,?)}";
                cStmt = db.tibero.prepareCall(strProcessSP);
                break;
        }

        if (cStmt != null) {
            cStmt.registerOutParameter(1, Types.VARCHAR);
            cStmt.registerOutParameter(2, Types.VARCHAR);
            cStmt.registerOutParameter(3, Types.VARCHAR);
            cStmt.setString(4, CommonUtil.getDate(CommonConstants.DATE_MS));
            cStmt.setString(5, "JAVA");
            cStmt.setString(6, CommonUtil.getDate(CommonConstants.DATE_DTTI));
            cStmt.setString(7, FileUtils.strProgram_Id);
            cStmt.setString(8, strRecord_DV);
            cStmt.setString(9, strCCO_C);
            cStmt.setString(10, strDate);
            cStmt.setString(11, strFileName);
        }

        /********************
         * 업무 SP 실행
         ********************/

        try {

            if (cStmt != null) {
                cStmt.executeUpdate();
                result_code = cStmt.getString(1);
                System.out.println(String.format("실행완료 [%s] : %s", result_code, strProcessSP));
            }

        } catch (SQLException ex) {

            try {

                if (db.tibero != null) db.tibero.close();

                System.out.println(String.format("[BGN][%s][서비스강제종료 => (%s) : %s]"
                        , CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                        , FileUtils.o_result_code
                        , FileUtils.o_result_msg));

                System.out.println("Error[" + ex.getErrorCode() + "] : " + ex.getMessage());
                ex.printStackTrace();

            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }

            System.exit(1);

        } finally {
            try {
                if (cStmt != null) cStmt.close();
                if (db.tibero != null) db.tibero.close();
            } catch (Exception e) {
                System.out.println("Exception occured : " + e.toString());

            }
        }

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END, strCCO_C, strRecord_DV);
    }

    private static boolean IsProcessing() {
        boolean bSuccess = false;

        DBHandler db = new DBHandler();
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {

            StringBuffer sbSelectSQL = new StringBuffer();
            sbSelectSQL.append("SELECT DISTINCT FILE_NM             ");
            sbSelectSQL.append("FROM MEM_FILE_BAT_RG AS A           ");
            sbSelectSQL.append("WHERE EXISTS (                      ");
            sbSelectSQL.append("    SELECT 1                        ");
            sbSelectSQL.append("    FROM MEM_FILE_BAT_RG AS B       ");
            sbSelectSQL.append("    WHERE A_FILE_NM = B.FILE_NM     ");
            sbSelectSQL.append("    AND B.BAT_PC_ST_C IN ('U')      ");
            sbSelectSQL.append(")                                   ");

//            System.out.println(sbSelectSQL.toString());
            pStmt = db.tibero.prepareCall(sbSelectSQL.toString());

            rs = pStmt.executeQuery();

            String tempFileName = null;
            while (rs.next()) {
                tempFileName = rs.getString(1);

                retFileList.add(tempFileName);
                bSuccess = true;
            }

            result_code = "00000";

        } catch (SQLException ex) {
            System.out.println("Error[" + ex.getErrorCode() + "] : " + ex.getMessage());
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
