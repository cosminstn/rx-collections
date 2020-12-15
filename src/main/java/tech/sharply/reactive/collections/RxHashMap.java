package tech.sharply.reactive.collections;

import io.reactivex.rxjava3.processors.PublishProcessor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A HashMap extension that on every put/remove operation emits the complete new state.
 * E.g.:
 * data: (1, 2), (3, 5)
 * cmd put(4, 4)
 * data: (1, 2), (3, 5), (4, 4)
 * -> emits (1, 2), (3, 5), (4, 4)
 * <p>
 * This collections is useful only in multithreaded contexts because on single threaded contexts you can easily read the data.
 * TODO: Handle changes on item level. E.g. map.get(4).setId(4); // this will not notify
 * Warning: Changes to value object property level should be emitted manually; RxHashMap cannot detect those changes.
 *
 * @param <K>
 * @param <V>
 */
@Getter
public class RxHashMap<K, V> extends HashMap<K, V> implements RxCollection {

	private final PublishProcessor<HashMap<K, V>> publisher;
	// TODO: To look into ReplayProcessor as well

	public RxHashMap() {
		super();
		this.publisher = PublishProcessor.create();
	}

	public RxHashMap(Map<? extends K, ? extends V> map) {
		super(map);
		this.publisher = PublishProcessor.create();
	}

	public RxHashMap(int initialCapacity) {
		super(initialCapacity);
		this.publisher = PublishProcessor.create();
	}

	@Override
	public V put(K key, V value) {
		final var val = super.put(key, value);
		// emit changes
		this.publish();
		return val;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		super.putAll(map);
		this.publish();
	}

	@Override
	public V remove(Object key) {
		final var val = super.remove(key);
		this.publish();
		return val;
	}

	@Override
	public void clear() {
		super.clear();
		this.publish();
	}

	/**
	 * Publishes the current state to the subscribers.
	 * Allows manually publishing for value object level changes.
	 */
	@Override
	public void publish() {
		publisher.onNext(this);
	}

	@Override
	public String toString() {
		AtomicReference<String> str = new AtomicReference<>("");
		this.keySet().forEach(key -> str.set(str.get() + key + "=" + this.get(key) + "\n"));

		return str.get();
	}
}
