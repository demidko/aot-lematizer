package com.farpost.aot;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MorphologyTest {

	private final Morphology d = new Morphology();

	public MorphologyTest() throws IOException {
	}

	@Test
	public void dictionaryShouldBeAbleToFindExistentWords() throws IOException {
		assertThat(d.lookup("краснеющий"), hasSize(1));
		assertThat(d.lookup("дорога"), hasSize(2));
		assertThat(d.lookup("клавиатура"), hasSize(1));

		//low level
		assertThat(d.lookup("краснеющий"), hasSize(1));
		assertThat(d.lookup("дорога"), hasSize(2));
		assertThat(d.lookup("клавиатура"), hasSize(1));
	}

	@Test
	public void dictionaryShouldNotFindNotRealWords() throws IOException {
		assertThat(d.lookup("фентифлюшка"), hasSize(0));
		// low level
		assertThat(d.lookupForLemmasIds("фентифлюшка").length, equalTo(0));
	}

	private List<String> collectNormsLowLevel(int[] ids) {
		List<String> res = new ArrayList<>();
		Arrays.stream(ids).forEach(x -> res.add(d.getLemmaString(x)));
		return res;
	}

	@Test
	public void lowerCaseWorkingCorrectly() throws IOException {
		List<String> lemmas = d
			.lookup("Германия")
			.stream()
			.map(w -> w.getLemma().toString())
			.collect(toList());

		assertThat(lemmas, containsInAnyOrder("германия", "германий"));

		// low level
		assertThat(collectNormsLowLevel(d.lookupForLemmasIds("Германия")),
			containsInAnyOrder("германия", "германий"));
	}

	@Test
	public void testEmptyWordBases() throws IOException {
		assertThat(d.lookup("человек"), hasSize(1));
		assertThat(d.lookup("люди"), hasSize(1));
		assertThat(d.lookup("ребёнок"), hasSize(1));
		assertThat(d.lookup("дети"), hasSize(1));

		//low level
		assertThat(d.lookupForLemmasIds("человек").length, equalTo(1));
		assertThat(d.lookupForLemmasIds("люди").length, equalTo(1));
		assertThat(d.lookupForLemmasIds("ребёнок").length, equalTo(1));
		assertThat(d.lookupForLemmasIds("дети").length, equalTo(1));
	}

	@Test
	public void shouldNotThrowExceptionIfWordHasUnknownCharacter() throws IOException {
		assertThat(d.lookup("super#starnge@string"), hasSize(0));
		//low level
		assertThat(d.lookupForLemmasIds("super#starnge@string").length, equalTo(0));
	}


	private static List<String> collectNorms(final List<Word> l) {
		return l.stream()
			.map(w -> w.getLemma().toString())
			.collect(toList());
	}


	@Test
	public void dictionaryShouldBeAbleToReturnWordNorms() throws IOException {
		assertThat(collectNorms(d.lookup("дорога")), hasItems("дорога", "дорогой"));
		assertThat(collectNorms(d.lookup("черномырдину")), hasItems("черномырдин"));

		// low level
		assertThat(collectNormsLowLevel(d.lookupForLemmasIds("дорога")), hasItems("дорога", "дорогой"));
		assertThat(collectNormsLowLevel(d.lookupForLemmasIds("черномырдину")), hasItems("черномырдин"));
	}

	@Test
	public void regression1() throws IOException {
		assertThat(collectNorms(d.lookup("замок")), hasItems("замок", "замокнуть"));

		//low level
		assertThat(collectNormsLowLevel(d.lookupForLemmasIds("замок")), hasItems("замок", "замокнуть"));
	}

	@Test
	public void regression3() throws IOException {
		assertThat(d.lookup("и"), hasSize(2));
		// low level
		assertThat(d.lookupForLemmasIds("и").length, equalTo(2));
	}


	@Test
	public void regression2() throws IOException {
		assertThat(collectNorms(d.lookup("придет")), hasItems("прийти"));
		// low level
		assertThat(collectNormsLowLevel(d.lookupForLemmasIds("придет")), hasItems("прийти"));
	}

	@Test
	public void dictionaryShouldBeAbleToReturnWordNormsForEmptyBases() throws IOException {
		List<String> norms = collectNorms(d.lookup("люди"));
		assertThat(norms, hasSize(1));
		assertThat(norms, hasItems("человек"));

		// low level
		List<String> norms2 = collectNormsLowLevel(d.lookupForLemmasIds("люди"));
		assertThat(norms, hasSize(1));
		assertThat(norms, hasItems("человек"));
	}
}
