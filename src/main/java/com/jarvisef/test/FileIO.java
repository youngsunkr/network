package com.jarvisef.test;

import java.io.*;

/**
 * Created by youngsunkr on 2015-05-18.
 */
public class FileIO {
    public static void main(String[] args) throws IOException{
//        FileOutputStream output = new FileOutputStream("d:/out.txt");
//        for(int i=1; i<11; i++) {
//            String data = i+" 번째 줄입니다.\r\n";
//            output.write(data.getBytes());
//        }
//        output.close();

        /*
        * FileWriter �� �̿��ϸ� byte �迭 ��� ���ڿ��� ���� ���Ͽ� �� ���� �ִ�.
        * */
        FileWriter fw = new FileWriter("d:/out.txt");
        for(int i=1; i<11; i++) {
            String data = i+" 번째 줄입니다.\r\n";
            fw.write(data);
        }
        fw.close();
//
//        FileWriter fw2 = new FileWriter("d:/out.txt", true);
//        for (int i=11; i<21; i++) {
//            String data = i + " ��° ���Դϴ�.\r\n";
//            fw2.write(data);
//        }
//        fw2.close();

//        /*
//        * PrintWriter�� �̿��ϸ� \r\n�� �����̴� ��� println�̶�� �޽�带 ����� �� �ְ� �ȴ�.
//        * */
        PrintWriter pw = new PrintWriter("d:/out.txt");
        for(int i=1; i<11; i++) {
            String data = i+" 번째 줄입니다.";
            pw.println(data);
        }
        pw.close();


        PrintWriter pw2 = new PrintWriter(new FileWriter("d:/out.txt", true));
        for(int i=11; i<21; i++) {
            String data = i+" 번째 줄입니다.";
            pw2.println(data);
        }
        pw2.close();

        /*
        * ���� �б�
        * */
        BufferedReader br = new BufferedReader(new FileReader("d:/out.txt"));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            System.out.println(line);
        }
        br.close();
    }


}
