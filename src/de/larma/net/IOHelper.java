package de.larma.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOHelper {
	private IOHelper() {

	}

	public static void stream(InputStream is, OutputStream os) throws IOException {
		int i = -1;
		while((i = is.read()) >= 0) {
			os.write(i);
		}
	}
}
