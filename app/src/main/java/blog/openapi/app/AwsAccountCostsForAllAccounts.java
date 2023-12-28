package blog.openapi.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsAccountCostsForAllAccounts {

  private Set<AwsAccountCostForPeriod> internal;
  private Set<AwsAccountCostForPeriod> external;
  private TimePeriod timePeriod;

  public AwsAccountCostsForAllAccounts() {

  }

  public AwsAccountCostsForAllAccounts(Set<AwsAccountCostForPeriod> internal,
      Set<AwsAccountCostForPeriod> external, TimePeriod timePeriod) {
    this.internal = internal;
    this.external = external;
    this.timePeriod = timePeriod;
  }

  public void setExternal(Set<AwsAccountCostForPeriod> external) {
    this.external = external;
  }

  public void setInternal(Set<AwsAccountCostForPeriod> internal) {
    this.internal = internal;
  }

  public void setTimePeriod(TimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }

  public Set<AwsAccountCostForPeriod> getExternal() {
    return external;
  }

  public Set<AwsAccountCostForPeriod> getInternal() {
    return internal;
  }

  public TimePeriod getTimePeriod() {
    return timePeriod;
  }
}
