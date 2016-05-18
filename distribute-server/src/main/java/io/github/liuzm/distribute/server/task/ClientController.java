/**
 * 
 * copyright@ xinhe99.com 信和大金融
 */
package io.github.liuzm.distribute.server.task;

import io.github.liuzm.distribute.remoting.protocol.Command;

/**
 * @author xh-liuzhimin
 *
 */
public interface ClientController {
	
	public int startCommand(Command c,String clientId);
	
	public int stopCommand(Command c,String clientId);
	
	public void SuspendCommand(Command c,String clientId);

}
