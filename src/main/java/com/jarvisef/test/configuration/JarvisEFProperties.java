//package com.jarvisef.core.configuration;

package com.jarvisef.test.configuration;

import org.apache.commons.configuration.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by youngsunkr on 2015-10-26.
 */
public class JarvisEFProperties {
    private static Configuration configuration;

    public void setConfiguration(Configuration conf) {
        configuration = conf;
    }

    public static Configuration subset(String prefix) {
        return configuration.subset(prefix);
    }

    public static boolean isEmpty() {
        return configuration.isEmpty();
    }

    public static boolean containsKey(String key) {
        return configuration.containsKey(key);
    }

    public static void addProperty(String key, Object value) {
        configuration.addProperty(key, value);
    }

    public static void setProperty(String key, Object value) {
        configuration.setProperty(key, value);
    }

    public static void clearProperty(String key) {
        configuration.clearProperty(key);
    }

    public static void clear() {
        configuration.clear();
    }

    public static Object getProperty(String key) {
        return configuration.getProperty(key);
    }

    public static Iterator<String> getKeys(String prefix) {
        return configuration.getKeys(prefix);
    }

    public static Iterator<String> getKeys() {
        return configuration.getKeys();
    }

    public static Properties getProperties(String key) {
        return configuration.getProperties(key);
    }

    public static boolean getBoolean(String key) {
        return configuration.getBoolean(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return configuration.getBoolean(key, defaultValue);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return configuration.getBoolean(key, defaultValue);
    }

    public static byte getByte(String key) {
        return configuration.getByte(key);
    }

    public static byte getByte(String key, byte defaultValue) {
        return configuration.getByte(key, defaultValue);
    }

    public static Byte getByte(String key, Byte defaultValue) {
        return configuration.getByte(key, defaultValue);
    }

    public static double getDouble(String key) {
        return configuration.getDouble(key);
    }

    public static double getDouble(String key, double defaultValue) {
        return configuration.getDouble(key, defaultValue);
    }

    public static Double getDouble(String key, Double defaultValue) {
        return configuration.getDouble(key, defaultValue);
    }

    public static float getFloat(String key) {
        return configuration.getFloat(key);
    }

    public static float getFloat(String key, float defaultValue) {
        return configuration.getFloat(key, defaultValue);
    }

    public static Float getFloat(String key, Float defaultValue) {
        return configuration.getFloat(key, defaultValue);
    }

    public static int getInt(String key) {
        return configuration.getInt(key);
    }

    public static int getInt(String key, int defaultValue) {
        return configuration.getInt(key, defaultValue);
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        return configuration.getInteger(key, defaultValue);
    }

    public static long getLong(String key) {
        return configuration.getLong(key);
    }

    public static long getLong(String key, long defaultValue) {
        return configuration.getLong(key, defaultValue);
    }

    public static Long getLong(String key, Long defaultValue) {
        return configuration.getLong(key, defaultValue);
    }

    public static short getShort(String key) {
        return configuration.getShort(key);
    }

    public static short getShort(String key, short defaultValue) {
        return configuration.getShort(key, defaultValue);
    }

    public static Short getShort(String key, Short defaultValue) {
        return configuration.getShort(key, defaultValue);
    }

    public static BigDecimal getBigDecimal(String key) {
        return configuration.getBigDecimal(key);
    }

    public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return configuration.getBigDecimal(key, defaultValue);
    }

    public static BigInteger getBigInteger(String key) {
        return configuration.getBigInteger(key);
    }

    public static BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return configuration.getBigInteger(key, defaultValue);
    }

    public static String getString(String key) {
        return configuration.getString(key);
    }

    public static String getString(String key, String defaultValue) {
        return configuration.getString(key, defaultValue);
    }

    public static String[] getStringArray(String key) {
        return configuration.getStringArray(key);
    }

    public static List<Object> getList(String key) {
        return configuration.getList(key);
    }

    public static List<String> getstringList(String key) {
        return Arrays.asList(configuration.getStringArray(key));
    }

    public static List<Object> getList(String key, List<Object> defaultValue) {
        return configuration.getList(key, defaultValue);
    }
}
