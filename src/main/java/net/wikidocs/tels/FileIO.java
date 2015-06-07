package net.wikidocs.tels;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by youngsunkr on 2015-05-18.
 */
public class FileIO {
    public static void main(String[] args) throws Exception {
//        FileOutputStream output = new FileOutputStream("d:/out.txt");
//        for (int i=1; i<11; i++) {
//            String data = i + " 번째 줄입니다.\r\n";
//            output.write(data.getBytes());
//        }
//        output.close();

        /*
        * FileWriter 를 이용하면 byte 배열 대신 문자열을 직접 파일에 쓸 수가 있다.
        * */
//        FileWriter fw = new FileWriter("d:/out.txt");
//        for (int i=1; i<11; i++) {
//            String data = i + " 번째 줄입니다.\r\n";
//            fw.write(data);
//        }
//        fw.close();
//
//        FileWriter fw2 = new FileWriter("d:/out.txt", true);
//        for (int i=11; i<21; i++) {
//            String data = i + " 번째 줄입니다.\r\n";
//            fw2.write(data);
//        }
//        fw2.close();

//        /*
//        * PrintWriter를 이용하면 \r\n을 덧붙이는 대신 println이라는 메써드를 사용할 수 있게 된다.
//        * */
//        PrintWriter pw = new PrintWriter("d:/out.txt");
//        for (int i=1; i<11; i++) {
//            String data = i + " 번째 줄입니다.";
//            pw.println(data);
//        }
//        pw.close();
//
//        PrintWriter pw2 = new PrintWriter(new FileWriter("d:/out.txt", true));
//        for (int i=11; i<21; i++) {
//            String data = i + " 번째 줄입니다.";
//            pw2.println(data);
//        }
//        pw2.close();

        /*
        * 파일 읽기
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
