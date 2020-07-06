package com.bordozer.battleship.multiplayer.converter;

import com.bordozer.battleship.multiplayer.dto.battle.LogDto;
import com.bordozer.battleship.multiplayer.model.LogItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogConverter {

    public static List<LogDto> getLogs(final List<LogItem> logs) {
        return logs.stream()
                .sorted((l1, l2) -> l2.getTime().compareTo(l1.getTime()))
                .map(log -> LogDto.builder()
                        .time(log.getTime())
                        .text(getText(log))
                        .build())
                .collect(Collectors.toList());
    }

    private static String getText(final LogItem log) {
        return String.format("%s:%s:%s %s",
                log.getTime().getHour(),
                log.getTime().getMinute(),
                log.getTime().getSecond(),
                log.getText()
        );
    }
}
