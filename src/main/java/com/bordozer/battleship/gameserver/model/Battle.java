package com.bordozer.battleship.gameserver.model;

import com.bordozer.battleship.gameserver.dto.battle.CurrentMove;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
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
    @NonNull
    private final CurrentMove currentMove;
}
