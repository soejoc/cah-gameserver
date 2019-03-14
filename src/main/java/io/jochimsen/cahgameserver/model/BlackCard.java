package io.jochimsen.cahgameserver.model;

import lombok.Value;

@Value
public class BlackCard {
    private final long blackCardId;
    private final String text;
    private final int blankCount;
}
