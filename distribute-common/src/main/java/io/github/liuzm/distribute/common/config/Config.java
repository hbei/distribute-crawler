/**
 * 
 */
package io.github.liuzm.distribute.common.config;

/**
 * @author lxyq
 *
 */
public class Config {

	public static class ZKPath {

		public static String CONNECT_STR = "127.0.0.1:2181";

		public static int CONNECT_TIMEOUT = 1000;

		public static int RETRY_TIMEOUT = 1000;

		public static int RETRY_TIMES = 3;

		public static String REGISTER_CLIENT_PATH = "/AQClient/register-clients";
		
		public static String REGISTER_SERVER_PATH = "/AQServer/register-servers";

	}
}
