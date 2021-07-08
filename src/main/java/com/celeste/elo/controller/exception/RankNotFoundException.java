package com.celeste.elo.controller.exception;

public class RankNotFoundException extends RuntimeException {

  public RankNotFoundException(final String error) {
    super(error);
  }

  public RankNotFoundException(final Throwable cause) {
    super(cause);
  }

  public RankNotFoundException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
