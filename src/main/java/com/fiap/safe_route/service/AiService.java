package com.fiap.safe_route.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AiService {

    private final ChatLanguageModel model;

    public AiService(@Value("${spring.ai.ollama.base-url}") String baseUrl){
        this.model = OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName("tinyllama")
                .build();
    }
    public String gerarDicaSeguranca(Locale locale) {
        String prompt = locale.getLanguage().equals("pt")
                ? "Me dê uma dica rápida de segurança para situações de desastres naturais"
                : "Give me a quick safety tip for natural disaster situations";

        return model.generate(prompt);
    }

}
