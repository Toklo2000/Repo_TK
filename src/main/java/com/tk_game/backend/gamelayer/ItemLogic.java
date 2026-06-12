package com.tk_game.backend.gamelayer;

import com.tk_game.backend.enume.StatType;

import java.util.Random;

public class ItemLogic {

  public static Random createRandom(long seed) {
    return new Random(seed);
  }

  public static StatType rollStatType(Random r) {
    StatType[] pool = {
      StatType.STR,
      StatType.DEX,
      StatType.CON,
      StatType.INT,
      StatType.LCK
    };

    return pool[r.nextInt(pool.length)];
  }

  public static int rollStatValue(Random r, int level) {
    double base = 5 + level * 2.2;
    double variance = r.nextDouble();

    double value = base + Math.pow(variance, 2.5) * base * 3;

    return Math.max(1, (int) value);
  }

  public static int rollStatCount(Random r) {
    double r1 = r.nextDouble();
    r1 = r1 * r1;

    if (r1 < 0.33) return 1;
    if (r1 < 0.66) return 2;
    return 3;
  }
}
