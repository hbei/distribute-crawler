package io.github.liuzm.distribute.remoting.protocol;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;

public interface CommandHeader {
	void checkFields() throws RemotingCommandException;
}
