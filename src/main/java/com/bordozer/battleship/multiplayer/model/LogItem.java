package com.bordozer.battleship.multiplayer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class LogItem {
    @NonNull
    private final LocalDateTime time = LocalDateTime.now();
    @NonNull
    private final String text;
}
