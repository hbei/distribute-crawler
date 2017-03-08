package io.github.liuzm.distribute.server.disruptor;

import com.lmax.disruptor.RingBuffer;

public class MessageEventProducer {

	private final RingBuffer<MessageHolder> ringBuffer;

	public MessageEventProducer(RingBuffer<MessageHolder> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void produce(Message message) {
		long sequence = ringBuffer.next();
		try {
			MessageHolder cpe = ringBuffer.get(sequence);
			cpe.set(message);
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
