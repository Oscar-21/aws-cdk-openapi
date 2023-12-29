package blog.openapi.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Month;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthYearSelection {

  private Month month;
  private Integer year;

  public MonthYearSelection() {}

  public MonthYearSelection(Month month, Integer year) {
    this.month = month;
    this.year = year;
  }

  public void setMonth(Month month) {
    this.month = month;
  }

  public Month getMonth() {
    return month;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Integer getYear() {
    return year;
  }
}
