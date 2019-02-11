package io.jochimsen.cahgameserver.backend.response;

import lombok.Data;

@Data
public class HashResponse<T> {
    private T data;
    private int hash;
}
