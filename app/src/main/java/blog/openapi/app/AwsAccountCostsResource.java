package blog.openapi.app;

import jakarta.ws.rs.QueryParam;
import java.net.URI;

import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
	public AwsAccountCostsForAllAccounts getAwsCostsByAllAccounts(
			@QueryParam("start") String start,
			@QueryParam("end") String end) {
		logger.info("getAwsCostsByAllAccounts");
		logger.info("START: " + start);
		logger.info("END: " + end);
    TimePeriod timePeriod = new TimePeriod(
				new MonthYearSelection(Month.APRIL, 2023), new MonthYearSelection(Month.MAY, 2023));
    Set<AwsAccountCostForPeriod> internal = new HashSet<>(List.of(
        new AwsAccountCostForPeriod("12345689012", "Foo", 33.22, timePeriod),
        new AwsAccountCostForPeriod("1234568229012", "Bar", 88.22, timePeriod)
    ));
    Set<AwsAccountCostForPeriod> external = new HashSet<>(List.of(
        new AwsAccountCostForPeriod("143434845689012", "ExFoo", 33.22, timePeriod),
        new AwsAccountCostForPeriod("123456854545229012", "ExBar", 88.22, timePeriod)
    ));
		AwsAccountCostsForAllAccounts awsAccountCostsForAllAccounts = new AwsAccountCostsForAllAccounts(
			internal,
			external,
			timePeriod
		);
		return awsAccountCostsForAllAccounts;
	}

//	@POST
//	@Path("/widgets")
//	public Response addWidget(Widget newWidget) {
//		logger.info("New Widget added {}", newWidget);
//
//		return Response.created(
//				URI.create(String.format("/widgets/%d", newWidget.getId()))).build();
//	}

//	@GET
//	@Path("/widgets/{widgetId}")
//	public Response getWidgget(@PathParam("widgetId") int widgetId) {
//		logger.info("getWidget {}", widgetId);
//
//		if (widgetId == 1) {
//			return Response.ok().entity(new Widget(1, "Widget 1", "The first Widget", true)).build();
//		} else if (widgetId == 2) {
//			return Response.ok().entity(new Widget(2, "Widget 2", "The second Widget", true)).build();
//		} else if (widgetId == 3) {
//			return Response.ok().entity(new Widget(3, "Widget 3", "The third Widget", true)).build();
//		}
//
//		return Response.status(Response.Status.NOT_FOUND).build();
//	}

//	@PUT
//	@Path("/widgets/{widgetId}")
//	public Response putWidget(Widget updatedWidget, @PathParam("widgetId") int widgetId) {
//		logger.info("Update Widget Id {} with {}", widgetId, updatedWidget);
//
//		if (!updatedWidget.getId().equals(widgetId)) {
//			return Response.status(Response.Status.BAD_REQUEST).build();
//		}
//
//		return Response.noContent().build();
//	}

}
