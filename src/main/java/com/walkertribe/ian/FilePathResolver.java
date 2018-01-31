package com.walkertribe.ian;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An implementation of PathResolver that reads resources relative to a
 * specified directory on disk. This should be the Artemis install directory, or
 * another directory that contains the appropriate resources in the same paths.
 * @author rjwut
 */
public class FilePathResolver implements PathResolver {
	private File directory;

	public FilePathResolver(String directory) {
		this(new File(directory));
	}

	public FilePathResolver(File directory) {
		if (directory == null) {
			throw new IllegalArgumentException("The directory is required");
		}

		if (!directory.exists()) {
			throw new IllegalArgumentException("Directory does not exist");
		}

		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Not a directory");
		}

		this.directory = directory;
	}

	@Override
	public InputStream get(String path) throws IOException {
		return new FileInputStream(new File(directory, path));
	}
}