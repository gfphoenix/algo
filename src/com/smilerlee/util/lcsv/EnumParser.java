package com.smilerlee.util.lcsv;

/**
 * The EnumParser parses a string to an enum.
 * 
 * @author smiler
 *
 */
public interface EnumParser {

	public <T extends Enum<T>> T parse(Class<T> enumType, String value);

}
