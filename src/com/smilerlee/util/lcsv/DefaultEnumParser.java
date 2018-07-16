package com.smilerlee.util.lcsv;

/**
 * A default implementation of {@link EnumParser} which simply delegates to
 * {@link Enum#valueOf(Class, String)}.
 * 
 * @author smiler
 *
 */
public class DefaultEnumParser implements EnumParser {

	public <T extends Enum<T>> T parse(Class<T> enumType, String value) {
		return Enum.valueOf(enumType, value);
	}
	public static final DefaultEnumParser DEFAULT_PARSER = new DefaultEnumParser();
}
