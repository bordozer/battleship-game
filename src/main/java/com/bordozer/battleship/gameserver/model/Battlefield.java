package com.bordozer.battleship.gameserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Battlefield {
    @CheckForNull
    private List<List<BattlefieldCell>> cells;
}
