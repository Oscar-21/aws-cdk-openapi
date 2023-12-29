package blog.openapi.app;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.YearMonth;
import jakarta.ws.rs.QueryParam;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import blog.openapi.app.model.AwsAccount;
import blog.openapi.app.model.AwsAccountCosts;
import blog.openapi.app.model.MonthYearSelection;
import blog.openapi.app.model.TimePeriod;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/")
public class AwsAccountCostsResource {

  private static Logger logger = LoggerFactory.getLogger(AwsAccountCostsResource.class);

  @GET
  @Path("/ping")
  @Produces(MediaType.TEXT_PLAIN)
  public Response ping() {
    logger.info("Ping invoked..");
    return Response.ok().entity("Pong...").build();
  }
  
  @GET
  @Path("/cost/accounts")
  public Response getAwsCostsByAllAccountsNoParams() {
    logger.info("getAwsCostsByAllAccounts");
//    logger.info("END: " + end);

    TimePeriod timePeriod = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2023),
        new MonthYearSelection(Month.MAY, 2023)
    );
    
    TimePeriod timePeriod2 = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2020),
        new MonthYearSelection(Month.MAY, 2020)
    );
    
    Set<AwsAccount> internal = new HashSet<>(List.of(
        new AwsAccount("12345689012", "Foo", 33.22, true, timePeriod),
        new AwsAccount("1234568229012", "Bar", 88.22, true, timePeriod)
    ));
    Set<AwsAccount> external = new HashSet<>(List.of(
        new AwsAccount("143434845689012", "ExFoo", 33.22, false, timePeriod2),
        new AwsAccount("123456854545229012", "ExBar", 88.22, false, timePeriod2)
    ));
    internal.addAll(external);
    
    
    AwsAccountCosts awsAccountCostsForAllAccounts =
        new AwsAccountCosts(internal, timePeriod);

    return Response.ok(awsAccountCostsForAllAccounts).build();
  }
  
  @GET
  @Path("/cost/accounts")
  public Response getAwsCostsByAllAccountsB(
      @QueryParam("start") String start, @QueryParam("end") String end) {
    logger.info("getAwsCostsByAllAccounts");
    logger.info("START: " + start);
    logger.info("END: " + end);

    YearMonth startYearMonth = parseDate(start);

    TimePeriod timePeriod = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2023),
        new MonthYearSelection(Month.MAY, 2023)
    );
    
    TimePeriod timePeriod2 = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2020),
        new MonthYearSelection(Month.MAY, 2020)
    );
    
    Set<AwsAccount> internal = new HashSet<>(List.of(
        new AwsAccount("12345689012", "Foo", 33.22, true, timePeriod),
        new AwsAccount("1234568229012", "Bar", 88.22, true, timePeriod)
    ));
    Set<AwsAccount> external = new HashSet<>(List.of(
        new AwsAccount("143434845689012", "ExFoo", 33.22, false, timePeriod2),
        new AwsAccount("123456854545229012", "ExBar", 88.22, false, timePeriod2)
    ));
    internal.addAll(external);
    
    
    AwsAccountCosts awsAccountCostsForAllAccounts =
        new AwsAccountCosts(internal, timePeriod);

    return Response.ok(awsAccountCostsForAllAccounts).build();
  }
  
  @GET
  @Path("/cost/accounts")
  public Response getAwsCostsByAllAccounts(@QueryParam("start") String start) {
    logger.info("getAwsCostsByAllAccounts");
    logger.info("START: " + start);
//    logger.info("END: " + end);

    YearMonth startYearMonth = parseDate(start);

    TimePeriod timePeriod = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2023),
        new MonthYearSelection(Month.MAY, 2023)
    );
    
    TimePeriod timePeriod2 = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2020),
        new MonthYearSelection(Month.MAY, 2020)
    );
    
    Set<AwsAccount> internal = new HashSet<>(List.of(
        new AwsAccount("12345689012", "Foo", 33.22, true, timePeriod),
        new AwsAccount("1234568229012", "Bar", 88.22, true, timePeriod)
    ));
    Set<AwsAccount> external = new HashSet<>(List.of(
        new AwsAccount("143434845689012", "ExFoo", 33.22, false, timePeriod2),
        new AwsAccount("123456854545229012", "ExBar", 88.22, false, timePeriod2)
    ));
    internal.addAll(external);
    
    
    AwsAccountCosts awsAccountCostsForAllAccounts =
        new AwsAccountCosts(internal, timePeriod);

    return Response.ok(awsAccountCostsForAllAccounts).build();
  }

  private YearMonth parseDate(String value) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM");
    try {
      return YearMonth.parse(value, dateFormatter);
    } catch (DateTimeParseException e) {
      throw new ClientErrorException(Status.BAD_REQUEST);
    }
  }

  @GET
  @Path("/cost/accounts")
  public Response getAwsCostsByAllAccounts(@QueryParam("start") String start,
      @QueryParam("end") String end) {
    logger.info("getAwsCostsByAllAccounts");
    logger.info("START: " + start);
    logger.info("END: " + end);

    YearMonth startYearMonth = parseDate(start);
    YearMonth endYearMonth = parseDate(end);

    TimePeriod timePeriod = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2023),
        new MonthYearSelection(Month.MAY, 2023)
    );
    
    TimePeriod timePeriod2 = new TimePeriod(
        new MonthYearSelection(Month.APRIL, 2020),
        new MonthYearSelection(Month.MAY, 2020)
    );
    
    Set<AwsAccount> internal = new HashSet<>(List.of(
        new AwsAccount("12345689012", "Foo", 33.22, true, timePeriod),
        new AwsAccount("1234568229012", "Bar", 88.22, true, timePeriod)
    ));
    Set<AwsAccount> external = new HashSet<>(List.of(
        new AwsAccount("143434845689012", "ExFoo", 33.22, false, timePeriod2),
        new AwsAccount("123456854545229012", "ExBar", 88.22, false, timePeriod2)
    ));
    internal.addAll(external);
    
    
    AwsAccountCosts awsAccountCostsForAllAccounts =
        new AwsAccountCosts(internal, timePeriod);

    return Response.ok(awsAccountCostsForAllAccounts).build();
  }

  private boolean withinRange(YearMonth start, YearMonth end, YearMonth target) {
    return !target.isBefore(start) && !target.isAfter(end);
  }
}
