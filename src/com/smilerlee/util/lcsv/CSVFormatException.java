package com.smilerlee.util.lcsv;

/**
 * A {@code CSVFormatException} indicates there's a format error in the CSV
 * spreadsheet.
 * 
 * @author smiler
 *
 */
public class CSVFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CSVFormatException() {
		super();
	}

	public CSVFormatException(String s) {
		super(s);
	}

	public CSVFormatException(Throwable cause) {
		super(cause);
	}

	public CSVFormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
