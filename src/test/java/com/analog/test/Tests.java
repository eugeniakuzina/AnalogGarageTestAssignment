package com.analog.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.analog.service.OutputParser;
import com.analog.service.YamlConfigParser;
import com.analog.service.ZTest;
import java.io.IOException;
import org.testng.annotations.Test;

public class Tests {

  // these values are hard-coded quantile values for confidence level of 99% for two-tailed test
  public static final double POSITIVE_QUANTILE = 2.575;
  public static final double NEGATIVE_QUANTILE = -2.575;

  @Test
  public void testNumOfSentMessages() throws IOException {

    int numMessagesInConfig = new YamlConfigParser().parse().getNum_messages();
    int numMessagesInMonitor = new OutputParser().getNumOfSentMessages();

    assertEquals(numMessagesInConfig, numMessagesInMonitor, "Wrong number of messages were sent. ");

  }

  @Test
  public void testExpectedRate() throws IOException {
    double meanTimeInConfig = new YamlConfigParser().parse().getSenders().get(0).getMean_time();
    double meanTimeInMonitor = new OutputParser().getExpectedTimePerMessage();

    assertEquals(meanTimeInConfig, meanTimeInMonitor,
        "Expected rate in monitor output is not what was set in config");
  }

  // This test implementation is for ONE sender only, for simplicity
  @Test
  public void testRateOfFailedMessages() throws IOException {

    double failedRateInConfig = new YamlConfigParser().parse().getSenders().get(0)
        .getFailure_rate();
    double failedRateInOutput = new OutputParser().getFailedMessagesRate();

    int numOfSentMessages = new OutputParser().getNumOfSentMessages();

    double zValue = new ZTest().test(failedRateInConfig, failedRateInOutput, numOfSentMessages);

    // TODO: use absolute value instead of two asserts
    assertTrue(zValue < POSITIVE_QUANTILE,
        "Calculated Z value is out of scope of Z score. "
            + "Real rate of failed messages is incorrect with 99% confidence. ZValue "
            + zValue + " boundary " + POSITIVE_QUANTILE);

    assertTrue(zValue > NEGATIVE_QUANTILE,
        "Calculated Z value is out of scope of Z score. "
            + "Real rate of failed messages is incorrect with 99% confidence. ZValue "
            + zValue + " boundary " + NEGATIVE_QUANTILE);
  }

  // This test implementation is for ONE sender only, for simplicity
  @Test
  public void testAverageTimePerMessage() throws IOException {

    double meanTimeInConfig = new YamlConfigParser().parse().getSenders().get(0).getMean_time();
    // double meanTimeInOutput = new OutputParser().getRealTimePerMessage();
    double meanTimeInOutput = new OutputParser().getExpectedTimePerMessage();

    int numOfSentMessages = new OutputParser().getNumOfSentMessages();

    double zValue = new ZTest().test(meanTimeInConfig, meanTimeInOutput, numOfSentMessages);

    assertTrue(zValue < POSITIVE_QUANTILE,
        "Calculated Z value is out of scope of Z score. "
            + "Real rate of failed messages is incorrect with 99% confidence. ZValue "
            + zValue + " boundary " + POSITIVE_QUANTILE);

    assertTrue(zValue > NEGATIVE_QUANTILE,
        "Calculated Z value is out of scope of Z score. "
            + "Real rate of failed messages is incorrect with 99% confidence. ZValue "
            + zValue + " boundary " + NEGATIVE_QUANTILE);
  }


}
