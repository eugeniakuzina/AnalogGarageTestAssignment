package com.analog.service;

public class ZTest {

  public double test(double hypothesisMean, double sampleMean, int sampleSize) {
    // calculating standardDeviation
    // Question: not sure if I should be using sampleMean or hypothesisMean sampleMean here.
    // double standardDeviation = Math.sqrt(sampleSize * hypothesisMean * (1 - hypothesisMean));

    // TODO: this is Bernoulli only
    // TODO: no sampleSize in the formula
    double standardDeviation = Math
        .sqrt(sampleSize * sampleMean * (1 - sampleMean));

    // calculating zValue for Z-test
    double zValue = (sampleMean - hypothesisMean) / standardDeviation * Math
        .sqrt(sampleSize);

    return zValue;
  }

}
