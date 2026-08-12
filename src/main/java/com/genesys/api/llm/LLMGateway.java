package com.genesys.api.llm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.android.keyattestation.verifier.GoogleRevocationListKt;
import com.android.keyattestation.verifier.GoogleTrustAnchors;
import com.android.keyattestation.verifier.VerificationResult;
import com.android.keyattestation.verifier.Verifier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesys.api.llm.adapters.LLMAdapter;
import com.genesys.api.llm.adapters.OpenAiAdapter;
import com.genesys.util.ServletUtilities;
import com.google.protobuf.ByteString;

public class LLMGateway extends HttpServlet {

	private static final int CHALLENGE_BYTE_SIZE = 32;
	private static final String ATTESTATION_NONCE = "attestationNonce";
	Boolean testing = true;

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
			try {
				List<X509Certificate> s = convertJsonToCerts(auth.get("data").asText());
				HttpSession session = request.getSession();
				String attestationChallenge = (String)session.getAttribute(ATTESTATION_NONCE);
				byte[] challenge = attestationChallenge.getBytes(StandardCharsets.UTF_8);
				if (testing || validateAttestation(s, challenge) != null) {
					// Clear the challenge after a single use
					session.removeAttribute(ATTESTATION_NONCE);
					handleLLMRequest(new OpenAiAdapter(), node, response);
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(500, e.getMessage());
			}
		}
	}

	private void handleLLMRequest(LLMAdapter adapter, JsonNode node, HttpServletResponse response) {
		String res = adapter.makeLLMRequest(node);
		try {
			PrintWriter out = response.getWriter();
			response.addHeader("Content-Type", "text/json; charset=utf-8");
			out.write(res);
		} catch (Exception e) {}
	}

	public static List<X509Certificate> convertJsonToCerts(String jsonArrayString) throws Exception {
        // 1. Parse the JSON array into a List of Base64 Strings
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> base64CertStrings = objectMapper.readValue(
            jsonArrayString, 
            new TypeReference<List<String>>() {}
        );

        List<X509Certificate> certificates = new ArrayList<>();
        
        // 2. Initialize the standard X.509 CertificateFactory
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        // 3. Decode and convert each item
        for (String certString : base64CertStrings) {
            // Clean up whitespaces, PEM headers, or footers if present
            String cleanedCert = certString
                    .replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedBytes = Base64.getDecoder().decode(cleanedCert);
            
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes)) {
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(inputStream);
                certificates.add(cert);
            }
        }

        return certificates;
    }

	public VerificationResult validateAttestation(
			List<X509Certificate> clientChain, byte[] expectedChallenge) throws Exception {

		// Create a verifier with default, Google-rooted trust anchors, revocation
		// info, and time source.
		Verifier verifier = new Verifier(
			GoogleTrustAnchors.INSTANCE,               // Trust anchors source
			GoogleRevocationListKt::getGoogleRevocationStatusFromWeb, // Revoked serials source (assuming a method reference or lambda)
			Instant::now                               // Time source
		);

		// Verify an attestation certificate chain
		VerificationResult result = verifier.verify(clientChain);

		// Handle the verification result
		if (result instanceof VerificationResult.Success) {
			VerificationResult.Success success = (VerificationResult.Success) result;
			// Access verified information
			// var publicKey = success.getPublicKey();
			// var securityLevel = success.getSecurityLevel();
			// var verifiedBootState = success.getVerifiedBootState();
			// var deviceInformation = success.getDeviceInformation();
			ByteString challenge = success.getChallenge();
			if (Arrays.compare(expectedChallenge, challenge.toByteArray()) == 0) {
				return result;
			}
		} else if (result instanceof VerificationResult.ChallengeMismatch) {
			// Handle challenge mismatch
		} else if (result instanceof VerificationResult.PathValidationFailure) {
			// Handle validation failure
		} else if (result instanceof VerificationResult.ChainParsingFailure) {
			// Handle parsing failure
		} else if (result instanceof VerificationResult.ExtensionParsingFailure) {
			// Handle extension parsing issues
		} else if (result instanceof VerificationResult.ConstraintViolation) {
			// Handle constraint violations
		}
		throw new Exception("Attestation Failed");
	}
}
