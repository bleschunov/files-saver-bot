package com.bleschunov.service.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author Bleschunov Dmitry
 */
@RequiredArgsConstructor
public enum ServiceCommand {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");

    private final String command;


    @Override
    public String toString() {
        return command;
    }

    public boolean equals(String cmd) {
        return this.toString().equals(cmd);
    }
}
