/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.xshadov.easyrandom.vavr.map;

import io.github.xshadov.easyrandom.vavr.VavrGenerationTests;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import lombok.Value;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;

public class HashMapRandomizeTest extends VavrGenerationTests {
	@Value
	private static class Person {
		private HashMap<String, Integer> simple;
		private HashMap<String, List<String>> listMap;
		private HashMap<String, Set<String>> setMap;
		private HashMap<String, HashMap<String, String>> nestedMap;
	}

	@Test
	public void correctRandomization() {
		final Person randomPerson = random(Person.class);

		assertSizeInRange(randomPerson.getSimple());

		assertSizeInRange(randomPerson.getListMap());
		randomPerson.getListMap().forEach((key, value) -> assertSizeInRange(value));

		assertSizeInRange(randomPerson.getNestedMap());
		randomPerson.getNestedMap().forEach((key, value) -> assertSizeInRange(value));
	}

	@Test
	public void randomizationWithConstantRanges() {
		final EasyRandomParameters parameters = defaultParameters()
				.randomize(String.class, () -> "constant")
				.randomize(Integer.class, () -> 123);

		final Person randomPerson = random(Person.class, parameters);

		assertHasSingleElement(randomPerson.getSimple());

		assertHasSingleElement(randomPerson.getListMap());
		randomPerson.getListMap().forEach((key, value) -> assertSizeInRange(value));

		assertHasSingleElement(randomPerson.getSetMap());
		randomPerson.getSetMap().forEach((key, value) -> assertHasSingleElement(value));

		assertHasSingleElement(randomPerson.getNestedMap());
		randomPerson.getNestedMap().forEach((key, value) -> assertHasSingleElement(value));
	}
}
