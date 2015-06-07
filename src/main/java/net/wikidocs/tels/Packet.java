package net.wikidocs.tels;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by youngsunkr on 2015-05-18.
 */
public class Packet {
    private ArrayList<Item> items = new ArrayList<Item>();
    private HashMap<String, Item> nameAccess = new HashMap<String, Item>();

    public void addItem(Item item) {
        this.items.add(item);
        if(nameAccess.containsKey(item.getName())) {
            throw new RuntimeException(
                    "Duplicated item name:["+item.getName()+"]");
        }
        nameAccess.put(item.getName(), item);
    }

    public Item getItem(int index) {
        return this.items.get(index);
    }

    public Item getItem(String name) {
        return nameAccess.get(name);
    }

    public String raw() {
        StringBuffer result = new StringBuffer();
        for(Item item: items) {
            result.append(item.raw());
        }
        return result.toString();
    }

    public void parse(String data) {
        byte[] bdata = data.getBytes();
        int pos = 0;
        for(Item item: items) {
            byte[] temp = new byte[item.getLength()];
            System.arraycopy(bdata, pos, temp, 0, item.getLength());
            pos += item.getLength();
            item.setValue(new String(temp));
        }
    }

    public static void main(String[] args) {
        Packet packet = new Packet();
        Item item1 = Item.create("이름", 10, "홍길동");
        Item item2 = Item.create("전화번호", 11, "01099998888");
        packet.addItem(item1);
        packet.addItem(item2);
        System.out.println("["+packet.raw()+"]");

        Packet recvPacket = new Packet();
        recvPacket.addItem(Item.create("생일", 8, null));
        recvPacket.addItem(Item.create("주소", 30, null));
        recvPacket.parse("19801215서울시 송파구 잠실동 123-3    ");

        System.out.println(recvPacket.getItem(1).raw());
        System.out.println(recvPacket.getItem("주소").raw());
    }
}
