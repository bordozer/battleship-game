package com.bordozer.battleship.multiplayer.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHelper {

    String getPlayerId(HttpServletRequest request);

    void addPlayerCookies(HttpServletResponse response, String playerId);
}
