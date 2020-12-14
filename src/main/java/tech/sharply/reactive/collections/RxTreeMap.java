package tech.sharply.reactive.collections;

import io.reactivex.rxjava3.processors.PublishProcessor;
import lombok.Getter;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class RxTreeMap<K, V> extends TreeMap<K, V> implements RxCollection {

	private final PublishProcessor<TreeMap<K, V>> publisher;

	public RxTreeMap() {
		super();
		this.publisher = PublishProcessor.create();
	}

	public RxTreeMap(Map<? extends K, ? extends V> map) {
		super(map);
		this.publisher = PublishProcessor.create();
	}

	public RxTreeMap(Comparator<? super K> comparator) {
		super(comparator);
		this.publisher = PublishProcessor.create();
	}

	@Override
	public V put(K key, V value) {
		final var val = super.put(key, value);
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
