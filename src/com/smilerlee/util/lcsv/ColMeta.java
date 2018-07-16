package com.smilerlee.util.lcsv;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

abstract class ColMeta<T> {
	final Field field;
	final Class<?> cls;
	BaseCSVParser<T> parser;

	ColMeta(BaseCSVParser<T> parser, Field field) {
		this.parser = parser;
		this.field = field;
		this.cls = field.getType();
	}
	abstract Object toValue(Reader in) throws IOException;
	
	static <T> ColMeta<T> checkAndMake(BaseCSVParser<T> parser, Field field){
		Class<?> cls = field.getType();
		if(cls==int.class || cls==Integer.class){
			return new ColMeta<T>(parser, field){
				@Override
				Object toValue(Reader in) throws IOException {
					return parser.nextInt(in);
				}
			};
		}else if(cls==long.class || cls==Long.class){
			return new ColMeta<T>(parser, field){
				@Override
				Object toValue(Reader in) throws IOException {
					return parser.nextLong(in);
				}
			};
		}else if(cls==boolean.class || cls==Boolean.class){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextBoolean(in);
				}
			};
		}else if(cls==String.class){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextString(in);
				}
			};
		}else if(cls.isEnum()){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextEnum(in, (Class<? extends Enum>)field.getType());
				}
			};
		}else if(!cls.isArray()){
			throw new Error("unsupported field type");
		}
		cls = cls.getComponentType();
		if(cls==int.class || cls==Integer.class){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextList_int(in, cls);
				}
			};
		}else if(cls==long.class || cls==Long.class){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextList_long(in, cls);
				}
			};
		}else if(cls==boolean.class || cls==Boolean.class){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextList_bool(in, cls);
				}
			};
		}else if(cls==String.class){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextList_string(in);
				}
			};
		}else if(cls.isEnum()){
			return new ColMeta<T>(parser, field){
				Object toValue(Reader in) throws IOException{
					return parser.nextList_enum(in, cls);
				}
			};
		}else{
			throw new Error("unsupported field type");
		}
	}
	
	void setValue(Object obj, Object value){
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
//	
//	static class ColMeta_bool<T> extends ColMeta<T> {
//		ColMeta_bool(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextBoolean(in);
//		}
//	}
//	static class ColMeta_int<T> extends ColMeta<T> {
//		ColMeta_int(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextInt(in);
//		}
//	}
//	static class ColMeta_long<T> extends ColMeta<T> {
//		ColMeta_long(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextLong(in);
//		}
//	}
//	static class ColMeta_string<T> extends ColMeta<T> {
//		ColMeta_string(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextString(in);
//		}
//	}
//	static class ColMeta_enum<T> extends ColMeta<T> {
//		ColMeta_enum(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			Object value = null;
//			String s = parser.nextString(in);
//			if (s != null) {
//				Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
//				value = DefaultEnumParser.DEFAULT_PARSER.parse(enumType, s);
//			}
//			return value;
//		}
//	}
//	static class ColMeta_list_int<T> extends ColMeta<T> {
//		ColMeta_list_int(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextList_int(in, cls);
//		}
//	}
//	static class ColMeta_list_long<T> extends ColMeta<T> {
//		ColMeta_list_long(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextList_long(in, cls);
//		}
//	}
//	static class ColMeta_list_bool<T> extends ColMeta<T> {
//		ColMeta_list_bool(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextList_bool(in, cls);
//		}
//	}
//	static class ColMeta_list_string<T> extends ColMeta<T> {
//		ColMeta_list_string(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextList_string(in);
//		}
//	}
//	static class ColMeta_list_enum<T> extends ColMeta<T> {
//		ColMeta_list_enum(BaseCSVParser<T> parser, Field field) {
//			super(parser, field);
//		}
//		@Override
//		Object toValue(Reader in) throws IOException{
//			return parser.nextList_enum(in, cls);
//		}
//	}
}
