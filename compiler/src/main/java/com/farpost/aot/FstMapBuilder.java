package com.farpost.aot;

import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.IntSequenceOutputs;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class FstMapBuilder {

	private final Map<String, Set<Integer>> map = new HashMap<>();

	/**
	 * @param flexion ключ
	 * @param lemmaId значение
	 */
	public void add(String flexion, int lemmaId) {
		map.computeIfAbsent(flexion, k -> new HashSet<>()).add(lemmaId);
	}

	public FST<IntsRef> finish() throws IOException {

		var result = new Builder<>(
			FST.INPUT_TYPE.BYTE1,
			IntSequenceOutputs.getSingleton()
		);

		var lst = map.entrySet().stream().sorted((a, b) -> {
			var res = String.CASE_INSENSITIVE_ORDER.compare(a.getKey(), b.getKey());
			return (res != 0) ? res : a.getKey().compareTo(b.getKey());
		}).collect(toList());

		for (var entry : lst) {
			var ints = entry.getValue().stream().mapToInt(Number::intValue).toArray();
			result.add(
				CreateIntsRef.fromString(entry.getKey()),
				new IntsRef(ints, 0, ints.length)
			);
		}

		return result.finish();
	}
}
