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

import org.jetbrains.annotations.NotNull;

import blog.openapi.cdk.stacks.ApiStack;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Fn;
import software.amazon.awscdk.IResolvable;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.AssetApiDefinition;
import software.amazon.awscdk.services.apigateway.DomainNameOptions;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.InlineApiDefinition;
import software.amazon.awscdk.services.apigateway.SpecRestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.CnameRecord;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.s3.assets.Asset;

public class BlogApiGatewayEndpoints {

	private HttpClient httpClient = HttpClient.newHttpClient();

	public void build(ApiStack stack) {
		
		IHostedZone hostedZone = HostedZone.fromLookup(stack, "ImportedHostedZone", HostedZoneProviderProps.builder()
		         .domainName("heoureialwed.com")
		         .build());
		
		 Certificate certificate = Certificate.Builder.create(stack, "Certificate")
           .domainName("openapiblog.heoureialwed.com")
           .validation(CertificateValidation.fromDns(hostedZone))
           .build();
		 
		DomainNameOptions domainNameOptions = DomainNameOptions.builder()
							.domainName("openapiblog.heoureialwed.com")
				    		.endpointType(EndpointType.REGIONAL)
				    		.certificate(certificate)
				    		.build();
		
		Asset openAPIAsset = Asset.Builder.create(stack, "OpenAPIBlogAsset").path("../app/api-docs/openapi.yaml").build();

		Map<String, String> transformMap = new HashMap<String, String>();
		transformMap.put("Location", openAPIAsset.getS3ObjectUrl());
		// Include the OpenAPI template as part of the API Definition supplied to API
		// Gateway
		IResolvable data = Fn.transform("AWS::Include", transformMap);

		InlineApiDefinition apiDefinition = AssetApiDefinition.fromInline(data);
	

		SpecRestApi restAPI = SpecRestApi.Builder.create(stack, "OpenAPIBlogRestAPI").apiDefinition(apiDefinition)
				.restApiName("OpenAPIBlogWidgetAPI")
				.domainName(domainNameOptions)
				.endpointExportName("OpenAPIBlogWidgetRestApiEndpoint")
				.deployOptions(StageOptions.builder().stageName(stack.getStage()).build()).deploy(true).build();

		/**
		 * This will be the endpoint used to access the documentation.
		 */
		// godaddyapi.
		stack.setRestIdOutput(
				CfnOutput.Builder.create(stack, "OpenAPIBlogAPIRestIdOutput").value(restAPI.getRestApiId()).build());

	}
	
}
