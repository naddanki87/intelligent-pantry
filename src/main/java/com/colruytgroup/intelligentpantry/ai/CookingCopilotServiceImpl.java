package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.request.CookingCopilotRequest;
import com.colruytgroup.intelligentpantry.dto.response.CookingCopilotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookingCopilotServiceImpl implements  CookingCopilotService {

    private final ChatClient chatClient;


    @Override
    public CookingCopilotResponse ask(CookingCopilotRequest request) {

        String prompt = """
                You are a professional cooking assistant.

                Recipe:

                %s

                User Question:

                %s

                Rules:

                1. Give practical cooking advice.
                2. Suggest substitutions when needed.
                3. Keep response concise.
                4. Explain cooking techniques clearly.
                5. If ingredient is unavailable,
                   suggest alternatives.

                Return only the answer.
                """
                .formatted(
                        request.recipeName(),
                        request.question());
        String answer =
                chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();

        return new CookingCopilotResponse(
                request.recipeName(),
                answer
        );
    }
}
