package com.genesys.webmenus.pos;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesys.api.RepositoryResource;
import com.genesys.webmenus.MenuOrderBean;
import com.genesys.webmenus.OrderItem;

public class PayPalPortal extends HttpServlet
{
    // Use PayPal Developer Dashboard to get your credentials for Sandbox/Live
    //private static final String CLIENT_ID = "AU2TRC2m41gTinrJfNVas_8sFyqjC5EaUYjjgTc3sZvJk5Hs1U1mWbSNPz3lgl3rOzkeCPS0kfeSBaWX";
    //private static final String CLIENT_SECRET = "EBaANfiMaanSblIinBj1p0lldHxVuCb0sWje5NSVElYfjVoUF06rQrXEjzVBlLAgNhiaS0hJV0IUq_EC";
    private static final String PAYPAL_API_BASE = "https://api-m.sandbox.paypal.com"; // Use "https://api-m.paypal.com" for live
    String clientId, clientSecret;

    MenuOrderBean menuOrderBean;
    String accessToken;

	public void init() throws ServletException
	{
        try {
            accessToken = generateAccessToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
            HttpSession beanSessionContext = request.getSession(); // Session Level Scope
			synchronized (beanSessionContext){
				menuOrderBean = (MenuOrderBean) beanSessionContext.getAttribute("menuOrderBean");
				if( menuOrderBean == null ){
					try{
						menuOrderBean = (MenuOrderBean) java.beans.Beans.instantiate(this.getClass().getClassLoader(), "com.genesys.webmenus.MenuOrderBean");
					}
					catch (Exception exc){
						throw new ServletException("Cannot create bean of class com.genesys.session.ClientSessionBean", exc);
					}
					beanSessionContext.setAttribute("menuOrderBean", menuOrderBean);
				}
			}

			// Extremely simple "REST" interface
			String resPath = request.getPathInfo();
			if( resPath != null )
			{
                // Extract JSON from POST body
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = request.getReader();
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } finally {
                    reader.close();
                }

                // Parse settings of payment config based on index from post
                ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(sb.toString());
				int payment_index = node.get("payment_index").asInt();
                clientId = menuOrderBean.queryPmConfig(payment_index, "CLIENT_ID");
				clientSecret = menuOrderBean.queryPmConfig(payment_index, "CLIENT_SECRET");
				if( resPath.equalsIgnoreCase("/createOrder") )
				{
					//String orderId = createOrder(accessToken, response);
                    createOrder(accessToken, response);
				}
				else if( resPath.startsWith("/order/") )
				{
                    String patternString = "/order/(.+)/capture";
                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(resPath);
                    // Check if the pattern is found in the string
                    if (matcher.find()) {
                        // group(1) refers to the first capturing group (the actual ID number)
                        String orderId = matcher.group(1); 
                        captureOrder(orderId, accessToken, response);
                    } else {
                        System.out.println("No ID found.");
                    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private String makeRequest(String accessToken, String contentType, String path, String reqString) throws Exception {
        URL url = new URL(PAYPAL_API_BASE + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", contentType);
        if (accessToken == null) {
            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        } else {
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = reqString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public String generateAccessToken() throws Exception {
        String postData = "grant_type=client_credentials";
        String jsonResponse = makeRequest(null, "application/x-www-form-urlencoded", "/v1/oauth2/token", postData);
        return jsonResponse.split("\"access_token\":\"")[1].split("\"")[0];
    }

    // private JSONObject createJsonObject(Map<String, Object> props) {
    //     JSONObject jsonObj = new JSONObject();
    //     for (String key : props.keySet()) {
    //         jsonObj.put(key, props.get(key));
    //     }
    //     return jsonObj;
    // }

	public String createOrder(String accessToken, HttpServletResponse httpResponse) throws Exception {
        String jsonInputString = "{}";
        try{
            JSONObject jsonOrder = new JSONObject();
            jsonOrder.put("intent", "CAPTURE");
            JSONArray jsonPurchaseUnits = new JSONArray();
            JSONObject jsonPurchaseUnit = new JSONObject();
            JSONObject jsonAmount = new JSONObject();
            jsonAmount.put("currency_code", "USD");
            jsonAmount.put("value", menuOrderBean.getTotal());
            jsonPurchaseUnit.put("amount", jsonAmount);
            jsonPurchaseUnits.put(jsonPurchaseUnit);

            // for( int i = 0; i < menuOrderBean.itemCount(); i++ ) {
            //     OrderItem item = menuOrderBean.getItemByIndex(i);
            //     if (item != null) {
            //         // Calculate amount
            //         JSONObject jsonAmount = new JSONObject();
            //         jsonAmount.put("currency_code", "USD");
            //         jsonAmount.put("value", item.getPrice());

            //         // Add each item
            //         JSONObject jsonPurchaseUnit = new JSONObject();
            //         jsonPurchaseUnit.put("amount", jsonAmount);
            //         jsonPurchaseUnit.put("description", item.getName());
            //         jsonPurchaseUnits.put(jsonPurchaseUnit);
            //     }
            // }

            // Add items array
            jsonOrder.put("purchase_units", jsonPurchaseUnits);
            JSONObject jsonApplicationContext = new JSONObject();
            jsonApplicationContext.put("return_url", "https://example.com/returnUrl");
            jsonApplicationContext.put("cancel_url", "https://example.com/cancelUrl");
            jsonOrder.put("application_context", jsonApplicationContext);
            jsonInputString = jsonOrder.toString();
        } catch (JSONException e) {
            // Log this
        }

        // Post order to paypal
        String jsonResponse = makeRequest(accessToken, "application/json", "/v2/checkout/orders", jsonInputString);
        String orderId = jsonResponse.split("\"id\":\"")[1].split("\"")[0];
        httpResponse.setContentType("text/json");
        httpResponse.setCharacterEncoding("utf-8");
        PrintWriter out = httpResponse.getWriter();
        out.write(jsonResponse.toCharArray());
        return orderId;
    }

	// Conceptual Java example to capture payment
	public String captureOrder(String orderId, String accessToken, HttpServletResponse httpResponse) throws Exception {

        String jsonInputString = "{}";
        try{
            JSONObject jsonOrder = new JSONObject();
            jsonOrder.put("id", orderId);
            JSONArray jsonPurchaseUnits = new JSONArray();
            JSONObject jsonPurchaseUnit = new JSONObject();

            JSONObject jsonPayments = new JSONObject();

            JSONArray jsonCaptures = new JSONArray();
            jsonPayments.put("captures", jsonCaptures);

            JSONObject jsonAmount = new JSONObject();
            jsonAmount.put("currency_code", "USD");
            jsonAmount.put("value", menuOrderBean.getTotal());
            JSONObject jsonCapture = new JSONObject();
            jsonCapture.put("amount", jsonAmount);

            jsonCaptures.put(jsonCapture);
            jsonPurchaseUnit.put("payments", jsonPayments);

            //jsonPurchaseUnit.put("amount", jsonAmount);
            jsonPurchaseUnits.put(jsonPurchaseUnit);

            // Add items array
            jsonOrder.put("purchase_units", jsonPurchaseUnits);
            JSONObject jsonApplicationContext = new JSONObject();
            jsonApplicationContext.put("return_url", "https://example.com/returnUrl");
            jsonApplicationContext.put("cancel_url", "https://example.com/cancelUrl");
            jsonOrder.put("application_context", jsonApplicationContext);
            jsonInputString = jsonOrder.toString();
        } catch (JSONException e) {
            throw new ServletException(e.getMessage(), e);
        }

		// URL url = new URL("https://api-m.sandbox.paypal.com" + orderId + "/capture");
		// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// conn.setRequestMethod("POST");
		// conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		// conn.setRequestProperty("Content-Type", "application/json");
		// Perform POST request and read response
        // try (OutputStream os = conn.getOutputStream()) {
        //     byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
        //     os.write(input, 0, input.length);
        // }

		// ... input stream handling ...
		//return httpResponse.toString();

        // Post order to paypal
        String jsonResponse = makeRequest(accessToken, "application/json", "/v2/checkout/orders/" + orderId + "/capture", jsonInputString);
        httpResponse.setContentType("text/json");
        httpResponse.setCharacterEncoding("utf-8");
        PrintWriter out = httpResponse.getWriter();
        out.write(jsonResponse.toCharArray());
        return orderId;
	}

}
