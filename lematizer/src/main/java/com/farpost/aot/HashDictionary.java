package com.farpost.aot;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class HashDictionary {

	final MorphologyTag[][] allMorphologyTags;
	final String[] allFlexionStrings;

	private final int[][] lemmas;
	private final Map<Integer, int[]> refs;

	public HashDictionary() throws IOException {
		try (DataInputStream file = new DataInputStream(getClass().getResourceAsStream("/MRD.BIN"))) {
			allMorphologyTags = Reader.readMorph(ByteBlock.readBlockFrom(file));
			allFlexionStrings = Reader.readStrings(ByteBlock.readBlockFrom(file));
			lemmas = Reader.readLemmas(ByteBlock.readBlockFrom(file));
			refs = Reader.readRefs(ByteBlock.readBlockFrom(file));
		}
		Flexion.db = this;
	}

	private boolean isCollision(int[] links, String query) {
		for (int i = 0; i < links.length; i += 2) {
			if (allFlexionStrings[links[i]].equals(query)) {
				return false;
			}
		}
		return true;
	}

	///               ***                    ///
	/// Здесь начинается высокоуровневое API ///
	///               ***                    ///

	private List<Word> filterLemmas(int[] refs, String query) {
		Word[] res = new Word[refs.length];
		int i = -1;
		for (int ref : refs) {
			if (!isCollision(lemmas[ref], query)) {
				res[++i] = new Word(lemmas[ref]);
			}
		}
		return new ImmutableWordList(res, i + 1);
	}

	public List<Word> lookup(String query) {
		query = query.toLowerCase().replace('ё', 'е');
		int[] refsToLemmas = refs.get(query.hashCode());
		return refsToLemmas == null ? emptyList() : filterLemmas(refsToLemmas, query);
	}

	///                  ***                        ///
	/// Отсюда и ниже начинается низкоуровневое API ///
	///                  ***                        ///

	/**
	 * @param lemmaId идентификатор леммы
	 * @return строка леммы
	 */
	public String getLemmaString(int lemmaId) {
		return getFlexionString(lemmaId, 0);
	}

	/**
	 * @param lemmaId идентификатор леммы
	 * @return набор морфологических тегов
	 */
	public MorphologyTag[] getLemmaTags(int lemmaId) {
		return getFlexionTags(lemmaId, 0);
	}

	/**
	 * @param lemmaId идентификатор леммы
	 * @return количество флексий (вариантов) леммы
	 */
	public int fexionsSize(int lemmaId) {
		return lemmas[lemmaId].length / 2;
	}

	/**
	 * @param lemmaId      идентификатор леммы (исходной формы слова)
	 * @param flexionIndex индекс флексии (варианта) исходной формы слова
	 * @return строка этой флексии
	 */
	public String getFlexionString(int lemmaId, int flexionIndex) {
		return allFlexionStrings[lemmas[lemmaId][flexionIndex * 2]];
	}

	/**
	 * @param lemmaId      идентификатор леммы (исходной формы слова)
	 * @param flexionIndex индекс флексии (варианта) исходной формы слова
	 * @return набор морфологических тегов, характеризующих эту флексию
	 */
	public MorphologyTag[] getFlexionTags(int lemmaId, int flexionIndex) {
		return allMorphologyTags[lemmas[lemmaId][flexionIndex * 2 + 1]];
	}

	/**
	 * @param query слово для поиска по нему
	 * @return набор идентификаторов лемм (исходных форм слова)
	 */
	public List<Integer> lookupForLemmasIds(String query) {
		query = query.toLowerCase().replace('ё', 'е');
		int[] ids = refs.get(query.hashCode());
		return ids == null ? emptyList() : filterLemmasIds(ids, query);
	}

	private List<Integer> filterLemmasIds(int[] refs, String query) {
		List<Integer> res = new ArrayList<>();
		for (int ref : refs) {
			if (!isCollision(lemmas[ref], query)) {
				res.add(ref);
			}
		}
		return res;
	}
}
