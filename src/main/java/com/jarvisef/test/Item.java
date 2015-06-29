package com.jarvisef.test;

import java.io.UnsupportedEncodingException;

/**
 * Created by youngsunkr on 2015-06-01.
 */
public class Item {
    private String name;
    private int length;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String raw() throws UnsupportedEncodingException {
        StringBuffer padded = new StringBuffer(this.value);
        while (padded.toString().getBytes("EUC-KR").length < this.length) {
            padded.append(' ');
        }
        return padded.toString();
    }

    public static Item create(String name, int length, String value) {
        Item item = new Item();
        item.setName(name);
        item.setLength(length);
        item.setValue(value);
        return item;
    }

//    public static void main(String[] args) {
//        Item item = new Item();
//        item.setName("�̸�");
//        item.setLength(10);
//        item.setValue("ȫ�浿");
//        System.out.println("[" + item.raw() + "]");
//    }
}

