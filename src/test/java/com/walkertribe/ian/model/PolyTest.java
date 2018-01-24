package com.walkertribe.ian.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PolyTest {
	@Test
	public void test() {
		List<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		ids.add("3");
		new Poly(ids);
	}
}
