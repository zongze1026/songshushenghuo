package com.yitkeji.songshushenghuo.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 邀请码生成器，算法原理：<br/>
 * 1) 获取id: 1127738 <br/>
 * 2) 使用自定义进制转为：gpm6 <br/>
 * 3) 转为字符串，并在后面加'o'字符：gpm6o <br/>
 * 4）在后面随机产生若干个随机数字字符：gpm6o7 <br/>
 * 转为自定义进制后就不会出现o这个字符，然后在后面加个'o'，这样就能确定唯一性。最后在后面产生一些随机字符进行补全。<br/>
 * @author jiayu.qiu
 */
public class ShareCodeUtil {

    /** 自定义进制(0,1没有加入,容易与o,l混淆) */
    private static final char[] R=new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};

    /** (不能与自定义进制有重复) */
    private static final char B='o';

    /** 进制长度 */
    private static final int BIN_LEN =R.length;

    /** 序列最小长度 */
    private static final int S=8;

    /**
     * 根据ID生成六位随机码
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        char[] buf=new char[32];
        int charPos=32;

        while((id / BIN_LEN) > 0) {
            int ind=(int)(id % BIN_LEN);
            buf[--charPos]=R[ind];
            id /= BIN_LEN;
        }
        buf[--charPos]=R[(int)(id % BIN_LEN)];
        String str=new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if(str.length() < S) {
            StringBuilder sb=new StringBuilder();
            sb.append(B);
            Random rnd=new Random();
            for(int i=1; i < S - str.length(); i++) {
                sb.append(R[rnd.nextInt(BIN_LEN)]);
            }
            str+=sb.toString();
        }
        return str;
    }

    public static long codeToId(String code) {
        char chs[]=code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j = 0; j < BIN_LEN; j++) {
                if(chs[i] == R[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == B) {
                break;
            }
            if(i > 0) {
                res=res * BIN_LEN + ind;
            } else {
                res=ind;
            }
        }
        return res;
    }

    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

    /**
     * 返回手机号码
     */
    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    private static String getTel() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }

    public static void main(String[] args){
        Set hs = new HashSet();
        for (int i=1;i<100;i++){
            Integer number= 0;
            String phone=getTel();
            try {
                number = Integer.valueOf(phone).intValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            String str=toSerialCode(number);
            try {
                hs.add(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**-------------------------------------另外一种生成机制------------------------------------------------------*/
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9" };


    public static String getPassword() {
        Random rd = new Random(); // 创建随机对象
        String n = ""; // 保存随机数
        int rdGet; // 取得随机数
        do {
            if (rd.nextInt() % 2 == 1) {
                rdGet = Math.abs(rd.nextInt()) % 10 + 48; // 产生48到57的随机数(0-9的键位值)
            } else {
                rdGet = Math.abs(rd.nextInt()) % 26 + 97; // 产生97到122的随机数(a-z的键位值)
            }
            char num1 = (char) rdGet; // int转换char
            String dd = Character.toString(num1);
            n += dd;
        } while (n.length() < 8);// 设定长度，此处假定长度小于8
        return n;

    }

    /**
     * 根据ID生成八位随机码
     * @param id ID
     * @return 随机码
     */
    public static String toRandomSerialCode(long id) {

        String str = "";
        int general = 8;
        int length = general-String.valueOf(id).length();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
            str += (char) (97 + random.nextInt(26));// 取得大写字母
        }
        return str+String.valueOf(id);
    }
}