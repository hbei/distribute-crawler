package io.github.liuzm.distribute.remoting.protocol;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;

public interface CommandBody {
	void checkFields() throws RemotingCommandException;
}
