package blog.openapi.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimePeriod {
  private MonthYearSelection start;
  private MonthYearSelection end;

  public TimePeriod() {

  }

  public TimePeriod(MonthYearSelection start, MonthYearSelection end) {
    this.start = start;
    this.end = end;
  }

  public TimePeriod(MonthYearSelection start) {
    this.start = start;
    this.end = start;
  }

  public MonthYearSelection getEnd() {
    return end;
  }

  public MonthYearSelection getStart() {
    return start;
  }

  public void setStart(MonthYearSelection start) {
    this.start = start;
  }

  public void setEnd(MonthYearSelection end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "TimePeriod{" + "start=" + start + ", end=" + end + '}';
  }
}
