package com.celeste.elo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rank {

  private final String name;
  private final String prefix;

  private final int elo;

}
