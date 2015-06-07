package com.jarvisef.test;

import java.io.*;
import java.util.*;

/**
 * Created by youngsunkr on 2015-06-06.
 */
public class FileUtils {

    /**
     * Opens and reads a file, and returns the contents as one String.
     */
    public static String readFileAsString(String filename)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Open and read a file, and return the lines in the file as a list of
     * Strings.
     */
    public static List<String> readFileAsListOfStrings(String filename) throws Exception {
        List<String> records = new ArrayList<String>();
        //BufferedReader reader = new BufferedReader(new FileReader(filename));
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF8"));
        int lineNumber = 1;
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                records.add(line);

                // �о�� ���� �Ľ�

                if (line.toUpperCase().contains("[HEAD]")) {
                        System.out.println(line.toUpperCase());
                } else if (line.toUpperCase().contains("[DATA]")) {
                    System.out.println(line.toUpperCase());
                } else {
                    System.out.println(line.toUpperCase());
                }

                lineNumber++;
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return records;
    }

    /**
     * Reads a "properties" file, and returns it as a Map (a collection of key/value pairs).
     *
     * @param filename
     * @param delimiter
     * @return
     * @throws Exception
     */
    public static Map<String, String> readPropertiesFileAsMap(String filename, String delimiter)
            throws Exception {
        Map<String, String> map = new HashMap();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) continue;
            if (line.charAt(0) == '#') continue;
            // assumption here is that proper lines are like "String : <a href="http://xxx.yyy.zzz/foo/bar"" title="http://xxx.yyy.zzz/foo/bar"">http://xxx.yyy.zzz/foo/bar"</a>,
            // and the ":" is the delimiter
            int delimPosition = line.indexOf(delimiter);
            String key = line.substring(0, delimPosition - 1).trim();
            String value = line.substring(delimPosition + 1).trim();
            map.put(key, value);
        }
        reader.close();
        return map;
    }

    /**
     * Read a Java properties file and return it as a Properties object.
     */
    public static Properties readPropertiesFile(String canonicalFilename)
            throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(canonicalFilename));
        return properties;
    }

    /**
     * Save the given text to the given filename.
     *
     * @param canonicalFilename Like /Users/al/foo/bar.txt
     * @param text              All the text you want to save to the file as one String.
     * @throws IOException
     */
    public static void writeFile(String canonicalFilename, String text)
            throws IOException {
        File file = new File(canonicalFilename);
        //BufferedWriter out = new BufferedWriter(new FileWriter(file));
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
        out.newLine();  // line feed
        out.write(text);
        out.close();
    }

    /**
     * Write an array of bytes to a file. Presumably this is binary data; for plain text
     * use the writeFile method.
     */
    public static void writeFileAsBytes(String fullPath, byte[] bytes) throws IOException {
        OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fullPath));
        InputStream inputStream = new ByteArrayInputStream(bytes);
        int token = -1;

        while ((token = inputStream.read()) != -1) {
            bufferedOutputStream.write(token);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        inputStream.close();
    }

    public static void copyFile(File source, File destination) throws IOException {
        //if (!source.isFile() || !dest.isFile()) return false;

        byte[] buffer = new byte[100000];

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(source));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
            int size;
            while ((size = bufferedInputStream.read(buffer)) > -1) {
                bufferedOutputStream.write(buffer, 0, size);
            }
        } catch (IOException e) {
            // TODO may want to do something more here
            throw e;
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
            } catch (IOException ioe) {
                // TODO may want to do something more here
                throw ioe;
            }
        }
    }

    ///http://www.programkr.com/blog/MUjN1ADMwYT5.html
    /**
     * ���� ����Ʈ ������ ���� �б�, �� ���̳ʸ� ������ �д� ���� �׸� ��Ҹ�, ���� �� ����.
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("���� ���� ������ ���� ����Ʈ ������ �� �� �о� �� ����Ʈ: ");
            // �� �� �о� �� ����Ʈ
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("���� ���� ������ ���� ����Ʈ ������ �� �� �о� �� �� ����Ʈ: ");
            // �� �� �о� �� �� ����Ʈ
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            FileUtils.showAvailableBytes(in);
            // ���� �� �� �� ����Ʈ ���� ����Ʈ �迭 �� byteread ���� �� �� �ǵ� �ϴ� ����Ʈ
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * ���� ���� ������ ���� �б�, �� �д� �ؽ�Ʈ, ���� �� ���� ����
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("���� ���� ������ ���� ������ �д� �� �� �о� �� ����Ʈ: ");
            // �� �� �о� �� ����
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // �� ���� windows �Ʒ� \r\n �� �� ���� �Բ� ���� ��, ǥ�� �� ��.
                // ������ ���� �� �� ���� ���� ���̱� �� �� �� �� �� �� �ٲٴ�.
                // �׷��� ���� ������ \r �Ǵ� ���� \n.�׷��� ������, ����, ���� �� �� ���̴�.
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("���� ���� ������ ���� ������ �д� �� �� �о� �� �� ����Ʈ: ");
            // �� �� �� ���� ����
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // ���� ���� ���� �ǵ� ���� ���� �迭 �� charread ���� �� �� ���� ���� ��
            while ((charread = reader.read(tempchars)) != -1) {
                // ���� ���� ������ \r �� ���̱�
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i <charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * ���� �ൿ ���� ���� �б�, �� �д� ���ϴ� �Ǵ� ���� ����
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("���� �ൿ ���� �б� ���� ������ �д� ��� �� �� �� ��: ");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // �� �� �ǵ� �� �� �ǵ� null ���Ϸ� ���� ������
            while ((tempString = reader.readLine()) != null) {
                // �� ��ȣ ǥ��
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    /**
     * ���÷��� �Է� �� ���� �ϴ� ����Ʈ
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("���� �Է� ����Ʈ �帧 �ӿ� ����Ʈ ����:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}