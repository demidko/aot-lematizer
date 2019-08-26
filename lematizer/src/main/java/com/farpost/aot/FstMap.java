package com.farpost.aot;

import org.apache.lucene.store.InputStreamDataInput;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.IntSequenceOutputs;
import org.apache.lucene.util.fst.Util;

import java.io.IOException;


public class FstMap {

	private final FST<IntsRef> fst;
	private final int[] emptyResult = new int[]{};

	private FstMap(FST<IntsRef> fst) {
		this.fst = fst;
	}

	public int[] get(String flexion) throws IOException {
		IntsRef maybeResult = Util.get(fst, CreateIntsRef.fromString(flexion));
		return maybeResult == null ? emptyResult : maybeResult.ints;
	}

	public static FstMap readFromResources() throws IOException {
		return new FstMap(new FST<>(
			new InputStreamDataInput(FstMap.class.getResourceAsStream("/FST.BIN")),
			IntSequenceOutputs.getSingleton()
		));
	}
}
