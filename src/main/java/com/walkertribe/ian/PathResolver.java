package com.walkertribe.ian;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * A PathResolver is an object which can accept a path to a particular resource
 * and return an InputStream to it. This is used by IAN when it needs to read in
 * a resource. Note that all resource paths are expressed relative to the
 * Artemis install directory.
 * </p>
 * <p>
 * IAN provides two PathResolver implementations for you: FilePathResolver (to
 * read resources from a directory on disk, such as the Artemis install
 * directory) and ClasspathResolver (to read resources from the classpath).
 * </p>
 * @author rjwut
 */
public interface PathResolver {
	/**
	 * Returns an InputStream from which the data at the given path can be
	 * read.
	 */
	public InputStream get(String path) throws IOException;
}