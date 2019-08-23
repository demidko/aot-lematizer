package com.farpost.aot;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.Util;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class FstDictionary {

	final MorphologyTag[][] allMorphologyTags;
	final String[] allFlexionStrings;

	private final int[][] lemmas;
	private final FST<Long> refs;

	public FstDictionary() throws IOException {
		try (DataInputStream file = new DataInputStream(getClass().getResourceAsStream("/MRD.BIN"))) {
			allMorphologyTags = Reader.readMorph(ByteBlock.readBlockFrom(file));
			allFlexionStrings = Reader.readStrings(ByteBlock.readBlockFrom(file));
			lemmas = Reader.readLemmas(ByteBlock.readBlockFrom(file));
		}
		refs = Reader.readRefs(getClass().getResourceAsStream("FST.BIN"));
		Flexion.db = this;
	}


	///               ***                    ///
	/// Здесь начинается высокоуровневое API ///
	///               ***                    ///

	private List<Word> collectLemmas(int[] refs, String query) {
		Word[] res = new Word[refs.length];
		for (int i = 0; i < res.length; ++i) {
			res[i] = new Word(lemmas[refs[i]]);
		}
		return asList(res);
	}

	public List<Word> lookup(String query) throws IOException {
		Long refsToLemmas = Util.get(refs, new BytesRef(query.toLowerCase().replace('ё', 'е')))
		return refsToLemmas == null ? emptyList() : collectLemmas(refsToLemmas, query);
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
