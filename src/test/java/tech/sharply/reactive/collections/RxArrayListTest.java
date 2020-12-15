package tech.sharply.reactive.collections;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
public class RxArrayListTest {

	@Test
	void testWhenPuttingValues_thenTriggersEveryTime() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveList = new RxArrayList<String>();

		var disposable = reactiveList.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveList.add("one");
		reactiveList.add("two");

		Thread.sleep(2000);
		disposable.dispose();

		assertEquals(reactiveList.size(), triggersCount.get());
	}

	@Test
	void testWhenRemovingOneValue_thenTriggersOnce() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveList = new RxArrayList<String>();

		reactiveList.add("one");
		reactiveList.add("two");

		var disposable = reactiveList.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveList.remove("one");

		Thread.sleep(2000);
		disposable.dispose();
		// we subscribed after the first value was inserted
		assertEquals(1, triggersCount.get());
	}

	@Test
	void testWhenCallingPublish_thenTriggers() throws InterruptedException {
		var triggersCount = new AtomicInteger(0);
		var reactiveList = new RxArrayList<String>();

		reactiveList.add("one");
		reactiveList.add("two");

		var disposable = reactiveList.getPublisher().subscribe(map -> {
			triggersCount.incrementAndGet();
			log.info("received items from publisher: ");
			log.info("\n" + map.toString());
		});

		reactiveList.publish();

		Thread.sleep(2000);
		disposable.dispose();
		// we subscribed after the first value was inserted
		assertEquals(1, triggersCount.get());
	}

}
