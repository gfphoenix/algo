package com.smilerlee.util.lcsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Map a Java field to a CSV spreadsheet column.
 * 
 * @author smiler
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

	/** The 0-based index of the column */
	int index() default -1;

	/** The name of the column: A, B, C, ... Z, AA, AB, etc. */
	String name() default "";

}
