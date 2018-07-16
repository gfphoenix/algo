package com.smilerlee.util.lcsv;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * A {@code CSVParser} parses a CSV spreadsheet to a list of entry.
 * 
 * @author smiler
 *
 * @param <T>
 *            The type of result entry
 */
public interface CSVParser<T> {

	public List<T> parse(InputStream in) throws IOException;

	public List<T> parse(Reader in) throws IOException;

}
