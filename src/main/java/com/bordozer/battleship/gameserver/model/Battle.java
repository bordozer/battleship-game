package com.bordozer.battleship.gameserver.model;

import com.bordozer.battleship.gameserver.dto.battle.PlayerType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Battle {
    @NonNull
    private final Battlefield battlefield1;
    @NonNull
    private final Battlefield battlefield2;
    @NonNull
    private final List<LogItem> logs;
    @CheckForNull
    private PlayerType currentMove;

    public Battle addLog(final LogItem logItem) {
        logs.add(logItem);
        return this;
    }

    public void addLogs(final List<LogItem> logItems) {
        logs.addAll(logItems);
    }

    public List<LogItem> getLogs() {
        return Collections.unmodifiableList(logs);
    }
}
