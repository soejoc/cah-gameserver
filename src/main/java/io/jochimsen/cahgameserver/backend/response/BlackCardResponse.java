package io.jochimsen.cahgameserver.backend.response;

import lombok.Data;

@Data
public class BlackCardResponse {
    private long blackCardId;
    private String text;
    private int blankCount;
}
