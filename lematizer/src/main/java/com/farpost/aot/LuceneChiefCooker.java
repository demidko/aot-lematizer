package com.farpost.aot;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Util;

/**
 * Класс готовит строки на любой вкус
 */
class LuceneChiefCooker {

	private static final IntsRefBuilder secretIngredientForLuceneString = new IntsRefBuilder();
	private static final BytesRefBuilder secretIngredientForForJavaString = new BytesRefBuilder();

	public static IntsRef prepareLuceneString(String javaString) {
		return Util.toIntsRef(
			new BytesRef(javaString),
			secretIngredientForLuceneString
		);
	}

	public static String prepareJavaString(IntsRef luceneString) {
		return Util.toBytesRef(
			luceneString,
			secretIngredientForForJavaString
		).utf8ToString();
	}
}