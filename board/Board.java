package misc.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by wuhao on 7/30/16.
 */
public class Board {
	// [0, n)
	static int random(int n) {
		return (int) (Math.random() * n);
	}

	static class Signature {
		Signature last;
		Vector2 self;
		int dep;

		Signature(Signature parent, Vector2 d) {
			this.last = parent;
			this.self = d;
			dep = parent == null ? 1 : parent.dep + 1;
		}

		Signature(Vector2 d) {
			this(null, d);
		}

		int depth() {
			return dep;
		}

		boolean cmp(Signature sig) {
			if (self != sig.self) // compare reference, since all self points
									// the common final constants
				return false;
			if (last == null)
				return sig.last == null;
			if (sig.last == null)
				return false;
			return last.cmp(sig.last);
		}
	}

	int[][] board;
	int digits;
	char[] tmp;

	Board(int row, int col) {
		board = initBoard(row, col);
		digits = getDigits(col);
		tmp = new char[digits];
		System.out.printf("board size = %d x %d - digits = %d\n", row, col,
				digits);
	}

	int getDigits(int N) {
		int d = 0;
		while (N > 0) {
			d++;
			N /= 10;
		}
		return d;
	}

	// | %2d | %2d |
	// ------------- n*5 +1
	void appendLine(StringBuilder sb, int w) {
		w = 1 + w * (3 + digits);
		for (int i = 0; i < w; i++) {
			sb.append('-');
		}
		sb.append('\n');
	}

	void appendNumber(StringBuilder sb, int n) {
		String fmt = "%" + digits + "d";
		String s = String.format(Locale.US, fmt, n);
		sb.append(s);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final int row = board.length;
		final int col = board[0].length;
		for (int r = 0; r < row; r++) {
			appendLine(sb, col);
			sb.append('|');
			for (int c = 0; c < col; c++) {
				sb.append(' ');
				appendNumber(sb, board[r][c]);
				sb.append(' ').append('|');
			}
			sb.append('\n');
		}
		appendLine(sb, col);
		return sb.toString();
	}

	int row() {
		return board.length;
	}

	int col() {
		return board[0].length;
	}

	public int[][] initBoard(int row, int col) {
		int[][] board = new int[row][col];
		int i = 0;
		for (int rr = 0; rr < row; rr++)
			for (int cc = 0; cc < col; cc++)
				board[rr][cc] = i++;
		return board;
	}

	public int at(int row, int col) {
		return board[row][col];
	}

	static void shuffle(int[] a) {
		for (int i = a.length; i > 0; i--) {
			int k = random(i);
			int tmp = a[k];
			a[k] = a[i - 1];
			a[i - 1] = tmp;
		}
	}

	public Vector2 randomShuffle() {
		int row = row();
		int col = col();
		int[] is = new int[row * col];
		for (int i = 0; i < is.length; i++) {
			is[i] = i;
		}
		shuffle(is);
		shuffle(is);
		int k = 0;
		int cx = -1, cy = -1;
		for (int x = 0; x < row; x++)
			for (int y = 0; y < col; y++) {
				int tmp = is[k++];
				board[x][y] = tmp;
				if (tmp == 0) {
					cx = x;
					cy = y;
				}
			}
		return new Vector2(cx, cy);
	}

	List<Signature> paths = new ArrayList<>();

	public Vector2 shuffle() {

		Vector2 current = new Vector2();
		Vector2 last = new Vector2();
		shuffle0(board, current, last, 100);
		System.out.println(current);
		return current;
	}

	// final Vector2 current = new Vector2();
	// final Vector2 last = new Vector2();
	static final Vector2[] dir = { new Vector2(0, 1), new Vector2(-1, 0),
			new Vector2(0, -1), new Vector2(1, 0), };
	final Vector2 tmpV2 = new Vector2();

	Vector2 shuffle(int[][] board, Vector2 current, Vector2 last, int dep,
			Signature parentPath) {

		int x = current.x, y = current.y;
		tmpV2.set(current);
		int row = row();
		int col = col();
		Vector2 d = null;
		while (true) {
			int xx = randomDir();
			d = dir[xx];
			Vector2.add(current, tmpV2, dir[xx]);
			if (current.x < 0 || current.x >= row || current.y < 0
					|| current.y >= col)
				continue;
			// if (!current.equals(last))
			// break;
			break;
		}
		boolean has = false;
		Signature path = new Signature(parentPath, d);
		for (Signature sig : paths) {
			if (sig.cmp(path)) {
				has = true;
				break;
			}
		}
		if (!has) {
			paths.add(path);
		}
		// swap
		int tmp = board[x][y];
		board[x][y] = board[current.x][current.y];
		board[current.x][current.y] = tmp;
		dep--;
		if (dep <= 0)
			return current;
		last.set(x, y);
		return shuffle(board, current, last, dep, path);
	}

	boolean validIndex(int x, int y) {
		return x >= 0 && y >= 0 && x < row() && y < col();
	}

	Vector2 shuffle0(int[][] board, Vector2 current, Vector2 tmpV2, int dep) {

		int x = current.x, y = current.y;
		tmpV2.set(current);
		while (true) {
			int xx = randomDir();
			Vector2 d = dir[xx];
			Vector2.add(current, tmpV2, d);
			if (validIndex(current.x, current.y))
				break;
		}

		// swap
		int tmp = board[x][y];
		board[x][y] = board[current.x][current.y];
		board[current.x][current.y] = tmp;
		dep--;
		if (dep <= 0)
			return current;
		return shuffle0(board, current, tmpV2, dep);
	}

	static int randomDir() {
		int i = (int) (Math.random() * 4);
		return i;
	}

	boolean inBound(int x, int y) {
		return x >= 0 && x < row() && y >= 0 && y < col();
	}

	boolean inBound(Vector2 v) {
		return inBound(v.x, v.y);
	}

	boolean isComplete() {
		int row = row();
		int col = col();
		int i = 0;
		for (int r = 0; r < row; r++)
			for (int c = 0; c < col; c++)
				if (board[r][c] != i++)
					return false;
		return true;
	}

	static void usage() {
		System.out.println("WASD to move");
	}

	// @Test
	public static void main(String[] args) {
		Board b = new Board(4, 4);
		System.out.println(b);
		Vector2 v = b.shuffle();
		System.out.println(b);
		Scanner scanner = new Scanner(System.in);
		usage();
		int row = v.x;
		int col = v.y;
		int xx = row, yy = col;
		while (scanner.hasNext()) {
			String s = scanner.next();
			if (s.length() != 1) {
				usage();
				continue;
			}
			switch (s.charAt(0)) {
			case 'w':
			case 'W':
				xx = row - 1;
				break;
			case 'a':
			case 'A':
				yy = col - 1;
				break;
			case 's':
			case 'S':
				xx = row + 1;
				break;
			case 'd':
			case 'D':
				yy = col + 1;
				break;
			default:
				usage();
				continue;
			}
			if (!b.inBound(xx, yy)) {
				xx = row;
				yy = col;
				System.out.println("out of bounds");
				System.out.println(b);
				continue;
			}
			int tmp = b.board[row][col];
			b.board[row][col] = b.board[xx][yy];
			b.board[xx][yy] = tmp;
			row = xx;
			col = yy;
			System.out.println(b);

			if (b.isComplete()) {
				System.out.println("************************");
				System.out.println("Success !!!");
				System.out.println("************************");
				break;
			}
		}

	}

}
