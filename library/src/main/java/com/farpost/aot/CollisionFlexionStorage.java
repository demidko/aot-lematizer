package com.farpost.aot;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

import static com.farpost.aot.Decompiler.isEndl;
import static com.farpost.aot.Decompiler.stringFromBytes;

/**
 * Класс - хранилище служебной информации о флексиях,
 * у которых совпадает хеш, и которые по этой причине,
 * не могут быть сохранены в основеном хранилище по хешу.
 * Здесь они хранятся напрямую по строке.
 */
public class CollisionFlexionStorage {

	private final Map<String, int[]> map = new HashMap<>();

	/**
	 * @param flexion строка с колизионным хешем
	 * @return индексы лемм и грамматической информации
	 */
	public int[] get(final String flexion) {
		return map.getOrDefault(flexion, new int[0]);
	}

	public CollisionFlexionStorage() throws IOException {
		try (DataInputStream reader = new DataInputStream(getClass().getResourceAsStream("/collisions.bin"))) {
			final int count = reader.readInt();
			for (int i = 0; i < count; ++i) {

				final byte[] strbuf = new byte[36];
				int strbufIndex = -1;
				for (byte j = reader.readByte(); !isEndl(j); j = reader.readByte()) {
					strbuf[++strbufIndex] = j;
				}

				final String javaString = stringFromBytes(Arrays.copyOf(strbuf, strbufIndex + 1));
				final int lemmaIndex = reader.readInt();
				final int grammarIndex = reader.readInt();

				final int[] oldValue = map.get(javaString);

				if (oldValue == null) {
					map.put(javaString, new int[]{lemmaIndex, grammarIndex});
				} else {
					final int[] joinedValue = new int[oldValue.length + 2];
					System.arraycopy(oldValue, 0, joinedValue, 0, oldValue.length);
					joinedValue[joinedValue.length - 2] = lemmaIndex;
					joinedValue[joinedValue.length - 1] = grammarIndex;
					map.put(javaString, joinedValue);
				}
			}
		}
	}
}