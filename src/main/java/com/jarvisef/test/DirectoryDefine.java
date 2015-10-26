package com.jarvisef.test;

/********************************************************
 * 하나금융그룹 통합 멤서십 프로젝트
 ********************************************************
 *
 * 1. 클래스 명 : DirectoryDefine.java
 * 2. Description : APP서버의 디렉터리에 대한 정의를 설정한다.
 * 3. 관련테이블
 * 4. Author : 노영선
 * 5. 작성일 : 2015-06-10
 * 6. 수정일 :
 * <날짜>    : <수정내용>(<개발자명>)
 ********************************************************/

import java.io.File;

/**
 * Created by youngsunkr on 2015-06-15.
 */
public class DirectoryDefine {

//    /********************************
//     ********** 운영 디렉터리 **********
//     ******************************** */
//    /********************************
//     * 기본 디렉터리
//     ******************************** */
//    public static final String dataDirectory                = "data";
//
//    /********************************
//     * 배치 데이터 디렉터리
//     ******************************** */
//    public static final String batchDirectory               = "batch";
//
//    /********************************
//     * 기본 제휴사 디렉터리
//     ******************************** */
//    public static final String base_Directory               = File.separator + dataDirectory + File.separator + batchDirectory + File.separator;
//
//    /********************************
//     * 직제휴사 디렉터리
//     ******************************** */
//    public static final String cop_base_Directory           = base_Directory + "COP";
//
//    /********************************
//     * 개인정보 증적 디렉터리
//     ******************************** */
//    public static final String person_log_Direcotry          = File.separator + dataDirectory + File.separator + "PIHISTORY" + File.separator;
//
//    /********************************
//     * 로그 디렉터리
//     ******************************** */
//    public static final String logs                           = "logs";
//    public static final String log_Direcotry                  = File.separator + logs + File.separator + batchDirectory + File.separator;
//
//    /********************************
//     * 설정 디렉터리
//     ******************************** */
//    public static final String dbconfig                       = File.separator + "app" + File.separator + batchDirectory + File.separator + "config" + File.separator + "db.properties";




    /********************************
     ********** 개발 디렉터리 **********
     ******************************** */
    /********************************
     * 기본 디렉터리
     ******************************** */
    public static final String dataDirectory               = "d:/전문파일테스트/data/";

    /********************************
     * 배치 데이터 디렉터리
     ******************************** */
    public static final String batchDirectory               = "batch";

    /********************************
     * 기본 제휴사 디렉터리
     ******************************** */
    public static final String base_Directory               = dataDirectory + File.separator + batchDirectory + File.separator;

    /********************************
     * 직제휴사 디렉터리
     ******************************** */
    public static final String cop_base_Directory           = dataDirectory + File.separator + "COP" + File.separator;

    /********************************
     * 개인정보 증적 디렉터리
     ******************************** */
    public static final String person_log_Direcotry         = dataDirectory + File.separator + "PIHISTORY" + File.separator;

    /********************************
     * 로그 디렉터리
     ******************************** */
    public static final String logs                         = "logs";
    public static final String log_Direcotry                = dataDirectory + File.separator + logs + File.separator + batchDirectory + File.separator;

    /********************************
     * 설정 디렉터리
     ******************************** */
    public static final String dbconfig                     = "d:/전문파일테스트/app" + File.separator + batchDirectory + File.separator + "config" + File.separator + "db.properties";



    /********************************
     * 수신 디렉터리
     ******************************** */
    public static final String RCV   	    = "RCV";

    /********************************
     * 송신 디렉터리
     ******************************** */
    public static final String SND   	    = "SND";

    /********************************
     * 백업 디렉터리
     ******************************** */
    public static final String BackupDir   	= "backup";

    public static String COP_DirPath(String cco_c, String savePath) {
        return cop_base_Directory.concat(cco_c + savePath);
    }

    public static String CCO_DirPath(String cco_c, String savePath) {
        return base_Directory.concat(cco_c + savePath);
    }
}



