package com.fphoenix.platform;

import com.badlogic.gdx.utils.Array;
import com.fphoenix.common.Config;
import com.fphoenix.common.GridHelper;
import com.fphoenix.xutils.XBundleData;
import com.smilerlee.util.lcsv.Column;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by alan on 18-4-11.
 */

public class DesktopTest {
    public static class LevelData {
        @Column(name = "B")
        int[] stars;
        @Column(name = "C")
        String[] target_names;
        @Column(name = "D")
        int[] target_values;
        @Column(name = "E")
        String[] enemies;
        @Column(name = "F")
        String _int_map;
        @Column(name = "G")
        String _str_map;

        Map<String, Integer> int_map;
        Map<String, String> str_map;

        public LevelData init() {
            if (target_names == null || target_values == null || target_names.length != target_values.length)
                throw new IllegalArgumentException("illegal targets");
            int_map = XBundleData.parse_int_map(null, _int_map);
            str_map = XBundleData.parse_str_map(null, _str_map);
            return this;
        }

        public int n_stars() {
            return stars.length;
        }

        public int star(int idx) {
            return stars[idx];
        }

        public int n_targets() {
            return target_names.length;
        }

        public String target_name(int idx) {
            return target_names[idx];
        }

        public int target_value(int idx) {
            return target_values[idx];
        }

        public int get(String key, int default_int) {
            if (int_map == null) return default_int;
            Integer i = int_map.get(key);
            return i == null ? default_int : i;
        }

        public String get(String key, String default_string) {
            if (str_map == null) return default_string;
            String s = str_map.get(key);
            return s == null ? default_string : s;
        }
    }

    public static class Foo {
        @Column(name = "A")
        int id;
        @Column(name = "B")
        int[] il;
        @Column(name = "C")
        String name;
        @Column(name = "D")
        String[] names;
        @Column(name = "E")
        String intValues;
        @Column(name = "F")
        String strValues;

        Map<String, String> str_map;
        Map<String, Integer> int_map;

        void init() {
            str_map = XBundleData.parse_str_map(null, strValues);
            int_map = XBundleData.parse_int_map(null, intValues);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("id=").append(id)
                    .append(", name='")
                    .append(name)
                    .append("', il=");
            if (il == null) sb.append("null, ");
            else {
                sb.append("[ ");
                if (il.length > 0) sb.append(il[0]);
                for (int i = 1; i < il.length; i++) {
                    sb.append(", ").append(il[i]);
                }
                sb.append(" ]");
            }
            sb.append(", names=");

            if (names == null) sb.append("null, ");
            else {
                sb.append("[ ");
                if (names.length > 0) sb.append("'").append(names[0]).append("'");
                for (int i = 1; i < names.length; i++) {
                    sb.append(", '").append(names[i]).append("'");
                }
                sb.append(" ]");
            }
            sb.append(", intmap=");
            if (int_map == null) sb.append("null, ");
            else {
                sb.append("{ ");
                for (String key : int_map.keySet()) {
                    sb.append("'").append(key).append("':").append(int_map.get(key)).append(", ");
                }
                sb.append(" }");
            }
            sb.append(", strmap=");
            if (str_map == null) sb.append("null, ");
            else {
                sb.append("{ ");
                for (String key : str_map.keySet()) {
                    sb.append("'").append(key).append("':'").append(str_map.get(key)).append("', ");
                }
                sb.append(" }");
            }
            return sb.toString();
        }
    }

    private static void dump_xy(GridHelper gridHelper) {
        int row = gridHelper.getRow();
        int col = gridHelper.getCol();
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                sb.append((int) gridHelper.xAt(c)).append(", ").append((int) gridHelper.yAt(r)).append(", ");
            }
        }
        sb.append("} ");
        System.out.println(sb);
    }

}
