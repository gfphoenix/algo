package com.smilerlee.util.lcsv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ObjectCSVParser<T> extends BaseCSVParser<T> {

	private static final EnumParser DEFAULT_ENUM_PARSER = new DefaultEnumParser();

	private final ColMap<T> columnMap;
	private final EnumParser enumParser;

	public ObjectCSVParser(Class<T> type) {
		this(type, true, DEFAULT_ENUM_PARSER);
	}

	public ObjectCSVParser(Class<T> type, boolean skipHeader) {
		this(type, skipHeader, DEFAULT_ENUM_PARSER);
	}

	public ObjectCSVParser(Class<T> type, EnumParser enumParser) {
		this(type, true, enumParser);
	}

	public ObjectCSVParser(Class<T> type, boolean skipHeader, EnumParser enumParser) {
		super(skipHeader);
		this.columnMap = new ColMap<T>(this, type);
		this.enumParser = enumParser;
	}
	@Override
	public EnumParser getEnumParser() {
		return enumParser;
	}
	boolean validLast(int ch){
		return ch==-1 || ch==',' || ch=='\r' || ch=='\n';
	}
//	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List<T> parseContent(Reader in) throws IOException {
		List<T> result = new ArrayList<T>();
		T obj = null;
		boolean wasNewLine = true;
		int columnIndex = 0;
		while (true) {
			ColMeta<T> cm = columnMap.getColMeta(columnIndex);
			Object value = null;
			if (cm == null) {
				skipColumn(in);
			} else {
				value = cm.toValue(in);
			}
			if (isNewLine()) {
				if (value == null && wasNewLine) {
					// skip empty line
				} else {
					if (obj == null) {
						obj = columnMap.newInstance();
					}
					if (value != null) {
						columnMap.setColumnValue(obj, columnIndex, value);
					}
					if(obj instanceof PostLoad){
						((PostLoad)obj).afterLoad();
					}
					result.add(obj);
					obj = null;
				}
				wasNewLine = true;
				columnIndex = 0;
				if (isEof()) {
					break;
				}
			} else {
				if (obj == null) {
					obj = columnMap.newInstance();
				}
				if (value != null) {
					cm.setValue(obj, value);
				}
				wasNewLine = false;
				columnIndex++;
			}
		}
		return result;
	}
}
