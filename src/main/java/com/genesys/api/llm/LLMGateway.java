package com.genesys.api.llm;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.genesys.api.auth.AndroidAttestation;
import com.genesys.api.llm.adapters.LLMAdapter;
import com.genesys.api.llm.adapters.OpenAiAdapter;
import com.genesys.util.ServletUtilities;

public class LLMGateway extends HttpServlet {

	private static final int CHALLENGE_BYTE_SIZE = 32;
	private static final String ATTESTATION_NONCE = "attestationNonce";
	private Boolean debugging = false;

    public void init() throws ServletException {
    }

	/**
	 * Main entry point for all web requests
	 *
	 * @param request 			HttpServletRequest
	 * @param response 			HttpServletResponse
	 * @throws IOException
	 */

	public void service( HttpServletRequest request, HttpServletResponse response )
	                     throws IOException, ServletException {

		// Extremely simple "REST" interface
		String resPath = request.getPathInfo();
		if( resPath == null ) {
			Handle_LLMRequest(request, response);
		}
		else {
			if( resPath.equalsIgnoreCase("/get_attestation_nonce") ) {
				Handle_GetAttestationNonce(request, response);
			}
			else if( resPath.equalsIgnoreCase("/query") ) {
				Handle_LLMRequest(request, response);
			}
		}
    }

	public void Handle_GetAttestationNonce( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		SecureRandom secureRandom = new SecureRandom();
        byte[] challengeBytes = new byte[CHALLENGE_BYTE_SIZE];
        secureRandom.nextBytes(challengeBytes);
        
        // Encode to URL-safe Base64 without padding
		String challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(challengeBytes);
		HttpSession session = request.getSession();
		session.setAttribute(ATTESTATION_NONCE, challenge);

		PrintWriter out = response.getWriter();
		out.write(Base64.getUrlEncoder().withoutPadding().encodeToString(challengeBytes));
	}

	public void Handle_LLMRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		JsonNode node = ServletUtilities.extractJsonBody(request);
		JsonNode auth = node.get("authentication");
		if (auth.get("type").asText().equals("android_attestation")) {
			HttpSession session = request.getSession();
			String attestationChallenge = (String)session.getAttribute(ATTESTATION_NONCE);
			byte[] challenge = attestationChallenge.getBytes(StandardCharsets.UTF_8);
			AndroidAttestation androidAttestation = new AndroidAttestation();
			if (debugging || androidAttestation.validate(auth.get("data").asText(), challenge)) {
				// Clear the challenge after a single use
				session.removeAttribute(ATTESTATION_NONCE);

				JsonNode msgs = node.get("messages");
				if (msgs.isArray() && !msgs.isEmpty()) {
					for (JsonNode msg : msgs) {
						String prompt = msg.get("content").asText();
						processLLMRequest(new OpenAiAdapter(), prompt, response);
						break;
					}
				} else {
					response.sendError(500, "Invalid format");
				}
			} else {
				response.sendError(500, "Authentication failed");
			}
		} else {
			response.sendError(500, "Invalid authentication type");
		}
	}

	private void processLLMRequest(LLMAdapter adapter, String prompt, HttpServletResponse response) {
		String res = adapter.makeLLMRequest(prompt);
		try {
			PrintWriter out = response.getWriter();
			response.addHeader("Content-Type", "text/json; charset=utf-8");
			out.write(res);
		} catch (Exception e) {}
	}
}
