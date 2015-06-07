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
            // UTF-8�� ��ȯ�ϴ°�� �ѱ� 2Byte, ��Ÿ 1Byte�� ������
            bytTemp = strData.getBytes("EUC-KR");
            iLength = bytTemp.length;


            for(int iIndex = 0; iIndex < iLength; iIndex++) {
                if(iStartPos <= iIndex) {
                    break;
                }
                iChar = (int)bytTemp[iIndex];
                if((iChar > 127)|| (iChar < 0)) {
                    // �ѱ��� ���(2byte ���ó��)
                    // �ѱ��� 2Byte�̱� ������ ���� ���ڴ� ���͵� ���� ��ŵ�Ѵ�
                    iRealStart++;
                    iIndex++;
                } else {
                    // ��Ÿ �۾�(1Byte ���ó��)
                    iRealStart++;
                }
            }

            iRealEnd = iRealStart;
            int iEndLength = iRealStart + iByteLength;
            for(int iIndex = iRealStart; iIndex < iEndLength; iIndex++)
            {
                iChar = (int)bytTemp[iIndex];
                if((iChar > 127)|| (iChar < 0)) {
                    // �ѱ��� ���(2byte ���ó��)
                    // �ѱ��� 2Byte�̱� ������ ���� ���ڴ� ���͵� ���� ��ŵ�Ѵ�
                    iRealEnd++;
                    iIndex++;
                } else {
                    // ��Ÿ �۾�(1Byte ���ó��)
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
