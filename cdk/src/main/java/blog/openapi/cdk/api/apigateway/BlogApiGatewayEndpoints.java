package blog.openapi.cdk.api.apigateway;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Fn;
import software.amazon.awscdk.IResolvable;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.AssetApiDefinition;
import software.amazon.awscdk.services.apigateway.InlineApiDefinition;
import software.amazon.awscdk.services.apigateway.SpecRestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.s3.assets.Asset;

public class BlogApiGatewayEndpoints {

	public CfnOutput build(Stack stack, String stage) {
		Asset openAPIAsset = Asset.Builder.create(stack, "OpenAPIBlogAsset")
				.path("../api/openapi.yaml").build();

		Map<String, String> transformMap = new HashMap<String, String>();
		transformMap.put("Location", openAPIAsset.getS3ObjectUrl());
		// Include the OpenAPI template as part of the API Definition supplied to API
		// Gateway
		IResolvable data = Fn.transform("AWS::Include", transformMap);

		InlineApiDefinition apiDefinition = AssetApiDefinition.fromInline(data);

		SpecRestApi restAPI = SpecRestApi.Builder.create(stack, "OpenAPIBlogRestAPI")
				.apiDefinition(apiDefinition)
				.restApiName("OpenAPIBlogWidgetAPI")
				.endpointExportName("OpenAPIBlogWidgetRestApiEndpoint")
				.deployOptions(StageOptions.builder().stageName(stage).build())
				.deploy(true)
				.build();

		/**
		 * This will be the endpoint used to access the documentation.
		 */
		return CfnOutput.Builder.create(stack, "OpenAPIBlogAPIRestIdOutput")
				.value(restAPI.getRestApiId())
				.build();

	
	}

}
