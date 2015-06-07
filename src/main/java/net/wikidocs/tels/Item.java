package net.wikidocs.tels;

/**
 * Created by youngsunkr on 2015-05-18.
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

    public String raw() {
        StringBuffer padded = new StringBuffer(this.value);
        while (padded.toString().getBytes().length < this.length) {
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

    public static void main(String[] args) {
        Item item = new Item();
        item.setName("ÀÌ¸§");
        item.setLength(10);
        item.setValue("È«±æµ¿");
        System.out.println("[" + item.raw() + "]");
    }
}
