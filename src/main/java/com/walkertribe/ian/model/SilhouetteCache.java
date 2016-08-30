package com.walkertribe.ian.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.walkertribe.ian.Context;

/**
 * A convenient class for generating Silhouettes on demand and storing them for
 * later use.
 * @author rjwut
 */
public class SilhouetteCache {
	/**
	 * Composite key under which Silhouettes are stored.
	 */
	private class Key {
		private Model model;
		private Color color;
		private int hashCode;

		private Key(Model model, Color color) {
			this.model = model;
			this.color = color;
			hashCode = model.hashCode() * 31 + color.hashCode();
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}

			if (!(object instanceof Key)) {
				return false;
			}

			Key that = (Key) object;
			return model.equals(that.model) && color.equals(that.color);
		}

		@Override
		public int hashCode() {
			return hashCode;
		}
	}

	private Context ctx;
	private int size;
	private Map<Key, Silhouette> map = new HashMap<Key, Silhouette>();

	/**
	 * Creates a new SilhouetteCache where all the Silhouettes created will be
	 * of the given size.
	 */
	public SilhouetteCache(Context ctx, int size) {
		this.ctx = ctx;
		this.size = size;
	}

	/**
	 * Retrieves the indicated Silhouette from the cache. Silhouettes are
	 * created and cached automatically on demand if they are not already
	 * cached.
	 */
	public Silhouette get(Model model, Color color) {
		Key key = new Key(model, color);
		Silhouette silhouette = map.get(key);

		if (silhouette == null) {
			silhouette = new Silhouette(ctx, model, size, color);
			map.put(key, silhouette);
		}

		return silhouette;
	}
}