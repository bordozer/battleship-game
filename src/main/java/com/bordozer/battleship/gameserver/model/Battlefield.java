package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.util.List;

@Getter
@Setter
@ToString
public class Battlefield {
    private List<List<BattlefieldCell>> cells;
    @CheckForNull
    private PlayerMove lastShot;

    public Battlefield(final List<List<BattlefieldCell>> cells) {
        this.cells = cells;
    }
}
