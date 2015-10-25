package com.jarvisef.test;

/**
 * Created by youngsunkr on 2015-07-05.
 */
public class CommonConstants {
    public static final String DATE_DTTI                    = "yyyyMMddHHmmss";
    public static final String DATE_DT                      = "yyyyMMdd";
    public static final String DATE_HR                      = "HHmmss";
    public static final String DATE_MS                      = "yyyyMMddHHmmssSSS";
    public static final String DATE_STANDARD                = "yyyy-MM-dd HH:mm:ss";

    public static final String CHARSET_EUC_KR               = "EUC-KR";
    public static final String BLANK                        = "";

    public static final String CHNL_C                       = "CHNL_C";
    public static final String MEB_CST_NO                   = "MEB_CST_NO";
    public static final String PSWD_TP_C                    = "PSWD_TP_C";
    public static final String ONLINE                       = "01";
    public static final String MOBILE                       = "02";
    public static final String CHNL_C_PC_WEB                = "01";
    public static final String CHNL_C_MOB_WEB               = "02";
    public static final String CHNL_C_MOB_APP               = "03";
    public static final String GRP_C                        = "GRP_C";

    public static final String JOIN_YN                      = "JOIN_YN";
    public static final String JOIN_YN_Y                    = "Y";
    public static final String JOIN_YN_N                    = "N";

    public static final String USE_YN                       = "USE_YN";     /* 사용_여부_공통코드 */
    public static final String USE_YN_Y                     = "Y";
    public static final String USE_YN_N                     = "N";
    public static final String CTS_DV_C_EVENT               = "01";         /* 컨텐트_구분_코드 : 01 - 혜택-이벤트 */
    public static final String CTS_DV_C_MEMPRD              = "02";         /* 컨텐트_구분_코드 : 02 - 혜택-멤버십상품 */
    public static final String CTS_DV_C_SPARETIME           = "03";         /* 컨텐트_구분_코드 : 03 - 행복나무-여가 */
    public static final String CTS_DV_C_FUTURE              = "04";         /* 컨텐트_구분_코드 : 04 - 행복나무-미래 */

    public static final String RST_CD                       = "RST_CD";
    public static final String RST_MSG                      = "RST_MSG";
    public static final String HNM_SESSION                  = "HNM_SESSION";

    public static final String CATEGORY_TYPE                = "CATEGORY_TYPE";
    public static final String CATEGORY_TYPE_MY             = "my";
    public static final String CATEGORY_TYPE_COIN           = "coin";
    public static final String CATEGORY_TYPE_TREE           = "tree";
    public static final String CATEGORY_TYPE_MAIN           = "main";
    public static final String CATEGORY_TYPE_BENEFIT        = "benefit";
    public static final String RST_CD_SUCCESS               = "00000";

    //-------------------------------------------- Response Code -------------------------------------------- //

    // 공통
    public static final String RST_CD_ALREADY_EXIST_LOADING     = "96666";
    public static final String RST_CD_NEED_TO_APP_LOGIN         = "97776";
    public static final String RST_CD_NEED_LOGIN                = "97777";

    // 회원가입
    public static final String RST_CD_PASSWORD_EXCEEDED         = "10002";
    public static final String RST_CD_PASSWORD_INCORRECT        = "10003";
    public static final String RST_CD_NOT_EXSIST_USERID         = "10004";
    public static final String RST_CD_EXSIST_USERID             = "10005";
    public static final String RST_CD_NOT_A_MEMBER              = "10006";
    public static final String RST_CD_IOS_PASSWORD_EXCEEDED     = "10007";
    public static final String RST_CD_NEED_OTHER_CI             = "10008";

    // 쿠폰
    public static final String CAN_ISSUE_CPON                   = "30001";

    // 머니
    public static final String SEND_TO_ME                        = "02001";
    public static final String WRONG_NAME                        = "02002";
    public static final String LIMITED_ONCE_USED_MAX_COIN        = "02003";
    public static final String LIMITED_DAILY_MAX_COIN            = "02004";
    public static final String NOT_ENOUGH_LIMITED_DAILY_COIN     = "02005";
    public static final String MINUS_AVL_COIN                    = "01111";
    public static final String HAS_ETF_COIN                      = "01119";
    public static final String NOT_ENOUGH_COIN                   = "00034";
    public static final String DON_MIN_COIN                      = "01120";
    public static final String DON_MAX_COIN                      = "01121";

    // N wallet
    public static final String NOT_EXSIST_MDM_DRM_NO_COLUMN      = "24443";
    public static final String NOT_EXSIST_MDM_DRM_NO             = "24444";

    // 오류
    public static final String RST_CD_NO_DATA                    = "90001";
    public static final String RST_CD_NO_SERVICE                 = "900021";
    public static final String RST_CD_NO_NEED_AUTHORITY          = "95555";
    public static final String RST_CD_IO_ERR                     = "98886";
    public static final String RST_CD_UNSUPP_ENCODE_ERR          = "98887";
    public static final String RST_CD_DB_ERR_SP_RST              = "98888";
    public static final String RST_CD_DB_ERR_QRY                 = "98889";
    public static final String RST_CD_DATA_PROC_ERR              = "99000";
    public static final String RST_CD_UNKNOWN_ERROR              = "99999";

    public static final String RESULT_CODE                       = "RESULT_CODE";
    public static final String RESULT_CODE_SUCCESS               = "00000";
    public static final String KCB_RST_CD_SUCCESS                = "B000";
    public static final String OCB_RST_CD_SUCCESS                = "B000";

    public static final int FOR_SINGLE_LOGIN_RANDOM_NUMBER      = 100000;

    // CARD-BIN : 9420-18XX-XXXX-XXXX 고정갑
    public static final int BCR_CARD_NO                         = 942018;

    // 행복감 상수
    public static final String PRODUCT_THE_DE_RSON_C            = "30002";
    public static final String HAPYCOIN_THE_DE_RSON_C           = "30001";

    // 쇼핑-코인사용
    public static final String COIN_USE_THE_DE_RSON_C           = "22003";

    // PasswordChecker
    public static final int PSWD_ERR_CNT                                            = 3;
    public static final int ACCOUNT_PWD_DUP_WORD_CNT                                = 3;
    public static final int ACCOUNT_PWD_CONTINOUS_WORD_CNT                          = 3;
    public static final int ACCOUNT_PWD_DUP_BIRTH_MOBILE_NUM_CNT                    = 4;
    public static final int ACCOUNT_PWD_NUMBER_AND_SPETIAL_UPPER_LOWER_CASE_LETTER  = 3;


}
