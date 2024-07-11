package org.example.services;

import lombok.Getter;
import lombok.Setter;
import org.example.models.GameState;

@org.springframework.stereotype.Service
@Getter
@Setter
public class Service {
    GameState game;

    public Service() {
        game = new GameState();
    }
}
