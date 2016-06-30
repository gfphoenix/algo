import org.junit.Test;

/**
 * Created by wuhao on 6/30/16.
 */
public class ExcelName {
    boolean all(String s, int offset, char ch){
        for(int n=s.length(); offset<n; offset++)
            if(s.charAt(offset) != ch)
                return false;
        return true;
    }
    boolean less1(String small, String big){
        int n = small.length();
        int m = big.length();
        if(n!=m){
            return n+1==m && all(small, 0, 'Z') && all(big, 0, 'A');
        }
        for (int i=0; i<n; i++){
            int a = small.charAt(i);
            int b = big.charAt(i);
            if(a!=b){
                return a+1==b && all(small, i+1, 'Z') && all(big, i+1, 'A');
            }
        }
        return false;
    }

    boolean validName(CharSequence cs) {
        int n = cs.length();
        for (int i=0; i<n; i++) {
            int c = cs.charAt(i);
            if(c<'A' || c>'Z')
                return false;
        }
        return n>0;
    }
    int A2i(String name){
        int index = 0;
        for (int i = 0, length = name.length(); i < length; i++) {
            char ch = name.charAt(i);
            if (ch < 'A' || ch > 'Z') {
                throw new IllegalArgumentException("Illegal column name: " + name);
            }
            index = index * 26 + (ch - 'A');
        }
        return index;
    }
    StringBuilder next(StringBuilder sb){
        int n = sb.length();
        for(int i=n-1; i>=0; i--) {
            int ch = sb.charAt(i);
            if(ch<'Z') {
                sb.setCharAt(i, (char)(ch+1));
                for(int k=i+1; k<n; k++)
                    sb.setCharAt(k, 'A');
                return sb;
            }
            // ch is 'Z'
        }
        // all 'Z'
        for(int i=0; i<n; i++)
            sb.setCharAt(i, 'A');
        sb.append('A');
        return sb;
    }
    @Test
    public void printA(){
        StringBuilder sb = new StringBuilder().append('A');

        for (int i=0; i<10000; i++) {
            System.out.println(""+i+" => "+ sb.toString());
            sb = next(sb);
        }
    }
    String i2A1(StringBuilder sb, int i) {
        int d=1;
        for(int x=i; x>=26; x=x/26-1)
            d++;
        sb.setLength(d);
        for (; i>=26; i=i/26-1) {
            sb.setCharAt(--d, (char)('A'+(i%26)));
        }
        sb.setCharAt(0, (char)('A'+i));
        return sb.toString();
    }
    String i2A0(StringBuilder sb, int i) {
        sb.setLength(0);
        while(i>=26){
            int mod = i%26;
            sb.append((char)('A'+mod));
            i = i/26 -1;
        }
        sb.append((char)('A'+i));
        for(int j=0,k=sb.length()-1; j<k; j++, k--){
            char c1 = sb.charAt(j);
            char c2 = sb.charAt(k);
            sb.setCharAt(j, c2);
            sb.setCharAt(k, c1);
        }
        return sb.toString();
    }
    String i2A(StringBuilder sb, int i){
        String s = "";
        while (i>=26) {
            int mod = i%26;
            s = ""+(char)('A'+mod) + s;
            i = i/26 -1;
        }
        s = ""+(char)('A'+i) + s;
        return  s;
    }

    @Test
    public void testLess1(){
        StringBuilder sb = new StringBuilder();
        long t0 = System.currentTimeMillis();
        String a = i2A0(sb, 0);
        int error=0;
        for(int i=1; i<100000000; i++){
            String b = i2A0(sb, i);
            boolean ok = less1(a, b);
            if(!ok){
                error++;
                System.out.printf("small = %d %s   big = %d %s\n",(i-1), a, i, b);
                if(error==8)
                    break;
            }
            a = b;
        }
        long t1 = System.currentTimeMillis();
        System.out.println("time = "+(t1-t0)+"ms");
    }
}
