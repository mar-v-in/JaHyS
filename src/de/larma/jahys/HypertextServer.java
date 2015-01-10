package de.larma.jahys;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HypertextServer {
	public static final int HTTP_PORT = 80;
	public static final int HTTPS_PORT = 443;
	private final HttpServer httpServer;

	public HypertextServer(boolean secure) throws IOException {
		this(secure, -1);
	}

	public HypertextServer(boolean secure, int port) throws IOException {
		int myport = port < 0 ? secure ? HTTPS_PORT : HTTP_PORT : port;
		httpServer = secure ? HttpsServer.create(new InetSocketAddress(myport), -1) :
					 HttpServer.create(new InetSocketAddress(myport), -1);
	}

	public void add(HypertextSite site) {
		httpServer.createContext(site.getSitePrefix(), new SiteHandler(site));
	}

	public void start() {
		httpServer.start();
	}

}
