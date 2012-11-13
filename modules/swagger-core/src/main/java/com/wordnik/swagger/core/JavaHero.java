package com.wordnik.swagger.core;

import java.lang.reflect.Type;

public class JavaHero {
	public static Class doMagic(Type t) {
		System.out.println("type variable is " + t);
		return String.class;
	}
}
