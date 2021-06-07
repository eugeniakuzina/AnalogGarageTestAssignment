package com.analog.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testng.Assert;

public class OutputParser {

  private final File output = new File("src/test/resources/output.txt");

  /**
   * Parses monitor output and returns value after the 'Expected rate' converted to time per
   * message
   *
   * @return double value
   * @throws IOException if file is not found
   */
  public double getExpectedTimePerMessage() throws IOException {
    Pattern patten = Pattern.compile("(?<=Expected rate) (\\d*\\.?\\d*)");
    Matcher matcher = patten.matcher(Files.readString(output.toPath(), StandardCharsets.US_ASCII));

    Assert.assertTrue(matcher.find(), "Error: Output file doesn't contain expected rate ");

    double expectedRate = Double.parseDouble(matcher.group(1));

    // since monitor returns rate and we need target averageTimeProMessage, using this calculation;
    BigDecimal expectedRateDecimal = BigDecimal.valueOf(1 / expectedRate)
        .setScale(2, RoundingMode.HALF_UP);
    return expectedRateDecimal.doubleValue();

  }

  /**
   * Parses monitor output and returns real sent messages rate converted to time per message
   *
   * @return double value
   * @throws IOException if file not found
   */
  public double getRealTimePerMessage() throws IOException {
    Pattern patten = Pattern.compile("(?<=rate: )(\\d*\\.?\\d*)");
    Matcher matcher = patten.matcher(Files.readString(output.toPath(), StandardCharsets.US_ASCII));

    Assert.assertTrue(matcher.find(), "Error: Output file doesn't contain any rate messages info ");

    List<String> result = new ArrayList<>();

    while (matcher.find()) {
      result.add(matcher.group());
    }

    // returning the last value which is the last line, representing data of all messages sent
    // since monitor returns rate and we need real averageTimeProMessage, using this calculation
    BigDecimal expectedRateDecimal = BigDecimal
        .valueOf(1 / Double.parseDouble(result.get(result.size() - 1)))
        .setScale(2, RoundingMode.HALF_UP);
    return expectedRateDecimal.doubleValue();
  }

  /**
   * Parses monitor output and returns real number of sent messages
   *
   * @return int value
   * @throws IOException if file not found
   */
  public int getNumOfSentMessages() throws IOException {
    Pattern patten = Pattern.compile("(?<=MONITOR sent: )(\\d+)");
    Matcher matcher = patten.matcher(Files.readString(output.toPath(), StandardCharsets.US_ASCII));

    Assert.assertTrue(matcher.find(), "Error: Output file doesn't contain any sent messages info ");

    List<String> result = new ArrayList<>();

    while (matcher.find()) {
      result.add(matcher.group());
    }

    // returning the last value which is the last line, representing data of all messages sent
    return Integer.parseInt(result.get(result.size() - 1));

  }

  /**
   * Parses monitor output and returns real number of failed messages
   *
   * @return int value
   * @throws IOException if file not found
   */
  public int getNumOfFailedMessages() throws IOException {
    Pattern patten = Pattern.compile("(?<=failed: )(\\d+)");
    Matcher matcher = patten.matcher(Files.readString(output.toPath(), StandardCharsets.US_ASCII));

    Assert
        .assertTrue(matcher.find(), "Error: Output file doesn't contain any failed messages info ");

    List<String> result = new ArrayList<>();

    while (matcher.find()) {
      result.add(matcher.group());
    }

    // returning the last value which is the last line, representing data of all messages sent
    return Integer.parseInt(result.get(result.size() - 1));

  }

  /**
   * Calculates real failed message rate
   *
   * @return double value
   * @throws IOException if file not found
   */
  public double getFailedMessagesRate() throws IOException {
    return getNumOfFailedMessages() * 1.0 / getNumOfSentMessages();
  }

}
