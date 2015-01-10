package de.larma.jahys;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SiteHandler implements HttpHandler {
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String POST_HEADER = "post";
	private static final String FILE_ENCODING = "file.encoding";
	private static final Pattern PARAM_DIVIDER = Pattern.compile("[&]");
	private static final Pattern KEY_VALUE_DIVIDER = Pattern.compile("[=]");
	
	private final HypertextSite site;

	public SiteHandler( HypertextSite site) {
		this.site = site;
	}

	@Override
	public void handle( HttpExchange httpExchange) throws IOException {
		try {
			site.incomingRequest(httpExchange, parseParams(httpExchange));
		} catch (Throwable t) {
			// TODO: print error to httpexchange!
			t.printStackTrace();
		}
	}

	/**
	 * COMEFROM https://leonardom.wordpress.com/2009/08/06/getting-parameters-from-httpexchange/
	 *
	 * @param exchange
	 * @param params
	 * @throws UnsupportedEncodingException
	 */
	private void parseGetParameters( HttpExchange exchange,  Map<String, List<String>> params)
			throws UnsupportedEncodingException {
		URI requestedUri = exchange.getRequestURI();
		String query = requestedUri.getRawQuery();
		parseQuery(query, params);
	}

	
	private Map<String, List<String>> parseParams( HttpExchange httpExchange) throws IOException {
		Map<String, List<String>> params = new HashMap<>();
		parseGetParameters(httpExchange, params);
		parsePostParameters(httpExchange, params);
		return params;
	}

	/**
	 * COMEFROM https://leonardom.wordpress.com/2009/08/06/getting-parameters-from-httpexchange/
	 *
	 * @param httpExchange
	 * @param params
	 * @throws IOException
	 */
	private void parsePostParameters( HttpExchange httpExchange,  Map<String, List<String>> params)
			throws IOException {


		List<String> types = httpExchange.getRequestHeaders().get(CONTENT_TYPE_HEADER);
		String contentType = ((types != null) && (!types.isEmpty())) ? types.get(0) : "";
		if (POST_HEADER.equalsIgnoreCase(httpExchange.getRequestMethod()) && contentType.startsWith("text/")) {
			InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
			try (BufferedReader br = new BufferedReader(isr)) {
				String query = br.readLine();
				parseQuery(query, params);
			}
		} else {
			InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
			try (BufferedReader br = new BufferedReader(isr)) {
				String line;
				while((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
		}
	}

	/**
	 * COMEFROM https://leonardom.wordpress.com/2009/08/06/getting-parameters-from-httpexchange/
	 *
	 * @param query
	 * @param params
	 * @throws UnsupportedEncodingException
	 */
	private void parseQuery(CharSequence query,  Map<String, List<String>> params)
			throws UnsupportedEncodingException {

		if (query != null) {
			String[] pairs = PARAM_DIVIDER.split(query);

			for (String pair : pairs) {
				String[] param = KEY_VALUE_DIVIDER.split(pair);

				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], System.getProperty(FILE_ENCODING));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], System.getProperty(FILE_ENCODING));
				}

				if (params.containsKey(key)) {
					List<String> values = params.get(key);
					values.add(value);
				} else {
					List<String> list = new ArrayList<>();
					list.add(value);
					params.put(key, list);
				}
			}
		}
	}
}
