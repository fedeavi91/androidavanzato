package it.androidavanzato.c2dm.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.io.AbstractBuffer;
import org.eclipse.jetty.io.ByteArrayBuffer;

@SuppressWarnings("serial")
public class TriggerServlet extends HttpServlet {
	private static final String AUTH = "METTI_QUI_LA_GOOGLE_AUTH";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String index = request.getParameter("index");

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h1>Gestione notifiche C2DM</h1><hr/>");

		if (index == null) {
			out.println("<ul>");
			for (int i = 0; i < NotificationServer.devices.size(); i++) {
				out.println("<li><a href=\"/trigger?index=" + i + "\">" + NotificationServer.devices.get(i).getValue() + "</a></li>");
			}
			out.println("</ul>");
		} else {
			out.println("Notifica inviata!!!");
			try {
				sendToCloud(Integer.parseInt(index));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		out.println("</body></html>");
	}

	private void sendToCloud(int index) throws Exception {
		HttpClient client = new HttpClient();
		client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		client.start();

		ContentExchange exchange = new ContentExchange() {

		};

		exchange.setMethod("POST");
		exchange.setURL("http://android.apis.google.com/c2dm/send");
		exchange.addRequestHeader("Authorization", "GoogleLogin auth=" + AUTH);
		StringBuffer formBuffer = new StringBuffer();
		formBuffer.append("registration_id=" + NotificationServer.devices.get(index).getKey());
		formBuffer.append("&collapse_key=" + Long.toHexString(System.currentTimeMillis()));
		//formBuffer.append("&data.toast=Sveglia!");
		AbstractBuffer content = new ByteArrayBuffer(formBuffer.toString().getBytes("UTF-8"));
		exchange.setRequestContent(content);
		exchange.setRequestContentType("application/x-www-form-urlencoded");

		client.send(exchange);

	}

}
