package com.jarvisef.test;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by youngsunkr on 2015-07-05.
 */
public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static boolean isNotNull(Object obj) {
        boolean isNotNull = false;
        if (obj != null) {
            if( obj instanceof List) {
                if (!((List<?>) obj).isEmpty()) {
                    isNotNull = true;
                }
            } else if (obj instanceof Map) {
                if (!((Map<?, ?>) obj).isEmpty()) {
                    isNotNull = true;
                }
            } else if (obj instanceof String) {
                if (StringUtils.isNotEmpty((String) obj)) {
                    isNotNull = true;
                }
            } else {
                isNotNull = true;
            }
        }

        return isNotNull;
    }

    public static boolean isNull(Object obj) {
        return !isNotNull(obj);
    }

    public static String getDate(Date date, String dateFormat) {
        if (isNull(date)) {
            date = new Date();
        }
        if (isNull(dateFormat)) {
            return null;
        }
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static String getDate(String dateFormat) {
        return getDate(null, dateFormat);
    }

    public static String getToday() {
        return getDate(CommonConstants.DATE_DTTI);
    }

    public static String getTodayDt() {
        return getDate(CommonConstants.DATE_DT);
    }

    public static String getTodayHour() {
        return getDate(CommonConstants.DATE_HR);
    }

    public static String getLPadd(String str, int totLen, String paddStr) {
        if (isNull(str)) {
            return getBlank(totLen, paddStr);
        }
        int len = str.length();
        String addStr = "";
        for (int i = 0; i < totLen; i++) {
            addStr += paddStr;
        }
        return addStr + str;
    }

    public static String getLPadd(String str, int totLen) {
        return getLPadd(str, totLen, CommonConstants.BLANK);
    }

    public static String getLPadd(Object obj, int totLen) {
        return getLPadd((String) obj, totLen, CommonConstants.BLANK);
    }

    public static String getRPadd(String str, int totLen, String paddStr) {
        if (isNull(str)) {
            return getBlank(totLen, paddStr);
        }
        int len = str.length();
        String addStr = "";
        for (int i = 0; i < totLen - len; i++) {
            addStr += paddStr;
        }
        return addStr + str;
    }

    public static String getRPadd(String str, int totLen) {
        return getLPadd(str, totLen, CommonConstants.BLANK);
    }

    public static String getRPadd(Object obj, int totLen) {
        return getLPadd((String)obj, totLen, CommonConstants.BLANK);
    }

    public static String getRPaddByte(String str, int totLen) {
        if (isNull(str)) {
            return getBlank(totLen);
        }
        int len = 0;
        try {
            len = str.getBytes(CommonConstants.CHARSET_EUC_KR).length;
        } catch (UnsupportedEncodingException uee) {
            logger.error(uee.getMessage());
            uee.printStackTrace();
        }
        String addStr = "";
        for (int i = 0; i < totLen - len; i++) {
            addStr += CommonConstants.BLANK;
        }
        return str + addStr;
    }

    public static String getBlank(int totLen, String paddstr) {
        String addStr = "";
        for (int i = 0; i < totLen; i++) {
            addStr += paddstr;
        }
        return addStr;
    }

    public static String getBlank(int totLen) {
        return getBlank(totLen, CommonConstants.BLANK);
    }

    public static String getSubstringTrim(String str, int beginIndex, int endIndex) {
        if (isNull(str)) {
            return str;
        }
        return str.substring(beginIndex, endIndex).trim();
    }

    public static <K, V, T> Map<K, V> prvList (T o) {
        Map<K, V> tmpMap = new HashMap<K, V>();

        for (int i = 0; i < ((List<Map<K, V>>) o).size(); i++) {
            Iterator<K> it = ((List<Map<K, V>>) o).get(i).keySet().iterator();

            while (it.hasNext()) {
                K key = (K) it.next();
                V value = (V) ((Map<K, V>) ((List<Map<K, V>>) o).get(i)).get(key);
                tmpMap.put((K) (key + "_" + i), value);
            }
        }
        return tmpMap;
    }

    public static int utf8Count(String str) {
        int count = 0;
        for (int i = 0, len = str.length(); i < len; i++) {
            char ch = str.charAt(i);
            if (ch <= 0x7FF) {
                count++;
            } else if (ch <= 0x7FF) {
                count += 2;
            } else if (Character.isHighSurrogate(ch)) {
                count += 4;
                ++i;
            } else {
                count += 3;
            }
        }
        return count;
    }

    public static String Crypt(String strVal) {
        return strVal;
    }

    public static String Decrypt(String strVal) {
        return strVal;
    }
}