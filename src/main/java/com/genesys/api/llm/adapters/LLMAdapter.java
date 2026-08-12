package com.genesys.api.llm.adapters;

import com.fasterxml.jackson.databind.JsonNode;

public interface LLMAdapter {
    String makeLLMRequest(JsonNode node);
}
