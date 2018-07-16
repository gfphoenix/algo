package com.smilerlee.util.lcsv;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ColMap<T> {
	private final Class<T> type;
	private final ColMeta[] metaMap;

	public ColMap(BaseCSVParser<T> parser, Class<T> type) {
		Field[] fields = type.getDeclaredFields();
		ColMeta[] metaMap = new ColMeta[fields.length];
		for (Field field : fields) {
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				if (Modifier.isStatic(field.getModifiers())) {
					throw new IllegalArgumentException("Illegal column: static field: " + field.getName());
				}
				int index = columnIndex(columnAnnotation);
				if (index >= metaMap.length) {
					// rarely happened
					ColMeta[] newMetaMap = new ColMeta[Math.max(metaMap.length * 2, index + 8)];
					System.arraycopy(metaMap, 0, newMetaMap, 0, metaMap.length);
					metaMap = newMetaMap;
				} else if (metaMap[index] != null) {
					throw new IllegalArgumentException("Duplicate column: " + index);
				}
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				metaMap[index] = ColMeta.checkAndMake(parser, field);
			}
		}
		this.type = type;
		this.metaMap = metaMap;
	}

	public T newInstance() {
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	public ColMeta<T> getColMeta(int index){
		if(index>=metaMap.length)
			return null;
		return metaMap[index];
	}

//	public Class<?> getColumnType(int columnIndex) {
//		if (columnIndex >= metaMap.length) {
//			return null;
//		}
//		ColumnMeta meta = metaMap[columnIndex];
//		if (meta == null) {
//			return null;
//		}
//		return meta.cls;
//	}

//	public Class<?> getFieldType(int columnIndex) {
//		return metaMap[columnIndex].field.getType();
//	}

	public void setColumnValue(T object, int columnIndex, Object value) {
		Field field = metaMap[columnIndex].field;
		try {
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static int columnIndex(Column columnAnnotation) {
		int index = columnAnnotation.index();
		if(index>=0) return index;
		if(index!=-1){
			throw new IllegalArgumentException("index must be >=0");
		}
		String name = columnAnnotation.name();
		if(name.length()==0){
			throw new IllegalArgumentException("Neither column index nor name is specified");
		}
		index = 0;
		for (int i = 0, length = name.length(); i < length; i++) {
			char ch = name.charAt(i);
			if (ch < 'A' || ch > 'Z') {
				throw new IllegalArgumentException("Illegal column name: " + name);
			}
			index = index * 26 + (ch - 'A');
		}
		return index;
	}

}
