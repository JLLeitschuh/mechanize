/**
 * Copyright (C) 2012-2014 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize.document.json.query;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.document.json.node.JsonNode;
import com.gistlabs.mechanize.document.json.node.impl.ObjectNodeImpl;
import com.gistlabs.mechanize.util.css_query.NodeSelector;


public class NodeSelectorTest {

	protected NodeSelector<JsonNode> build(final String json) throws JSONException {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject(json));
		return node.buildNodeSelector();
	}

	@Test
	public void testStar() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		List<JsonNode> result = selector.findAll("*");
		assertEquals(8, result.size());
	}

	@Test
	public void testName() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		List<JsonNode> result = selector.findAll("b");
		assertEquals(2, result.size());
	}

	@Test
	public void testNames() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		List<JsonNode> result = selector.findAll("b, results");
		assertEquals(4, result.size());
	}

	@Test
	public void testAttributePresent() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		List<JsonNode> result = selector.findAll("b[x]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));

		assertTrue(selector.findAll("b[z]").isEmpty());
	}

	@Test
	public void testAttributeEquals() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		List<JsonNode> result = selector.findAll("b[x=\"y\"]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));

		assertTrue(selector.findAll("b[x=\"z\"]").isEmpty());
	}

	@Test
	public void testAttributeTilda() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y foo bar\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		assertEquals(1, selector.findAll("b[x~=\"y\"]").size());
		assertEquals(1, selector.findAll("b[x~=\"foo\"]").size());
		assertEquals(1, selector.findAll("b[x~=\"bar\"]").size());

		assertEquals(0, selector.findAll("b[x~=\"baz\"]").size());
	}

	@Test
	public void testWildcardWithAttribute() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }");

		List<JsonNode> result = selector.findAll("*[x]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));

		assertTrue(selector.findAll("b[z]").isEmpty());
	}
	@Test
	public void testNot() throws Exception {
		NodeSelector<JsonNode> selector = build("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"a\" : 2 } ] }");

		List<JsonNode> result = selector.findAll("*:not([x])");
		assertEquals(7, result.size());
		assertEquals("root", result.get(0).getName());
		assertEquals("2", result.get(0).getAttribute("a"));

		List<JsonNode> result2 = selector.findAll("*:not([a])");
		assertEquals(5, result2.size());
		assertEquals("b", result2.get(0).getName());
	}
}


