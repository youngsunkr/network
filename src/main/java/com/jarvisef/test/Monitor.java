package com.jarvisef.test;

import javax.management.monitor.MonitorSettingException;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by youngsunkr on 2015-07-20.
 */
public class Monitor {

    private static DatagramSocket isRun;

    public static void monitoring() throws MonitorSettingException {
        try {
            isRun = new DatagramSocket(9999);
        } catch (SocketException ex) {
            //throw new MonitorSettingException();
            System.exit(1);
        }
    }
    public static void close() {
        if (isRun != null) {
            isRun.close();
            System.out.println("Monitor.close()");
        }
    }
}
