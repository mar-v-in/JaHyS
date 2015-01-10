package de.larma.jahys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerApp {

	public static void main(String[] args) throws IOException {
		int port = -1;
		boolean secure = false;
		List<HypertextSite> sites = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			switch (arg) {
				case "-s":
					secure = true;
					break;
				case "-p":
					i++;
					port = Integer.parseInt(args[i]);
					break;
				case "-f":
					i++;
					String prefix = args[i];
					i++;
					String files = args[i];
					sites.add(new FileSystemSite(prefix, files));
					break;
				case "-r":
					sites.add(new RequestInfoSite());
					break;
				default:
					System.out.println("Unknown option: " + arg);
			}
		}
		HypertextServer server = new HypertextServer(secure, port);
		for (HypertextSite site : sites) {
			server.add(site);
		}
		server.start();
		System.out.println("Starting " + (secure ? "secure " : "") + "server on port " + port + ".");
		System.out.println("Sites started: ");
		String page = "http" + (secure ? "s" : "") + "://127.0.0.1:" + port;
		for (HypertextSite site : sites) {
			System.out.println(page + site.getSitePrefix() + "\t- " + site.getClass().getName());
		}
		System.out.println();
	}
}
