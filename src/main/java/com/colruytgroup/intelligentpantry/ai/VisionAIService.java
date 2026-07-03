package com.colruytgroup.intelligentpantry.ai;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class VisionAIService {

    private final ChatClient chatClient;

    public String askImage(MultipartFile file, String question) throws IOException {

        ByteArrayResource imageResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        Media imageMedia = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                .data(imageResource)
                .build();

        return chatClient.prompt()
                .user(user -> user
                        .text("""
                              Answer the user's question based only on the uploaded image.
                              Question:
                              %s
                              """.formatted(question))
                        .media(imageMedia)
                )
                .call()
                .content();
    }


}
