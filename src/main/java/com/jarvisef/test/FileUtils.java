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

                    //System.out.println(line.toUpperCase());

                    Packet recvPacket = new Packet();
                    recvPacket.addItem(Item.create("HEAD", 6, null));
                    recvPacket.addItem(Item.create("헤드1", 5, null));
                    recvPacket.addItem(Item.create("헤드2", 5, null));
                    recvPacket.addItem(Item.create("헤드3", 5, null));
                    recvPacket.addItem(Item.create("헤드4", 5, null));
                    recvPacket.addItem(Item.create("헤드5", 6, null));
                    recvPacket.parse(line.toUpperCase());
                    //System.out.println(line.toUpperCase());
                    //System.out.println(recvPacket.getItem("HEAD").raw());
                    System.out.println(recvPacket.getItem("헤드5").raw());

                } else if (line.toUpperCase().contains("[DATA]")) {
                    //System.out.println(line.toUpperCase());

                    Packet recvPacket = new Packet();
                    recvPacket.addItem(Item.create("DATA", 6, null));
                    recvPacket.addItem(Item.create("Data1", 6, null));
                    recvPacket.addItem(Item.create("Data2", 6, null));
                    recvPacket.addItem(Item.create("Data3", 6, null));
                    recvPacket.addItem(Item.create("Data4", 6, null));
                    recvPacket.addItem(Item.create("Data5", 6, null));
                    recvPacket.parse(line.toUpperCase());
                    //System.out.println(line.toUpperCase());
                    //System.out.println(recvPacket.getItem("HEAD").raw());
                    System.out.println(recvPacket.getItem("Data5").raw());
                } else {
                    //System.out.println(line.toUpperCase());

                    Packet recvPacket = new Packet();
                    recvPacket.addItem(Item.create("TAIL", 6, null));
                    recvPacket.addItem(Item.create("Data1", 16, null));
                    recvPacket.addItem(Item.create("Data2", 16, null));
                    recvPacket.addItem(Item.create("Data3", 20, null));
                    recvPacket.addItem(Item.create("Data4", 16, null));
                    recvPacket.addItem(Item.create("Data5", 16, null));
                    recvPacket.addItem(Item.create("Data6", 20, null));
                    recvPacket.parse(line.toUpperCase());

                    //System.out.println(recvPacket.getItem("TAIL").raw());
                    System.out.println(String.format("[%s] : [%s]", recvPacket.getItem("Data3").raw().length(), recvPacket.getItem("Data3").raw()));
                    System.out.println(String.format("[%s] : [%s]", recvPacket.getItem("Data6").raw().length(), recvPacket.getItem("Data6").raw()));
                    System.out.println(line);
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
     * 으로 바이트 단위로 파일 읽기, 없 바이너리 파일을 읽는 같은 그림 목소리, 영상 등 파일.
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("으로 파일 내용을 읽은 바이트 단위로 한 번 읽어 한 바이트: ");
            // 한 번 읽어 한 바이트
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
            System.out.println("으로 파일 내용을 읽은 바이트 단위로 한 번 읽어 여 개 바이트: ");
            // 한 번 읽어 여 개 바이트
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            FileUtils.showAvailableBytes(in);
            // 리드 인 여 개 바이트 까지 바이트 배열 중 byteread 위해 한 번 판독 하는 바이트
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
     * 으로 문자 단위로 파일 읽기, 없 읽다 텍스트, 숫자 등 형식 파일
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("으로 문자 단위로 파일 내용을 읽는 한 번 읽어 한 바이트: ");
            // 한 번 읽어 한 글자
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 에 대해 windows 아래 \r\n 이 두 글자 함께 있을 때, 표현 한 줄.
                // 하지만 만약 이 두 글자 따로 보이기 를 할 때 두 번 줄 바꾸다.
                // 그래서 차단 버리다 \r 또는 차단 \n.그렇지 않으면, 많이, 많이 빈 줄 것이다.
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("으로 문자 단위로 파일 내용을 읽는 한 번 읽어 여 개 바이트: ");
            // 한 번 더 읽은 글자
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 여러 개의 문자 판독 까지 문자 배열 중 charread 위해 한 번 읽은 글자 수
            while ((charread = reader.read(tempchars)) != -1) {
                // 같은 차단 버리다 \r 안 보이기
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
     * 으로 행동 단위 파일 읽기, 없 읽다 향하다 되는 서식 파일
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("으로 행동 단위 읽기 파일 내용을 읽는 모든 한 번 한 줄: ");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 한 번 판독 한 줄 판독 null 파일로 끝날 때까지
            while ((tempString = reader.readLine()) != null) {
                // 줄 번호 표시
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
     * 임의 파일 내용 읽기
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("임의 파일 내용을 읽는 동안: ");
            // 열 한 임의 접근 파일 읽기 전용 방식 대로 흐르다,
            randomFile = new RandomAccessFile(fileName, "r");
            // 파일 길이, 바이트
            long fileLength = randomFile.length();
            // 파일을 읽을 수 있는 시작 위치
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 파일 읽는 것이다 시작 위치 이동 beginIndex 위치.
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 한 번 읽어 10개 바이트, 만약 파일 내용 부족 10개 읽은 바이트 않으면 남은 바이트.
            // 는 한 번 읽은 바이트 주다 byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 디스플레이 입력 중 아직 하는 바이트
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("현재 입력 바이트 흐름 속에 바이트 위해:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}