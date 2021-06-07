package com.analog.service;

public class ZTest {

  public double test(double hypothesis, double real, int sampleSize) {
    // calculating standardDeviation
    // Question: not sure if I should be using real mean or hypothesis mean here.
    // double standardDeviation = Math.sqrt(sampleSize * hypothesis * (1 - hypothesis));
    double standardDeviation = Math
        .sqrt(sampleSize * real * (1 - real));

    // calculating zValue for Z-test
    double zValue = (real - hypothesis) / standardDeviation * Math
        .sqrt(sampleSize);

    return zValue;
  }

}
