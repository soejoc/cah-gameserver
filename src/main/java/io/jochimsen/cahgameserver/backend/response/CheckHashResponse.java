package io.jochimsen.cahgameserver.backend.response;

import lombok.Data;

@Data
public class CheckHashResponse {
    private boolean hashEqual;
}
