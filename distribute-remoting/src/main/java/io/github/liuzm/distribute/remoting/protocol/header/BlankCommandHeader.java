package io.github.liuzm.distribute.remoting.protocol.header;

import io.github.liuzm.distribute.remoting.exception.RemotingCommandException;
import io.github.liuzm.distribute.remoting.protocol.CommandHeader;

public class BlankCommandHeader implements CommandHeader {
	
	private String nodeId;
	
	public BlankCommandHeader(String nodeId){
		this.nodeId = nodeId;
	}

	@Override
	public void checkFields() throws RemotingCommandException {
		
	}
}
