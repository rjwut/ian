package com.walkertribe.ian;

import java.io.IOException;
import java.io.InputStream;

/**
 * An implementation of PathResolver that reads resources relative to a
 * specified Class on the classpath. This allows you to bundle the required
 * resources inside your project.
 * @author rjwut
 */
public class ClasspathResolver implements PathResolver {
	private Class<?> clazz;

	public ClasspathResolver(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Class cannot be null");
		}

		this.clazz = clazz;
	}

	@Override
	public InputStream get(String path) throws IOException {
		return clazz.getResourceAsStream(path);
	}
}