package it.androidavanzato.c2dm.server;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		NotificationServer.devices.add(new SimpleEntry<String, String>(request.getParameter("registration_id"), request.getParameter("device")));
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		System.out.println("Device: " + request.getParameter("registration_id"));
	}
}
