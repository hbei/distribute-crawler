package io.github.liuzm.distribute.server.disruptor;

import com.lmax.disruptor.EventFactory;

public class MessageEventFactory implements EventFactory<MessageHolder> {

	@Override
	public MessageHolder newInstance() {
		return new MessageHolder();
	}

}
