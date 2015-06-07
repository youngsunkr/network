package com.jarvisef.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngsunkr on 2015-06-07.
 */
public class StringHelper {

    public static String subString(String strData, int iStartPos, int iByteLength) {
        byte[] bytTemp = null;
        int iRealStart = 0;
        int iRealEnd = 0;
        int iLength = 0;
        int iChar = 0;

        try {
            // UTF-8로 변환하는경우 한글 2Byte, 기타 1Byte로 떨어짐
            bytTemp = strData.getBytes("EUC-KR");
            iLength = bytTemp.length;


            for(int iIndex = 0; iIndex < iLength; iIndex++) {
                if(iStartPos <= iIndex) {
                    break;
                }
                iChar = (int)bytTemp[iIndex];
                if((iChar > 127)|| (iChar < 0)) {
                    // 한글의 경우(2byte 통과처리)
                    // 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
                    iRealStart++;
                    iIndex++;
                } else {
                    // 기타 글씨(1Byte 통과처리)
                    iRealStart++;
                }
            }

            iRealEnd = iRealStart;
            int iEndLength = iRealStart + iByteLength;
            for(int iIndex = iRealStart; iIndex < iEndLength; iIndex++)
            {
                iChar = (int)bytTemp[iIndex];
                if((iChar > 127)|| (iChar < 0)) {
                    // 한글의 경우(2byte 통과처리)
                    // 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
                    iRealEnd++;
                    iIndex++;
                } else {
                    // 기타 글씨(1Byte 통과처리)
                    iRealEnd++;
                }
            }
        } catch(Exception e) {
            //
            //Log.d("DEBUG",e.getMessage());
        }

        return strData.substring(iRealStart, iRealEnd);
    }


    public static List<String> readLines(File file) throws Exception {
        if (!file.exists()) {
            return new ArrayList<String>();
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> results = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            results.add(line);
            line = reader.readLine();
        }

        return results;
    }
}
