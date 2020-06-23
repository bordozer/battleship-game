package com.bordozer.battleship.gameserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Battle {
    @NonNull
    private final Battlefield battlefield1;
    @NonNull
    private final Battlefield battlefield2;
}
