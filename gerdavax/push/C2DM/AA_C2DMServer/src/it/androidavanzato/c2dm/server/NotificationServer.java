package it.androidavanzato.c2dm.server;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class NotificationServer {
	public static ArrayList<SimpleEntry<String, String>> devices = new ArrayList<SimpleEntry<String,String>>();
	
	public static void main(String[] args) {
		try {
			Server server = new Server(8099);
			
			ServletContextHandler context = new ServletContextHandler();
			context.setContextPath("/");
			
			context.addServlet(new ServletHolder(new RegisterServlet()), "/register");
			context.addServlet(new ServletHolder(new TriggerServlet()), "/trigger");
			
			server.setHandler(context);
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
