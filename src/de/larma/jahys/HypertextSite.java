package de.larma.jahys;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HypertextSite {
	String getSitePrefix();
	void incomingRequest(HttpExchange httpExchange, Map<String,List<String>> params) throws IOException;
}
