package de.larma.jahys;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class RequestInfoSite implements HypertextSite {
	private void addDivider(StringBuilder out) {
		out.append("<tr><td style=\"border-top: 1px solid black; padding: 0\" colspan=\"2\"></td></tr>");
	}

	private void addFooter(StringBuilder out) {
		out.append("</tbody></table><footer><hr><small>Generated by JaHyS</small></footer></body>");
	}

	private void addHeader(StringBuilder out) {
		out.append(
				"<!DOCTYPE html>\n<html><head><title>Request Info</title></head><body><header><h1>Request Info  Test" +
				" Page</h1><hr></header><table><thead><tr><td>Name</td><td>Value</td></tr></thead><tbody>");
	}

	private void addLine(Object name, Object value, StringBuilder out) {
		out.append("<tr><td>").append(name).append("</td><td>").append(value).append("</td></tr>");
	}

	@Override
	public String getSitePrefix() {
		return "/.special_page/request_info";
	}

	@Override
	public void incomingRequest(HttpExchange httpExchange, Map<String, List<String>> params) throws IOException {
		StringBuilder out = new StringBuilder();
		httpExchange.getResponseHeaders().set(SiteHandler.CONTENT_TYPE_HEADER, "text/html");
		addHeader(out);
		addDivider(out);
		addLine("Protocol", httpExchange.getProtocol(), out);
		addLine("Method", httpExchange.getRequestMethod(), out);
		addLine("URI", httpExchange.getRequestURI(), out);
		addLine("Remote", httpExchange.getRemoteAddress(), out);
		addDivider(out);
		for (Map.Entry<String, List<String>> entry : httpExchange.getRequestHeaders().entrySet()) {
			addLine(entry.getKey(), entry.getValue(), out);
		}
		addDivider(out);
		for (Map.Entry<String, List<String>> entry : params.entrySet()) {
			addLine(entry.getKey(), entry.getValue(), out);
		}
		addFooter(out);
		httpExchange.sendResponseHeaders(200, out.length());
		try (OutputStream os = httpExchange.getResponseBody()) {
			os.write(out.toString().getBytes());
		}
	}
}