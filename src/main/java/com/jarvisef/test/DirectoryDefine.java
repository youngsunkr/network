package com.jarvisef.test;

/**
 * Created by youngsunkr on 2015-06-15.
 */
public class DirectoryDefine {

    public static final String base_Directory               = "d:/전문파일테스트/data/batch";

    public static final String RCV   	= "/RCV";

    public static final String RCV_BACKUP   	= "/RCV/backup";

    public static final String SND   	= "/SND";

    public static final String SND_BACKUP   	= "/SND/backup";

    public static String CCO_DirPath(String cco_c, String savePath) {
        return base_Directory.concat(cco_c.concat(savePath));
    }
}



