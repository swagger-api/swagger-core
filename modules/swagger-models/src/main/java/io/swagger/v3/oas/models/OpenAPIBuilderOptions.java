package io.swagger.v3.oas.models;

public class OpenAPIBuilderOptions {
	// THUAN - Configurations - Miscellaneous
	public static boolean USE_FULLNAME = false; // include package name in schemas
	public static boolean USE_ENUMNAME = false; // use Enum's name() instead of toString()
	public static boolean OMIT_GENERIC = false; // remove generic part in schemas' name
	public static boolean RECYCLE_ENUM = false; // make enums reusable
	public static boolean HIDE_PARENTS = false; // hide all properties from ancestors provided in allOf
}
