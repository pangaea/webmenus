package com.genesys.api.llm.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

public class OpenAiAdapter implements LLMAdapter {

    @Override
	public String makeLLMRequest(JsonNode node) {
        StringBuffer output = new StringBuffer();
		JsonNode msgs = node.get("messages");
		if (msgs.isArray()) {
			for (JsonNode msg : msgs) {
				String prompt = msg.get("content").asText();

				OpenAIClient client = OpenAIOkHttpClient.fromEnv();

				ResponseCreateParams params =
					ResponseCreateParams.builder().input(prompt).model("gpt-5.6").build();

				Response llmResponse = client.responses().create(params);
				llmResponse.output().stream()
					.flatMap(item -> item.message().stream())
					.flatMap(message -> message.content().stream())
					.flatMap(content -> content.outputText().stream())
					.forEach(outputText -> {
						System.out.println(outputText.text());
                        output.append(outputText.text());
					});

				break;
			}
		}
        return output.toString();
	}
}
