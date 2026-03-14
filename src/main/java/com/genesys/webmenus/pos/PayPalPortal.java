package com.genesys.webmenus.pos;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*;

import com.genesys.views.ViewResponseWriter;

public class PayPalPortal extends HttpServlet
{
	public void init() throws ServletException
	{
	}
	
	/**
	 * Main entry point for all web requests
	 *
	 * @param request 			HttpServletRequest
	 * @param response 			HttpServletResponse
	 * @throws IOException
	 */
	public void service( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		try {
			// Extremely simple "REST" interface
			String resPath = request.getPathInfo();
			if( resPath != null )
			{
				if( resPath.equalsIgnoreCase("/createOrder") )
				{
					String accessToken = generateAccessToken();
					System.out.println("Access Token: " + accessToken);
					String orderId = createOrder(accessToken, response);
					System.out.println("Created Order ID: " + orderId);
					// After this, you should redirect the user to the approval URL 
					// provided in the response links to approve the payment
				}
				else if( resPath.equalsIgnoreCase("/order") )
				{
					// extract orderId, accessToken, reponse
					String orderId = null;
					String accessToken = null;
					captureOrder(orderId, accessToken, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	// Use PayPal Developer Dashboard to get your credentials for Sandbox/Live
    private static final String CLIENT_ID = "AU2TRC2m41gTinrJfNVas_8sFyqjC5EaUYjjgTc3sZvJk5Hs1U1mWbSNPz3lgl3rOzkeCPS0kfeSBaWX";
    private static final String CLIENT_SECRET = "EBaANfiMaanSblIinBj1p0lldHxVuCb0sWje5NSVElYfjVoUF06rQrXEjzVBlLAgNhiaS0hJV0IUq_EC";
    private static final String PAYPAL_API_BASE = "https://api-m.sandbox.paypal.com"; // Use "https://api-m.paypal.com" for live

    public static String generateAccessToken() throws Exception {
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        
        URL url = new URL(PAYPAL_API_BASE + "/v1/oauth2/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        conn.setDoOutput(true);

        String postData = "grant_type=client_credentials";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = postData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            // Parse the JSON response to extract the "access_token"
            // (A simple JSON parser like Gson or Jackson would be better here)
            String jsonResponse = response.toString();
            String accessToken = jsonResponse.split("\"access_token\":\"")[1].split("\"")[0];
            return accessToken;
        }
    }

	public static String createOrder(String accessToken, HttpServletResponse httpResponse) throws Exception {
        URL url = new URL(PAYPAL_API_BASE + "/v2/checkout/orders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setDoOutput(true);

        String jsonInputString = "{"
            + "\"intent\": \"CAPTURE\","
            + "\"purchase_units\": [{"
            + "\"amount\": {"
            + "\"currency_code\": \"USD\","
            + "\"value\": \"100.00\""
            + "},"
            + "\"description\": \"Green XL T-Shirt\""
            + "}],"
            + "\"application_context\": {"
            + "\"return_url\": \"https://example.com/returnUrl\","
            + "\"cancel_url\": \"https://example.com/cancelUrl\""
            + "}"
            + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            // Parse the JSON response to extract the "id" of the created order
            // (A simple JSON parser like Gson or Jackson would be better here)
            String jsonResponse = response.toString();
            String orderId = jsonResponse.split("\"id\":\"")[1].split("\"")[0];


			httpResponse.setContentType("text/json");
			httpResponse.setCharacterEncoding("utf-8");
			//response.setContentType("text/xml; charset=UTF-8");
			PrintWriter out = httpResponse.getWriter();
			out.write(jsonResponse.toCharArray());


            return orderId;
        }
    }

	// Conceptual Java example to capture payment
	public String captureOrder(String orderId, String accessToken, HttpServletResponse httpResponse) throws Exception {
		URL url = new URL("https://api-m.sandbox.paypal.com" + orderId + "/capture");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken); //
		conn.setRequestProperty("Content-Type", "application/json");
		// Perform POST request and read response
		// ... input stream handling ...
		return httpResponse.toString();
	}

}
