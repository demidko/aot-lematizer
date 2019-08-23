package com.farpost.aot;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.Util;

import java.io.IOException;

public class StringFstBuilder {

	private final Builder<Long> idBuilder = new Builder<>(
		FST.INPUT_TYPE.BYTE1,
		PositiveIntOutputs.getSingleton()
	);
	private final IntsRefBuilder scratchInts = new IntsRefBuilder();

	/**
	 * @param flexion     ключ
	 * @param metaLemmaId значение
	 */
	public void add(String flexion, long metaLemmaId) throws IOException {
		idBuilder.add(Util.toIntsRef(new BytesRef(flexion), scratchInts), metaLemmaId);
	}

	public FST<Long> finish() throws IOException {
		return idBuilder.finish();
	}
}
