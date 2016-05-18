/**
 * 
 */
package io.github.liuzm.distribute.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 获取当前机器ip的基本信息
 * 
 * @author xh-liuzhimin
 *
 */
public class LocalIPUtil {

	/**
	 * 获取本地的ip地址
	 * 
	 * @return
	 * @throws SocketException 
	 */
	public static String getIpAddress() throws SocketException {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean finded = false;// 是否找到外网IP
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
					localip = ip.getHostAddress();
				}
			}
		}

		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	/**
	 * 获取机器的hostname
	 * 
	 * @return
	 */
	public static String getHostname() {
		try {
			InetAddress netAddress = InetAddress.getLocalHost();
			if (netAddress != null) {
				return netAddress.getHostName();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) throws SocketException {
		System.out.println(getIpAddress());
		System.out.println(getHostname());
	}
}
