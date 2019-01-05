package io.jochimsen.cahgameserver.game.card;

public class WhiteCard {
    private final long whiteCardId;
    private String text;

    public WhiteCard(final long whiteCardId, final String text) {
        this.whiteCardId = whiteCardId;
        this.text = text;
    }

    public long getWhiteCardId() {
        return whiteCardId;
    }

    public String getText() {
        return text;
    }
}
