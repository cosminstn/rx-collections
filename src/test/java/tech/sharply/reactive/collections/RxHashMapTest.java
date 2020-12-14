package tech.sharply.reactive.collections;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
class RxHashMapTest {

	@Test
	void testWhenPuttingValues_thenTriggersEveryTime() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveMap = new RxHashMap<Integer, String>();

		var disposable = reactiveMap.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveMap.put(1, "one");
		reactiveMap.put(2, "two");

		Thread.sleep(2000);
		disposable.dispose();

		assertEquals(reactiveMap.size(), triggersCount.get());
	}

	@Test
	void testWhenRemovingOneValue_thenTriggersOnce() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveMap = new RxHashMap<Integer, String>();

		reactiveMap.put(1, "one");
		reactiveMap.put(2, "two");

		var disposable = reactiveMap.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveMap.remove(1);

		Thread.sleep(2000);
		disposable.dispose();
		// we subscribed after the first value was inserted
		assertEquals(1, triggersCount.get());
	}

	@Test
	void testWhenCallingPublish_thenTriggers() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveMap = new RxHashMap<Integer, String>();

		reactiveMap.put(1, "one");
		reactiveMap.put(2, "two");

		var disposable = reactiveMap.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveMap.publish();

		Thread.sleep(2000);
		disposable.dispose();
		// we subscribed after the first value was inserted
		assertEquals(1, triggersCount.get());
	}
}