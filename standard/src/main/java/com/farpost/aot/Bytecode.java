package com.farpost.aot;

public final class Bytecode {

	public static final byte endOfCompiledLine = 100;

	public static boolean isEndl(byte b) {
		return b == endOfCompiledLine;
	}
}