package com.smilerlee.util.lcsv;
//package com.smilerlee.util.csv;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * The {@code StringArrayCSVParser} parses a CSV spreadsheet to a list of string
// * array.
// * 
// * @author smiler
// *
// */
//public class StringArrayCSVParser extends AbstractCSVParser<String[]> {
//
//	public StringArrayCSVParser() {
//		this(true);
//	}
//
//	public StringArrayCSVParser(boolean skipHeader) {
//		super(skipHeader);
//	}
//
//	@Override
//	protected List<String[]> parseContent(Reader in) throws IOException {
//		List<String[]> result = new ArrayList<String[]>();
//		List<String> row = new ArrayList<String>();
//		boolean wasNewLine = true;
//		while (true) {
//			String s = nextString(in);
//			if (isNewLine()) {
//				if (s == null && wasNewLine) {
//					// skip empty line
//				} else {
//					row.add(s);
//					result.add(row.toArray(new String[row.size()]));
//					row.clear();
//				}
//				wasNewLine = true;
//				if (isEof()) {
//					break;
//				}
//			} else {
//				row.add(s);
//				wasNewLine = false;
//			}
//		}
//		return result;
//	}
//
//}
