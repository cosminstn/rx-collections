package tech.sharply.reactive.collections;

import io.reactivex.rxjava3.processors.PublishProcessor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An ArrayList extension that on every put/remove operation emits the complete new state.
 * Warning: Changes to value object property level should be emitted manually; RxArrayList cannot detect those changes.
 * @param <V>
 */
@Getter
public class RxArrayList<V> extends ArrayList<V> implements IRxCollection {

	private final PublishProcessor<ArrayList<V>> publisher;

	public RxArrayList() {
		super();
		this.publisher = PublishProcessor.create();
	}

	public RxArrayList(Collection<? extends V> collection) {
		super(collection);
		this.publisher = PublishProcessor.create();
	}

	@Override
	public boolean add(V value) {
		final var val = super.add(value);
		this.publish();
		return val;
	}

	@Override
	public boolean addAll(Collection<? extends V> collection) {
		final var val = super.addAll(collection);
		this.publish();
		return val;
	}

	@Override
	public boolean remove(Object value) {
		final var val = super.remove(value);
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
		this.forEach(val -> str.set(str.get() + val + "\n"));

		return str.get();
	}
}
