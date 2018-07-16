package graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by wuhao on 7/27/16.
 */
public final class Buffers {
	private Buffers(){}
    public static final ByteBuffer dtmp = ByteBuffer.allocateDirect(80).order(ByteOrder.nativeOrder());
}
