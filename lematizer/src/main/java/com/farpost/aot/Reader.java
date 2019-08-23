package com.farpost.aot;

import org.apache.lucene.store.InputStreamDataInput;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.PositiveIntOutputs;

import java.io.IOException;
import java.io.InputStream;

class Reader {

	static MorphologyTag[][] readMorph(ByteBlock block) {
		MorphologyTag[][] result = new MorphologyTag[block.getLinesCount()][];
		for (int i = 0, pos = 0; i < block.getLinesCount(); ++i) {
			int to = pos;
			while (Bytecode.isContent(block.getBytes()[to])) {
				++to;
			}
			MorphologyTag[] currentTags = result[i] = new MorphologyTag[to - pos];
			for (int j = 0; j < currentTags.length; ++j, ++pos) {
				currentTags[j] = MorphologyTag.values()[block.getBytes()[pos]];
			}
			// !
			++pos;
			// !
		}
		return result;
	}

	static String[] readStrings(ByteBlock block) {
		String[] result = new String[block.getLinesCount()];
		for (int i = 0, pos = 0; i < block.getLinesCount(); ++i) {
			int to = pos;
			while (Bytecode.isContent(block.getBytes()[to])) {
				++to;
			}
			char[] buf = new char[to - pos];
			for (int j = 0; j < buf.length; ++j, ++pos) {
				buf[j] = FarpostAotUtils.safeByteToChar(block.getBytes()[pos]);
			}
			++pos;
			result[i] = new String(buf);
		}
		return result;
	}

	static int[][] readLemmas(ByteBlock block) {
		int[][] result = new int[block.getLinesCount()][];
		for (int i = 0, pos = 0; i < block.getLinesCount(); ++i) {
			int lemmaSize = intFromByteArray(block.getBytes(), pos);
			pos += 4;
			int[] curr = result[i] = new int[lemmaSize * 2];
			for (int j = 0; j < (lemmaSize * 2); j += 2, pos += 8) {
				curr[j] = intFromByteArray(block.getBytes(), pos);
				curr[j + 1] = intFromByteArray(block.getBytes(), pos + 4);
			}
		}
		return result;
	}

	static FST<Long> readRefs(InputStream stream) throws IOException {
		return new FST<Long>(
			new InputStreamDataInput(stream),
			PositiveIntOutputs.getSingleton()
		);
	}

	private static int intFromByteArray(byte[] arr, int from) {
		return arr[from + 3] & 0xFF |
			(arr[from + 2] & 0xFF) << 8 |
			(arr[from + 1] & 0xFF) << 16 |
			(arr[from] & 0xFF) << 24;
	}
}
