package com.farpost.aot;

import org.apache.lucene.store.InputStreamDataInput;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.IntSequenceOutputs;
import org.apache.lucene.util.fst.Util;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FstMap {

	private final FST<IntsRef> fst;
	private final int[] emptyResult = new int[]{};

	private FstMap(FST<IntsRef> fst) {
		this.fst = fst;
	}

	public int[] get(String flexion) throws IOException {
		IntsRef maybeResult = Util.get(fst, CreateIntsRef.fromString(flexion));
		return maybeResult == null ? emptyResult : maybeResult.ints;
	}

	public static FstMap readFromTarget() throws IOException {
		return new FstMap(new FST<>(
			new InputStreamDataInput(new FileInputStream(Paths.get("target/TESTFST.BIN").toAbsolutePath().toString())),
			IntSequenceOutputs.getSingleton()
		));
	}
}

public class FstMapBuilderTest {

	private FST<IntsRef> fst;

	@BeforeTest
	public void setUp() throws IOException {
		var builder = new FstMapBuilder();
		builder.add("топор", 1);
		builder.add("дерево", 2);
		builder.add("топор", 30);
		fst = builder.finish();
	}

	@Test
	public void testFinish() throws IOException {
		var axe = Util.get(fst, CreateIntsRef.fromString("топор"));

		assertThat(axe, not(nullValue()));
		assertThat(axe.ints, not(nullValue()));
		assertThat(axe.ints.length, equalTo(2));

	}


	@Test
	public void restoreTest() throws IOException {
		fst.save(Paths.get("target/TESTFST.BIN").toAbsolutePath());
		var map = FstMap.readFromTarget();
		assertThat(map.get("топор"), not(nullValue()));
		assertThat(Arrays.toString(map.get("топор")), equalTo("[1, 30]"));
	}
}