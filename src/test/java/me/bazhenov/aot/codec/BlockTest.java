package me.bazhenov.aot.codec;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static me.bazhenov.aot.codec.Block.lookupAffix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BlockTest {

	@Test
	public void pageObjectShouldBeAbleToReadWriteResultsFromByteBuffer() throws IOException {
		BlockLine[] lines = new BlockLine[]{
			new BlockLine("ая",
				new BlockLine.MorphRef(1, new short[]{1, 2, 3}),
				new BlockLine.MorphRef(2, new short[]{4, 5, 6})),
			new BlockLine("ный",
				new BlockLine.MorphRef(3, new short[]{7, 8, 9}))
		};

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Block.writeTo(buffer, lines);

		buffer.flip();

		buffer.mark();
		assertThat(lookupAffix(buffer, "ный"), equalTo(new BlockLine.MorphRef[]{
			new BlockLine.MorphRef(3, new short[]{7, 8, 9})
		}));

		buffer.reset();
		assertThat(lookupAffix(buffer, "ая"), equalTo(new BlockLine.MorphRef[]{
			new BlockLine.MorphRef(1, new short[]{1, 2, 3}),
			new BlockLine.MorphRef(2, new short[]{4, 5, 6})
		}));

		buffer.reset();
		assertThat(lookupAffix(buffer, "аа"), nullValue());

		buffer.reset();
		assertThat(lookupAffix(buffer, "ная"), nullValue());

		buffer.reset();
		assertThat(lookupAffix(buffer, "няя"), nullValue());
	}

	@Test
	public void compare() {
		assertThat(Block.compare(new byte[]{1, 2}, new byte[]{1, 2}), is(0));
		assertThat(Block.compare(new byte[]{1, 3}, new byte[]{1, 2}), is(-1));
		assertThat(Block.compare(new byte[]{1, 2}, new byte[]{1, 3}), is(1));
		assertThat(Block.compare(new byte[]{1}, new byte[]{1, 3}), is(1));
		assertThat(Block.compare(new byte[]{1, 2}, new byte[]{1}), is(-1));
	}
}
