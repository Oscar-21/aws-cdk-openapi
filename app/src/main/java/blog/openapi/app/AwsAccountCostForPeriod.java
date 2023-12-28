package blog.openapi.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsAccountCostForPeriod {

  private String accountId;
  private String name;
  private Double cost;
  private TimePeriod timePeriod;

  public AwsAccountCostForPeriod() {}

  public AwsAccountCostForPeriod(String accountId, String name, Double cost,
      TimePeriod timePeriod) {
    this.accountId = accountId;
    this.name = name;
    this.cost = cost;
    this.timePeriod = timePeriod;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public TimePeriod getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(TimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }
}
