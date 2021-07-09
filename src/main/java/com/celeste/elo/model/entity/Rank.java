package com.celeste.elo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Rank {

  private final String name;
  private final String prefix;

  private final int elo;

  private Rank() {
    this(null, null, 0);
  }

}
