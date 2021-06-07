package com.analog.model;

import java.util.List;

public class YamlConfig {

  private int num_messages;
  private float report_period;
  private List<Sender> senders;

  public int getNum_messages() {
    return num_messages;
  }

  public void setNum_messages(int num_messages) {
    this.num_messages = num_messages;
  }

  public float getReport_period() {
    return report_period;
  }

  public void setReport_period(float report_period) {
    this.report_period = report_period;
  }

  public List<Sender> getSenders() {
    return senders;
  }

  public void setSenders(List<Sender> senders) {
    this.senders = senders;
  }
}
