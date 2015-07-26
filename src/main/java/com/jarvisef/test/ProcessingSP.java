package com.jarvisef.test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-07-10.
 */
public class ProcessingSP {

    public static List<String> retFileList = new ArrayList<String>();

    public static void main(String[] args) throws Exception {

        if (args != null) {
            for (int i=0; i<args.length; i++) {
                System.out.println("args[" + i + "]=" + args[i]);
            }
        }

        DoWork(args[0], args[1], CommonUtil.getTodayDt());

        System.exit(1);
    }

    private static void DoWork(String arg, String s, String todayDt) throws SQLException {
        if (IsProcessing()) {

            for (String strFileName : retFileList) {
                CallProcedure(strFileName);
            }

            retFileList.clear();
        }
    }

    private static void CallProcedure(String sourceFile) throws SQLException {
        String strCCO_C = sourceFile.split("_")[0];
        String strRecord_DV = sourceFile.split("_")[1];
        String strDate = sourceFile.split("_")[2];
        String strSndFlag = "10";
        String strFileName = String.format("%s_%s_%s_%s.sam", strCCO_C, strRecord_DV, strDate, strSndFlag);

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

        boolean bSuccess = FileUtils.setBatchLog(
                FileUtils.strCCO_C
                , FileUtils.strProgram_Id
                , "20"
        );

        if (!bSuccess) {
            FileUtils.setBatchLog(
                    FileUtils.strCCO_C
                    , FileUtils.strProgram_Id
                    , "40"
            );

            System.out.println(String.format("[BGN][%s] [서비스강제종료 => (%s) : %s"
                    , CommonUtil.getDate(CommonConstants.DATE_STANDARD)
                    , FileUtils.o_result_code
                    , FileUtils.o_result_msg));

            System.exit(1);
        }

        DBHandler db = new DBHandler();
        CallableStatement cStmt = null;
        String strProcessSP = null;

        if (CommonUtil.isNotNull(strFileName)) {
            switch (strRecord_DV) {
                case "10" :
                    strProcessSP = "{call MEM_PT_B_009(?,?,?,?,?, ?,?,?,?,?,?,?)}";
                    cStmt = db.tibero.prepareCall(strProcessSP);
                    cStmt.registerOutParameter(1, Types.VARCHAR);
                    cStmt.registerOutParameter(2, Types.VARCHAR);
                    cStmt.registerOutParameter(3, Types.VARCHAR);
                    cStmt.setString(4, CommonUtil.getDate(CommonConstants.DATE_MS));
                    cStmt.setString(5, "Batch");
                    cStmt.setString(6, CommonUtil.getDate(CommonConstants.DATE_DTTI));
                    cStmt.setString(7, FileUtils.strProgram_Id);
                    cStmt.setString(8, strRecord_DV);
                    cStmt.setString(9, strCCO_C);
                    cStmt.setString(10, strDate);
                    cStmt.setString(11, strFileName);
                    break;
                case "20" :
                    strProcessSP = "{call MEM_PT_B_009(?,?,?,?,?, ?,?,?,?,?,?,?)}";
                    cStmt = db.tibero.prepareCall(strProcessSP);
                    cStmt.registerOutParameter(1, Types.VARCHAR);
                    cStmt.registerOutParameter(2, Types.VARCHAR);
                    cStmt.registerOutParameter(3, Types.VARCHAR);
                    cStmt.setString(4, CommonUtil.getDate(CommonConstants.DATE_MS));
                    cStmt.setString(5, "Batch");
                    cStmt.setString(6, CommonUtil.getDate(CommonConstants.DATE_DTTI));
                    cStmt.setString(7, FileUtils.strProgram_Id);
                    cStmt.setString(8, strRecord_DV);
                    cStmt.setString(9, strCCO_C);
                    cStmt.setString(10, strDate);
                    cStmt.setString(11, strFileName);
                    break;
                case "60" :
                    strProcessSP = "{call MEM_PT_B_009(?,?,?,?,?, ?,?,?,?,?,?,?)}";
                    cStmt = db.tibero.prepareCall(strProcessSP);
                    cStmt.registerOutParameter(1, Types.VARCHAR);
                    cStmt.registerOutParameter(2, Types.VARCHAR);
                    cStmt.registerOutParameter(3, Types.VARCHAR);
                    cStmt.setString(4, CommonUtil.getDate(CommonConstants.DATE_MS));
                    cStmt.setString(5, "Batch");
                    cStmt.setString(6, CommonUtil.getDate(CommonConstants.DATE_DTTI));
                    cStmt.setString(7, FileUtils.strProgram_Id);
                    cStmt.setString(8, strRecord_DV);
                    cStmt.setString(9, strCCO_C);
                    cStmt.setString(10, strDate);
                    cStmt.setString(11, strFileName);
                    break;
            }

            FileUtils.setConsoleLog(FileUtils.ConsoleLogType.BGN);

            try {
                cStmt.executeUpdate();
            } catch (SQLException ex) {
                FileUtils.setBatchLog(
                        FileUtils.strCCO_C
                        , FileUtils.strProgram_Id
                        , "40"
                );

                System.out.println("Error[" + ex.getErrorCode() + "] : " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                try {
                    if (cStmt != null) cStmt.close();
                    if (db.tibero != null) db.tibero.close();
                } catch (Exception e) {
                    System.out.println("Exception occured : " + e.toString());

                }
            }
        }

        FileUtils.setBatchLog(
                FileUtils.strCCO_C
                , FileUtils.strProgram_Id
                , "30"
        );

        FileUtils.setConsoleLog(FileUtils.ConsoleLogType.END);
    }

    private static boolean IsProcessing() {
        boolean bSuccess = false;

        DBHandler db = new DBHandler();
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {

            StringBuffer sbSelectSQL = new StringBuffer();
            sbSelectSQL.append("SELECT COUNT(*), FILE_NM ");

            System.out.println(sbSelectSQL.toString());
            pStmt = db.tibero.prepareCall(sbSelectSQL.toString());

            rs = pStmt.executeQuery();

            int cnt = 0;
            String tempFileName = null;
            while (rs.next()) {
                tempFileName = rs.getString(1);
                cnt++;
            }

            if (cnt == 1) {
                retFileList.add(tempFileName);
                bSuccess = true;
            }

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
