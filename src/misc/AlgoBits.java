package misc;

/**
 * Created by wuhao on 6/30/16.
 */
public class AlgoBits {
    public boolean is2power(int i) {
        return i>0 && (i&(i-1))==0;
    }
    public int bits(int i) {
        int c=0;
        while (i>0) {
            i &= i - 1;
            c++;
        }
        return c;
    }

    public int findBitIndex(int i){
        if(i==0) return  -1;
        int c=0;
        while ((i&1)==0){
            i>>=1;
            c++;
        }
        return c;
    }
    // 31, 30, ... 3, 2, 1, 0
    // assume that i is power of 2
    static final byte[]bits = {
            -1, 0, 1, -1,
            2, -1, -1, -1,
            3, -1,
    };
    public int bit1Index(int i) {
        int c=0;
        if((i&0xffff0000)!=0){
            c = 16;
            i = (i>>16)&0x0000ffff;
        }
        if((i&0xff00)!=0) {
            c += 8;
            i >>= 8;
        }
        if((i&0xf0)!=0) {
            c += 4;
            i >>=4;
        }
        return c + bits[i];
    }
}
