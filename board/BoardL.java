package misc.board;

import java.util.Locale;
import java.util.Map;

/**
 * Created by wuhao on 7/31/16.
 */
public class BoardL implements IBoard {
    int []values;
    int R, C;

    BoardL(int row, int col) {
        R = row;
        C = col;
        values = new int[row * col];
    }
    public BoardL initBoard() {
        for (int i=0; i<values.length; i++)
            values[i] = i;
        return this;
    }

    @Override
    public int row() {
        return R;
    }

    @Override
    public int col() {
        return C;
    }

    @Override
    public int at(int idx) {
        return values[idx];
    }

    @Override
    public IBoard copy() {
        BoardL b = new BoardL(R, C);
        b.R = R;
        b.C = C;
        System.arraycopy(values, 0, b.values, 0, values.length);
        return b;
    }

    @Override
    public int number() {
        return values.length;
    }

    @Override
    public void swap(int idx1, int idx2) {
        int tmp = values[idx1];
        values[idx1] = values[idx2];
        values[idx2] = tmp;
    }

    public static void main(String []args) {
        BoardL b = new BoardL(5,3).initBoard();
//        Map<IBoard, Signature> map = BoardUtils.generateBoard(b);
//        System.out.println(map.size());
        System.out.println(b);
        BoardUtils.shuffle(b, 100);
        BoardUtils.play(b);
    }

    // | %2d | %2d |
    // ------------- n*5 +1
    void appendLine(StringBuilder sb, int w) {
        w = 1 + w * (3 + 1);
        for (int i = 0; i < w; i++) {
            sb.append('-');
        }
        sb.append('\n');
    }

    void appendNumber(StringBuilder sb, int n) {
        String fmt = "%" + 1 + "d";
        String s = String.format(Locale.US, fmt, n);
        sb.append(s);
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        final int row = row();
//        final int col = col();
//        for (int r = 0; r < row; r++) {
//            appendLine(sb, col);
//            sb.append('|');
//            for (int c = 0; c < col; c++) {
//                sb.append(' ');
//                appendNumber(sb, at(r, c));
//                sb.append(' ').append('|');
//            }
//            sb.append('\n');
//        }
//        appendLine(sb, col);
//        return sb.toString();
        return BoardUtils.toString(this, new StringBuilder()).toString();
    }


}
