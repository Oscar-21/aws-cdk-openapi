package blog.openapi.cdk.api.apigateway;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import blog.openapi.cdk.stacks.ApiStack;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Fn;
import software.amazon.awscdk.IResolvable;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.AssetApiDefinition;
import software.amazon.awscdk.services.apigateway.DomainNameOptions;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.InlineApiDefinition;
import software.amazon.awscdk.services.apigateway.SpecRestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.s3.assets.Asset;

public class BlogApiGatewayEndpoints {

	private HttpClient httpClient = HttpClient.newHttpClient();

	public void build(ApiStack stack) {
		Asset openAPIAsset = Asset.Builder.create(stack, "OpenAPIBlogAsset").path("../api/openapi.yaml").build();

		Map<String, String> transformMap = new HashMap<String, String>();
		transformMap.put("Location", openAPIAsset.getS3ObjectUrl());
		// Include the OpenAPI template as part of the API Definition supplied to API
		// Gateway
		IResolvable data = Fn.transform("AWS::Include", transformMap);

		InlineApiDefinition apiDefinition = AssetApiDefinition.fromInline(data);

		SpecRestApi restAPI = SpecRestApi.Builder.create(stack, "OpenAPIBlogRestAPI").apiDefinition(apiDefinition)
				.restApiName("OpenAPIBlogWidgetAPI").domainName(DomainNameOptions.builder()
						.domainName("awscostapi.nymbl.app").endpointType(EndpointType.REGIONAL)

				).endpointExportName("OpenAPIBlogWidgetRestApiEndpoint")
				.deployOptions(StageOptions.builder().stageName(stack.getStage()).build()).deploy(true).build();

		/**
		 * This will be the endpoint used to access the documentation.
		 */
		// godaddyapi.
		stack.setRestIdOutput(
				CfnOutput.Builder.create(stack, "OpenAPIBlogAPIRestIdOutput").value(restAPI.getRestApiId()).build());

	}

	private void bar(String restApiId) throws IOException, InterruptedException {
		HttpResponse<String> response = queryParamStoreEndpoint(createApplicationRequest(restApiId));

	}

	private HttpResponse<String> queryParamStoreEndpoint(Supplier<HttpRequest> requester)
			throws IOException, InterruptedException {
		return httpClient.send(requester.get(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
	}

	private Supplier<HttpRequest> createApplicationRequest(String restApiId) {
		return () -> HttpRequest.newBuilder().uri(generateURI(restApiId)).header("Content-Type", "application/json")
				.PUT(BodyPublishers.ofString("")).build();
	}

	private URI generateURI(String name) {
		return URI.create("baseURL " + URLEncoder.encode(name, StandardCharsets.UTF_8));
	}

}
