package com.gottaeat.services.fraud.scoring.fraudlabs;

import java.net.Inet6Address;
import java.net.InetAddress;

public class Ip6Test {

	public static void main(String[] args) throws Exception {
		Inet6Address addr = getIPv6Addresses(InetAddress.getAllByName("localhost"));
		System.out.println(addr.getHostAddress());
	}
	
	public static Inet6Address getIPv6Addresses(InetAddress[] addresses) {
	    for (InetAddress addr : addresses) {
	        if (addr instanceof Inet6Address) {
	            return (Inet6Address) addr;
	        }
	    }
	    return null;
	}
	
}
