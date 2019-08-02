package com.farpost.aot;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.farpost.aot.Decompiler.isEndl;
import static com.farpost.aot.Decompiler.stringFromBytes;

/**
 * Хранилище лемм, доступных по индексу
 */
public class LemmaStorage {

	private final byte[][] lines;

	public LemmaStorage() throws IOException {
		try(DataInputStream reader = new DataInputStream(getClass().getResourceAsStream("/lemmas.bin"))) {
			lines = new byte[reader.readInt()][];
			final byte[] buf = new byte[36];
			for(int i = 0, bufIndex = 0; i < lines.length; ++i, bufIndex = 0) {
				for(byte j = reader.readByte(); !isEndl(j); j = reader.readByte(), ++bufIndex) {
					buf[bufIndex] = j;
				}
				//!!
				lines[i] = Arrays.copyOf(buf, bufIndex);
			}
		}
	}

	/**
	 * Принимает индекс леммы, возвращает лемму
	 * @param requestedIndex индекс леммы
	 * @return лемма
	 */
	public String get(final int requestedIndex) {
		return stringFromBytes(lines[requestedIndex]);
	}
}
