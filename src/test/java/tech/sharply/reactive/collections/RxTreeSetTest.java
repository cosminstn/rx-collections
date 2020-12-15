package tech.sharply.reactive.collections;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class RxTreeSetTest {

	@Test
	void testWhenPuttingValues_thenTriggersEveryTime() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveSet = new RxTreeSet<String>();

		var disposable = reactiveSet.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveSet.add("one");
		reactiveSet.add("two");

		Thread.sleep(2000);
		disposable.dispose();

		assertEquals(reactiveSet.size(), triggersCount.get());
	}

	@Test
	void testWhenRemovingOneValue_thenTriggersOnce() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveSet = new RxTreeSet<String>();

		reactiveSet.add("one");
		reactiveSet.add("two");

		var disposable = reactiveSet.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveSet.remove("one");

		Thread.sleep(2000);
		disposable.dispose();
		// we subscribed after the first value was inserted
		assertEquals(1, triggersCount.get());
	}

	@Test
	void testWhenCallingPublish_thenTriggers() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveSet = new RxTreeSet<String>();

		reactiveSet.add("one");
		reactiveSet.add("two");

		var disposable = reactiveSet.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveSet.publish();

		Thread.sleep(2000);
		disposable.dispose();
		// we subscribed after the first value was inserted
		assertEquals(1, triggersCount.get());
	}
}