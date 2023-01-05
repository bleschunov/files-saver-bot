package com.bleschunov.service.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author Bleschunov Dmitry
 */
@RequiredArgsConstructor
public enum LinkType {
    GET_DOC("file/get-doc"),
    GET_PHOTO("file/get-photo");

    private final String link;

    @Override
    public String toString() {
        return link;
    }
}
