package blog.openapi.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAwsAccountCostRecord {
  @JsonProperty("Accounts")
  private Set<AwsAccount> awsAccounts;
  @JsonProperty("MonthYearSelection")
  private MonthYearSelection monthYearSelection;

  public CreateAwsAccountCostRecord() {

  }
  public CreateAwsAccountCostRecord(Set<AwsAccount> awsAccounts,
      MonthYearSelection monthYearSelection) {
    this.awsAccounts = awsAccounts;
    this.monthYearSelection = monthYearSelection;
  }

  public Set<AwsAccount> getAwsAccounts() {
    return awsAccounts;
  }

  public MonthYearSelection getMonthYearSelection() {
    return monthYearSelection;
  }

  public void setMonthYearSelection(MonthYearSelection monthYearSelection) {
    this.monthYearSelection = monthYearSelection;
  }
}
