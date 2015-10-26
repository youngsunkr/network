package com.jarvisef.test;
/********************************************************
 * 하나금융그룹 통합 멤서십 프로젝트
 ********************************************************
 *
 * 1. 클래스 명 : Membership_Batch.java
 * 2. Description : 멤버십 마감 배치
 * 3. 관련테이블
 * 4. Author : 노영선
 * 5. 작성일 : 2015-10-26
 * 6. 수정일 :
 * <날짜>    : <수정내용>(<개발자명>)
 ********************************************************/

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-10-26.
 */
public class Membership_Batch {

    public static String result_code = "99999";

    public static void main(String[] args) throws Exception {

        StopWatch stopWatch = new StopWatch();

        System.out.println("=====================================");
        System.out.println("[START] 배치 마감 업무 시작 [START]");
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

            DoWork(args);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }

        /* 수행시간 출력 */
        stopWatch.toString();

        System.out.println(String.format("[END TIME][%s]", CommonUtil.getDate(CommonConstants.DATE_STANDARD)));
        System.out.println("[END] 배치 마감 업무 종료 [END]");
        System.out.println("=====================================");

        if (result_code.equals("00000")) {
            System.exit(0);
        } else {
            System.exit(1);
        }

    }

    private static void DoWork(String[] args) {

        DBHandler db = new DBHandler();
        CallableStatement cStmt = null;

        try {

            String strProcessSP = null;
            StringBuffer sbProcessSP = new StringBuffer();
            List<String> arrList = new ArrayList<>();

            sbProcessSP.append("{call ");
            sbProcessSP.append(args[0] + "( ");
            sbProcessSP.append("?,?,?,?,?,?, ");
            String[] param = null;
            for (int i = 0; i<args.length; i++) {
                param = args[i].toString().split(",");
                if (CommonUtil.isNotNull(param)) {
                    sbProcessSP.append("?, ");
                    arrList.add(StringHelper.SetReplace(param[0].trim()));
                } else {
                    break;
                }
            }
            int len = sbProcessSP.toString().lastIndexOf(",");
            strProcessSP = sbProcessSP.toString().substring(0, len).concat(")}");

            cStmt = db.tibero.prepareCall(strProcessSP);
            if (cStmt != null) {
                cStmt.registerOutParameter(1, Types.VARCHAR);
                cStmt.registerOutParameter(2, Types.VARCHAR);
                cStmt.registerOutParameter(3, Types.VARCHAR);
                cStmt.setString(4, CommonUtil.getDate(CommonConstants.DATE_MS));
                cStmt.setString(5, "BATCH");
                cStmt.setString(6, CommonUtil.getDate(CommonConstants.DATE_DTTI));
            }

            int j = 7;
            for (String data : arrList) {
//                System.out.println(String.format("%s %s", j, data));
                if (data.toLowerCase().equals("null")) {
                    cStmt.setString(j, null);
                } else {
                    cStmt.setString(j, data);
                }
                j++;
            }

            FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN, args[0]);

            /********************
             * 업무 SP 실행
             ********************/
            if (cStmt != null) {
                cStmt.executeUpdate();
                result_code = cStmt.getString(1);
            }

        } catch (SQLException ex) {

            try {
                if (db.tibero != null) db.tibero.close();

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
                if (cStmt != null) cStmt.close();
                if (db.tibero != null) db.tibero.close();
            } catch(Exception e) {
                System.out.println("Exception occured : " + e.toString());
            }
        }

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END, args[0]);
    }
}
