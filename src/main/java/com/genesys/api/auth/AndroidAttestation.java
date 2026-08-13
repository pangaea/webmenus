package com.genesys.api.auth;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.time.Instant;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.android.keyattestation.verifier.GoogleRevocationListKt;
import com.android.keyattestation.verifier.GoogleTrustAnchors;
import com.android.keyattestation.verifier.VerificationResult;
import com.android.keyattestation.verifier.Verifier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.protobuf.ByteString;

public class AndroidAttestation {

    public Boolean validate(String data, byte[] challenge) {
        try {
            List<X509Certificate> s = convertJsonToCerts(data);
            if (validateAttestation(s, challenge) != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
