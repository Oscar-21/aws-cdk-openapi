package blog.openapi.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsAccount {

  private String accountId;

  private String name;

  private Double cost;
  private Boolean isInternal;
  private TimePeriod timePeriod;

  public AwsAccount() {}

  public AwsAccount(String accountId, String name, Double cost, Boolean isInternal,
      TimePeriod timePeriod) {
    this.accountId = accountId;
    this.name = name;
    this.cost = cost;
    this.isInternal = isInternal;
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

  public void setIsInternal(Boolean isInternal) {
    this.isInternal = isInternal;
  }

  public Boolean getIsInternal() {
    return isInternal;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public void setTimePeriod(TimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }

  public TimePeriod getTimePeriod() {
    return timePeriod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AwsAccount that = (AwsAccount) o;
    return Objects.equals(accountId, that.accountId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId);
  }
}
