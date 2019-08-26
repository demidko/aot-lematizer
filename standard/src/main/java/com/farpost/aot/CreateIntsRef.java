package com.farpost.aot;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Util;

final class CreateIntsRef {

	private static final IntsRefBuilder scratchInts = new IntsRefBuilder();

	public static IntsRef fromString(String str) {
		return Util.toIntsRef(
			new BytesRef(str),
			scratchInts
		);
	}
}