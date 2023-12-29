package blog.openapi.app.model;

import java.util.Set;

public class AwsAccountCosts {
  private Set<AwsAccount> accounts;
  private TimePeriod timePeriod;
  
  public AwsAccountCosts(Set<AwsAccount> accounts, TimePeriod timePeriod) {
    super();
    this.accounts = accounts;
    this.timePeriod = timePeriod;
  }
  public Set<AwsAccount> getAccounts() {
    return accounts;
  }
  public void setAccounts(Set<AwsAccount> accounts) {
    this.accounts = accounts;
  }
  public TimePeriod getTimePeriod() {
    return timePeriod;
  }
  public void setTimePeriod(TimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }
  
  
}
