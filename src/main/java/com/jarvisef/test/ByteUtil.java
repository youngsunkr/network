package com.jarvisef.test;

/**
 * Created by youngsunkr on 2015-05-20.
 * http://www.javaservice.net/~java/bbs/read.cgi?m=devtip&b=javatip&c=r_p&n=953537896
 */
/**
 * @(#) ByteUtil.java
 * Copyright 1999-2001 by  Java Service Network Community, KOREA.
 * All rights reserved.  http://www.javaservice.net
 *
 * NOTICE !      You can copy or redistribute this code freely,
 * but you should not remove the information about the copyright notice
 * and the author.
 *
 * @author  WonYoung Lee, javaservice@hanmail.net
 */
public class ByteUtil {
    private ByteUtil()
    {
    }

    public static final byte[] short2byte(short s)
    {
            byte dest[] = new byte[2];
        dest[1] = (byte)(s & 0xff);
        dest[0] = (byte)(s >>> 8 & 0xff);
        return dest;
    }

    public static final byte[] int2byte(int i)
    {
        byte dest[] = new byte[4];
        dest[3] = (byte)(i & 0xff);
        dest[2] = (byte)(i >>> 8 & 0xff);
        dest[1] = (byte)(i >>> 16 & 0xff);
        dest[0] = (byte)(i >>> 24 & 0xff);
        return dest;
    }

    public static final byte[] long2byte(long l)
    {
        byte dest[] = new byte[8];
        dest[7] = (byte)(int)(l & 255L);
        dest[6] = (byte)(int)(l >>> 8 & 255L);
        dest[5] = (byte)(int)(l >>> 16 & 255L);
        dest[4] = (byte)(int)(l >>> 24 & 255L);
        dest[3] = (byte)(int)(l >>> 32 & 255L);
        dest[2] = (byte)(int)(l >>> 40 & 255L);
        dest[1] = (byte)(int)(l >>> 48 & 255L);
        dest[0] = (byte)(int)(l >>> 56 & 255L);
        return dest;
    }

    public static final byte[] float2byte(float f)
    {
        byte dest[] = new byte[4];
        return setfloat(dest,0,f);
    }
    public static final byte[] double2byte(double d)
    {
        byte dest[] = new byte[8];
        return setdouble(dest,0,d);
    }

    public static final byte getbyte(byte src[], int offset)
    {
        return src[offset];
    }

    public static final byte[] getbytes(byte src[], int offset, int length)
    {
        byte dest[] = new byte[length];
        System.arraycopy(src, offset, dest, 0, length);
        return dest;
    }

    public static final short getshort(byte src[], int offset)
    {
        return (short)((src[offset] & 0xff) << 8 | src[offset + 1] & 0xff);
    }

    public static final int getint(byte src[], int offset)
    {
        return
                (src[offset] & 0xff) << 24 |
                        (src[offset + 1] & 0xff) << 16 |
                        (src[offset + 2] & 0xff) << 8 |
                        src[offset + 3] & 0xff;
    }

    public static final long getlong(byte src[], int offset)
    {
        return
                (long)getint(src, offset) << 32 |
                        (long)getint(src, offset + 4) & 0xffffffffL;
    }

    public static final float getfloat(byte src[], int offset)
    {
        return Float.intBitsToFloat(getint(src, offset));
    }

    public static final double getdouble(byte src[], int offset)
    {
        return Double.longBitsToDouble(getlong(src, offset));
    }

    public static final byte[] setbyte(byte dest[], int offset, byte b)
    {
        dest[offset] = b;
        return dest;
    }

    public static final byte[] setbytes(byte dest[], int offset, byte src[])
    {
        System.arraycopy(src, 0, dest, offset, src.length);
        return dest;
    }

    public static final byte[] setbytes(byte dest[], int offset, byte src[], int len)
    {
        System.arraycopy(src, 0, dest, offset, len);
        return dest;
    }

    public static final byte[] setshort(byte dest[], int offset, short s)
    {
        dest[offset] = (byte)(s >>> 8 & 0xff);
        dest[offset + 1] = (byte)(s & 0xff);
        return dest;
    }

    public static final byte[] setint(byte dest[], int offset, int i)
    {
        dest[offset] = (byte)(i >>> 24 & 0xff);
        dest[offset + 1] = (byte)(i >>> 16 & 0xff);
        dest[offset + 2] = (byte)(i >>> 8 & 0xff);
        dest[offset + 3] = (byte)(i & 0xff);
        return dest;
    }

    public static final byte[] setlong(byte dest[], int offset, long l)
    {
        setint(dest, offset, (int)(l >>> 32));
        setint(dest, offset + 4, (int)(l & 0xffffffffL));
        return dest;
    }

    public static final byte[] setfloat(byte dest[], int offset, float f)
    {
        return setint(dest, offset, Float.floatToIntBits(f));
    }

    public static final byte[] setdouble(byte dest[], int offset, double d)
    {
        return setlong(dest, offset, Double.doubleToLongBits(d));
    }
    //sjkim
    public static final byte[] string2byte(String s) {
        return string2byte(s,65535);
    }
    public static final byte[] string2byte(String s, int max) {
        if (s == null)
            return short2byte((short) 0);
        else {
            byte[] strBuf = s.getBytes();
            int len = strBuf.length;
            if (len == 0)
                return short2byte((short) 0);
            else if(len>max) len = max;
            else if (len > 65535) len = 65535;
            byte[] lenBuf = short2byte((short) len);

            byte[] rtnBuf = new byte[2 + len];

            System.arraycopy(lenBuf, 0, rtnBuf, 0, 2);
            System.arraycopy(strBuf, 0, rtnBuf, 2, len);
            return rtnBuf;
        }
    }

    public static final boolean isEquals(byte b[], String s)
    {
        if(b == null || s == null)
            return false;
        int slen = s.length();
        if(b.length != slen)
            return false;
        for(int i = slen; i-- > 0;)
            if(b[i] != s.charAt(i))
                return false;

        return true;
    }

    public static final boolean isEquals(byte a[], byte b[])
    {
        if(a == null || b == null)
            return false;
        if(a.length != b.length)
            return false;
        for(int i = a.length; i-- > 0;)
            if(a[i] != b[i])
                return false;

        return true;
    }

}
