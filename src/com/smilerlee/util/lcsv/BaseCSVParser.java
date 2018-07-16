package com.smilerlee.util.lcsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseCSVParser<T> implements CSVParser<T>{
	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private final boolean skipHeader;
	private StringBuilder stringBuilder;
	private List<String> list_string = new ArrayList<String>();
	private List<Integer> list_int = new ArrayList<Integer>();
	private List<Long> list_long = new ArrayList<Long>();
	private List<Boolean> list_bool = new ArrayList<Boolean>();
	private int lastCh;

	public BaseCSVParser(boolean skipHeader) {
		this.skipHeader = skipHeader;
		stringBuilder = new StringBuilder(128);
	}
	
	public final List<T> parse(InputStream in) throws IOException {
		return parse(new InputStreamReader(in, UTF_8));
	}

	public final List<T> parse(Reader in) throws IOException {
		in = new BufferedReader(in);
		if (skipHeader) {
			skipHeader(in);
		}
		return parseContent(in);
	}
	protected int lastChar(){
		return lastCh;
	}
	private void skipHeader(Reader in) throws IOException {
		while(true){
			int ch = in.read();
			switch (ch) {
			case -1:
			case '\r':
			case '\n':
				lastCh = ch;
				return;
			}
		}
	}

	protected abstract List<T> parseContent(Reader in) throws IOException;

	protected final boolean isNewLine() {
		return lastCh=='\r' || lastCh=='\n' || lastCh==-1;
	}

	protected final boolean isEof() {
		return lastCh==-1;
	}
	public EnumParser getEnumParser(){
		return null;
	}
	final String nextString(Reader in) throws IOException {
//		lastCh=-1;
		boolean hasQuote = false;
		int ch = in.read();
		switch (ch) {
		case -1:
		case '\r':
		case '\n':
		case ',':
			lastCh = ch;
			return null;
		case '\"':
			hasQuote = true;
			ch = in.read();
			break;
		}
		StringBuilder sb = this.stringBuilder;
		sb.setLength(0);
		while (true) {
			switch (ch) {
			case -1:
			case '\r':
			case '\n':
				lastCh = ch;
				if (hasQuote) {
					throw unexpectedCharacter(ch, sb);
				} else {
					return sb.toString();
				}
			case ',':
				if (hasQuote) {
					sb.append(',');
				} else {
					lastCh = ',';
					return sb.toString();
				}
				break;
			case '\"':
				if (!hasQuote) {
					throw unexpectedCharacter('\"', sb);
				}
				lastCh = in.read();
				return sb.toString();
			case '\\': // escape
				ch = in.read();
				switch (ch) {
				case 't':
					sb.append('\t');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case '\"':
					sb.append('"');
					break;
				case '\\':
					sb.append('\\');
					break;
				default:
					throw unexpectedCharacter(ch, sb);
				}
				break;
			default:
				sb.append((char) ch);
				break;
			}
			ch = in.read();
		}
	}
	@SuppressWarnings("rawtypes")
	Enum nextEnum(Reader in, Class<? extends Enum> enumType) throws IOException{
		String s = nextString(in);
		if(s==null){
			return null;
		}
//		System.out.printf("enum S = <%s> len=%d\n", s, s.length());
		return getEnumParser().parse(enumType, s);
	}
	final Integer nextInt(Reader in) throws IOException {
//		lastCh = -1;
		int ch = in.read();
		boolean negative = false;
		int limit = -Integer.MAX_VALUE;
		
		if(ch=='+'){
			ch = in.read();
		}else if(ch=='-'){
			negative = true;
			limit = Integer.MIN_VALUE;
			ch = in.read();
		}else if(ch>='0' && ch<='9'){
			// empty
		}else{
			lastCh = ch;
			return null;
		}
		int limit10th = limit / 10;
		int value = 0;
		int count = 0;
		while (true) {
			if (ch >= '0' && ch <= '9') {
				if (value < limit10th) {
					throw overflowInt();
				}
				value *= 10;
				int digit = ch - '0';
				if (value < limit + digit) {
					throw overflowInt();
				}
				value -= digit;
				count++;
			} else {
				if (count == 0) {
					// only got '+' or '-'
					throw unexpectedCharacter(ch);
				}
				lastCh = ch;
				return negative ? value : -value;
			}
			ch = in.read();
		}
	}

	final Long nextLong(Reader in) throws IOException {
//		lastCh = -1;
		int ch = in.read();
		boolean negative = false;
		long limit = -Long.MAX_VALUE;
		
		if(ch=='+'){
			ch = in.read();
		}else if(ch=='-'){
			negative = true;
			limit = Long.MIN_VALUE;
			ch = in.read();
		}else if(ch>='0' && ch<='9'){
			// empty
		}else{
			lastCh = ch;
			return null;
		}
		long limit10th = limit / 10;
		long value = 0;
		int count = 0;
		while (true) {
			if (ch >= '0' && ch <= '9') {
				if (value < limit10th) {
					throw overflowLong();
				}
				value *= 10;
				int digit = ch - '0';
				if (value < limit + digit) {
					throw overflowLong();
				}
				value -= digit;
				count++;
			} else {
				if (count == 0) {
					// only got '+' or '-'
					throw unexpectedCharacter(ch);
				}
				lastCh = ch;
				return negative ? value : -value;
			}
			ch = in.read();
		}
	}

	final Boolean nextBoolean(Reader in) throws IOException {
		String s = nextString(in);
		if (s == null) {
			return null;
		} else if (s.equals("true")) {
			return Boolean.TRUE;
		} else if (s.equals("false")) {
			return Boolean.FALSE;
		} else {
			throw new CSVFormatException("Illegal boolean value: " + s);
		}
	}
	// fieldType must be int[].class or Integer[].class
	Object nextList_int(Reader in, Class<?> fieldType) throws IOException {
		Class<?> inner = fieldType.getComponentType();
//		lastCh = -1;
		int ch = in.read();
		switch(ch){
		case -1:
		case '\r':
		case '\n':
		case ',':
			lastCh = ch;
			return null;
		}
		if(ch!='['){
			throw unexpectedCharacter(ch);
		}
		list_int.clear();
		Integer i;
		boolean loop=true;
		while(loop){
			i = nextInt(in);
			if(i!=null)
				list_int.add(i);
			switch(lastCh){
			case ',':
				break;
			case ']':
				lastCh = in.read();
				loop = false;
				break;
			default:
				throw unexpectedCharacter(lastCh);
			}
		}
		if(inner==Integer.class){
			Integer []v = new Integer[list_int.size()];
			return list_int.toArray(v);
		}
		int []a = new int[list_int.size()];
		for(int j=0, n=a.length; j<n; j++){
			a[j] = list_int.get(j);
		}
		return a;
	}
	Object nextList_long(Reader in, Class<?> fieldType) throws IOException {
		Class<?> inner = fieldType.getComponentType();
//		lastCh = -1;
		int ch = in.read();
		switch(ch){
		case -1:
		case '\r':
		case '\n':
		case ',':
			lastCh = ch;
			return null;
		}
		if(ch!='['){
			throw unexpectedCharacter(ch);
		}
		list_long.clear();
		Long i;
		boolean loop=true;
		while(loop){
			i = nextLong(in);
			if(i!=null)
				list_long.add(i);
			switch(lastCh){
			case ',':
				break;
			case ']':
				lastCh = in.read();
				loop = false;
				break;
			default:
				throw unexpectedCharacter(lastCh);
			}
		}
		if(inner==Long.class){
			Long []v = new Long[list_long.size()];
			return list_long.toArray(v);
		}
		long []a = new long[list_long.size()];
		for(int j=0, n=a.length; j<n; j++){
			a[j] = list_long.get(j);
		}
		return a;
	}
	Object nextList_string(Reader in) throws IOException {
//		lastCh = -1;
		int ch = in.read();
		switch(ch){
		case -1:
		case '\r':
		case '\n':
		case ',':
			lastCh = ch;
			return null;
		}
		if(ch!='['){
			throw unexpectedCharacter(ch);
		}
		list_string.clear();
		String i;
		boolean loop=true;
		while(loop){
			i = nextString(in);
			if(i!=null) {
				if (i.equals("]")) {
					break;
				}else {
					list_string.add(i);
				}
			}
			switch(lastCh){
			case ',':
				break;
			case ']':
				lastCh = in.read();
				loop = false;
				break;
			default:
				throw unexpectedCharacter(lastCh);
			}
		}
		String []result = new String[list_string.size()];
		return list_string.toArray(result);
	}
	Object nextList_enum(Reader in, Class<?> fieldType) throws IOException {
//		lastCh = -1;
		int ch = in.read();
		switch(ch){
		case -1:
		case '\r':
		case '\n':
		case ',':
			lastCh = ch;
			return null;
		}
		if(ch!='['){
			throw unexpectedCharacter(ch);
		}
		list_string.clear();
		String i;
		boolean loop=true;
		while(loop){
			i = nextString(in);
			if(i!=null)
				list_string.add(i);
			switch(lastCh){
			case ',':
				break;
			case ']':
				lastCh = in.read();
				loop = false;
				break;
			default:
				throw unexpectedCharacter(lastCh);
			}
		}
		Enum<?> []en = new Enum<?>[list_string.size()];
		Class<? extends Enum> enumType = (Class<? extends Enum>)fieldType; 
		for(int j=0,n=en.length; j<n; j++){
			en[j] = getEnumParser().parse(enumType, list_string.get(j));
		}
		return en;
	}
	Object nextList_bool(Reader in, Class<?> fieldType) throws IOException {
		Class<?> inner = fieldType.getComponentType();
//		lastCh = -1;
		int ch = in.read();
		switch(ch){
		case -1:
		case '\r':
		case '\n':
		case ',':
			lastCh = ch;
			return null;
		}
		if(ch!='['){
			throw unexpectedCharacter(ch);
		}
		list_bool.clear();
		Boolean i;
		boolean loop=true;
		while(loop){
			i = nextBoolean(in);
			if(i!=null)
				list_bool.add(i);
			switch(lastCh){
			case ',':
				break;
			case ']':
				lastCh = in.read();
				loop = false;
				break;
			default:
				throw unexpectedCharacter(lastCh);
			}
		}
		if(inner==Boolean.class){
			Boolean []v = new Boolean[list_bool.size()];
			return list_bool.toArray(v);
		}
		boolean []a = new boolean[list_bool.size()];
		for(int j=0, n=a.length; j<n; j++){
			a[j] = list_bool.get(j);
		}
		return a;
	}
	// '[' has eaten
	void skipColumnList(Reader in) throws IOException {
		int ch = in.read();
		boolean b1;
		while(true){
			ch = in.read();
			switch(ch){
			case -1:
			case '\r':
			case '\n':
				throw unexpectedCharacter(ch);
			case '\"': // skip quoted string
				// eat until next quote
				b1 = true; // in quote
				while (b1) {
					ch = in.read();
					switch (ch) {
					case -1:
					case '\r':
					case '\n':
						throw unexpectedCharacter(ch);
					case '\"':
						b1 = false;
						break;
					case '\\':
						ch = in.read();
						switch (ch) {
						case '\\':
						case 'r':
						case 'n':
						case 't':
							break;
						default:
							throw unexpectedCharacter(ch);
						}
						break;
					}
				}
				break;
			case ']':
				lastCh = in.read();
				return ;
			}
		}
	}
	protected final void skipColumn(Reader in) throws IOException {
		int ch = in.read();
		switch (ch) {
		case -1:
		case '\r':
		case '\n':
		case ',':
			lastCh = ch;
			return;
		case '\"': // skip quoted string
			while (true) {
				ch = in.read();
				switch (ch) {
				case -1:
				case '\r':
				case '\n':
					throw unexpectedCharacter(ch);
				case '\"':
					ch = in.read();
					switch (ch) {
					case -1:
					case '\r':
					case '\n':
					case ',':
						lastCh = ch;
						return;
					case '\"':
						// "" escape
						break;
					default:
						throw unexpectedCharacter(ch);
					}
					break;
				}
			}
		case '[': // start of list
			skipColumnList(in);
			break;
		default: // skip other
			while (true) {
				ch = in.read();
				switch (ch) {
				case -1:
				case '\r':
				case '\n':
				case ',':
					lastCh = ch;
					return;
				}
			}
		}
	}

	protected static CSVFormatException unexpectedCharacter(int ch) {
		if (ch == -1) {
			return new CSVFormatException("Unexcepted character: (EOF)");
		} else {
			return new CSVFormatException("Unexcepted character: <" + (char) ch+"> "+ch);
		}
	}

	protected static CSVFormatException unexpectedCharacter(int ch, StringBuilder sb) {
		if (ch == -1) {
			return new CSVFormatException("Unexcepted character: (EOF), prevoius input: " + sb);
		} else {
			return new CSVFormatException("Unexcepted character: " + (char) ch + ", prevoius input: " + sb);
		}
	}

	private static CSVFormatException overflowInt() {
		return new CSVFormatException("Overflow int value");
	}

	private static CSVFormatException overflowLong() {
		return new CSVFormatException("Overflow long value");
	}

}
