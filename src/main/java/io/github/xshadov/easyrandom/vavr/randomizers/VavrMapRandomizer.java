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

package io.github.xshadov.easyrandom.vavr.randomizers;

import io.vavr.Tuple2;
import io.vavr.collection.*;
import lombok.Builder;
import lombok.Getter;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.AbstractRandomizer;

import java.util.stream.Collector;

@Getter
@Builder
class VavrMapRandomizer<S, T> extends AbstractRandomizer<Map<S, T>> implements VavrCollectionRandomizer<S, Set<S>> {
	private Randomizer<? extends S> keyRandomizer;
	private Randomizer<? extends T> valueRandomizer;
	private EasyRandomParameters.Range<Integer> collectionSizeRange;
	@Builder.Default
	private Collector<Tuple2<S, T>, ?, ? extends Map<S, T>> collector = HashMap.collector();

	@Override
	public Map<S, T> getRandomValue() {
		final Set<S> keys = getDistinctCollection(HashSet.collector());
		final List<T> values = List.fill(keys.size(), () -> valueRandomizer.getRandomValue());

		return keys.zip(values)
				   .collect(collector);
	}

	@Override
	public Randomizer<? extends S> getValueRandomizer() {
		return keyRandomizer;
	}
}
