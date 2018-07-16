package misc;

/**
 * Created by wuhao on 7/1/16.
 */
public class Bsearch {
    interface List<T> {
        int size();
        T at(int idx);
    }
    public static List<Byte> wrap(final byte[]data) {
        return new List<Byte>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public Byte at(int idx) {
                return data[idx];
            }
        };
    }
    public static List<Character> wrap(final char[]data) {
        return new List<Character>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public Character at(int idx) {
                return data[idx];
            }
        };
    }
    public static List<Short> wrap(final short[]data) {
        return new List<Short>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public Short at(int idx) {
                return data[idx];
            }
        };
    }
    public static List<Integer> wrap(final int []data){
        return new List<Integer>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public Integer at(int idx) {
                return data[idx];
            }
        };
    }
    public static List<Long> wrap(final long[]data) {
        return new List<Long>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public Long at(int idx) {
                return data[idx];
            }
        };
    }
    public static List<Float> wrap(final float[]data) {
        return new List<Float>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public Float at(int idx) {
                return data[idx];
            }
        };
    }
    public static List<Double> wrap(final double[]data) {
        return new List<Double>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public Double at(int idx) {
                return data[idx];
            }
        };
    }
    public static List<String> wrap(final String[]data) {
        return new List<String>() {
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public String at(int idx) {
                return data[idx];
            }
        };
    }

    public static <T> int b_min(List<Comparable<T>> data, T key) {
        int len = data.size();
        if(len==0 || data.at(len-1).compareTo(key) <= 0)
            return -1;
        int l=0, r=len-1;
        while (l <= r) {
            int m = l + (r-l)/2;
            int rc = data.at(m).compareTo(key);
            if(rc<=0) {
                l = m+1;
            }else {
                r = m-1;
            }
        }
        return l;
    }
    public static <T> int be_min(List<Comparable<T>> data, T key) {
        int len = data.size();
        if(len==0 || data.at(len-1).compareTo(key) <0)
            return -1;
        int l = 0, r = len - 1;
        int m, rc;
        while (l <= r) {
            m = l + (r-l) / 2;
            rc = data.at(m).compareTo(key);
            if(rc < 0) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return l;
    }
    public static <T> int l_max(List<Comparable<T>> data, T key) {
        int len = data.size();
        if(len==0 || data.at(0).compareTo(key)>=0)
            return -1;
        int l = 0, r = len - 1;
        int m, rc;
        while (l <= r) {
            m = l + (r - l) / 2;
            rc = data.at(m).compareTo(key);
            if (rc >= 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return r;
    }
    public static <T> int le_max(List<Comparable<T>> data, T key) {
        int len = data.size();
        if(len==0 || data.at(0).compareTo(key) > 0)
            return -1;
        int l = 0, r = len - 1;
        int m, rc;
        while (l <= r) {
            m = l + (r - l) / 2;
            rc = data.at(m).compareTo(key);
            if (rc > 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return r;
    }
    public static <T> int bsearch(List<Comparable<T>> data, T key) {
        int l=0, r=data.size()-1;
        int m, rc;
        while (l<=r) {
            m = l + (r - l) / 2;
            rc = data.at(m).compareTo(key);
            if(rc==0) return m;
            if(rc<0) {
                l = m+1;
            }else {
                r = m-1;
            }
        }
        return -1;
    }


    // min ({ idx | data[idx] > key })
    public static int b_min(float[] data, float key) {
        int len = data.length;
        if (len==0 || data[len - 1] <= key)
            return -1;
        int l = 0, r = len - 1;
        while (l <= r) {
            int m = (r + l) / 2;
            if (data[m] <= key) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return l;
    }
    // min ({ idx | data[idx] >= key })
    public static int be_min(float[] data, float key) {
        int len = data.length;
        if (len==0 || data[len - 1] < key)
            return -1;
        int l = 0, r = len - 1;
        while (l <= r) {
            int m = (r + l) / 2;
            if (data[m] < key) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return l;
    }
    // max ( { idx | data[idx] < key } )
    public static int l_max(float[] data, float key) {
        int len = data.length;
        if (len==0 || data[0] >= key)
            return -1;
        int l = 0, r = len - 1;
        while (l <= r) {
            int m = (r + l) / 2;
            if (data[m] >= key) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return r;
    }
    // max({ idx | data[idx]<=key })
    public static int le_max(float[] data, float key) {
        int len = data.length;
        if (len==0 || data[0] > key)
            return -1;
        int l = 0, r = len - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (data[m] > key) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return r;
    }
    public static int bsearch(float []data, float key) {
        int l = 0, r = data.length - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            if(data[m] == key) return m;
            if (data[m] > key) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return -1;
    }
}
