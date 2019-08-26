package com.farpost.aot;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class Morphology {

	final MorphologyTag[][] allMorphologyTags;
	final String[] allFlexionStrings;

	private final int[][] lemmas;
	private final FstMap lemmasMap;

	public Morphology() throws IOException {
		try (DataInputStream file = new DataInputStream(getClass().getResourceAsStream("/MRD.BIN"))) {
			allMorphologyTags = MrdReader.readMorph(ByteBlock.readBlockFrom(file));
			allFlexionStrings = MrdReader.readStrings(ByteBlock.readBlockFrom(file));
			lemmas = MrdReader.readLemmas(ByteBlock.readBlockFrom(file));
		}
		lemmasMap = FstMap.readFromResources();
		Flexion.db = this;
	}

	///               ***                    ///
	/// Здесь начинается высокоуровневое API ///
	///               ***                    ///

	public List<Word> lookup(String query) throws IOException {
		int[] refs = lemmasMap.get(query.toLowerCase().replace('ё', 'е'));
		Word[] res = new Word[refs.length];
		for (int i = 0; i < res.length; ++i) {
			res[i] = new Word(lemmas[refs[i]]);
		}
		return asList(res);
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
	public int[] lookupForLemmasIds(String query) throws IOException {
		return lemmasMap.get(query.toLowerCase().replace('ё', 'е'));
	}
}
