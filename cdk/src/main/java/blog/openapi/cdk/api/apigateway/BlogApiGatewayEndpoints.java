package blog.openapi.cdk.api.apigateway;

import java.util.HashMap;
import java.util.Map;

import blog.openapi.cdk.stacks.ApiStack;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Fn;
import software.amazon.awscdk.IResolvable;
import software.amazon.awscdk.services.apigateway.AssetApiDefinition;
import software.amazon.awscdk.services.apigateway.DomainNameOptions;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.InlineApiDefinition;
import software.amazon.awscdk.services.apigateway.SpecRestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.targets.ApiGateway;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.s3.assets.Asset;

public class BlogApiGatewayEndpoints {

  public void build(ApiStack stack) {

    IHostedZone hostedZone =
        HostedZone.fromLookup(stack, ApiStack.Config.LogicalIds.ImportedHostedZone,
            HostedZoneProviderProps.builder().domainName(ApiStack.Config.DNS.ROOT).build());

    stack.setHostedZone(hostedZone);

    Certificate certificate =
        Certificate.Builder.create(stack, ApiStack.Config.LogicalIds.APIGatewayCertificate)
            .domainName(ApiStack.Config.DNS.apiRecord + "." + ApiStack.Config.DNS.ROOT)
            .validation(CertificateValidation.fromDns(stack.getHostedZone())).build();

    DomainNameOptions domainNameOptions = DomainNameOptions.builder()
        .domainName(ApiStack.Config.DNS.apiRecord + "." + ApiStack.Config.DNS.ROOT)
        .endpointType(EndpointType.REGIONAL).certificate(certificate).build();

    Asset openAPIAsset = Asset.Builder.create(stack, ApiStack.Config.LogicalIds.OpenAPIBlogAsset)
        .path("../app/api-docs/openapi.yaml").build();

    Map<String, String> transformMap = new HashMap<String, String>();
    transformMap.put("Location", openAPIAsset.getS3ObjectUrl());
    // Include the OpenAPI template as part of the API Definition supplied to API
    // Gateway
    IResolvable data = Fn.transform("AWS::Include", transformMap);

    InlineApiDefinition apiDefinition = AssetApiDefinition.fromInline(data);


    SpecRestApi restAPI =
        SpecRestApi.Builder.create(stack, ApiStack.Config.LogicalIds.OpenAPIBlogRestAPI)
            .apiDefinition(apiDefinition).restApiName("OpenAPIBlogWidgetAPI")
            .endpointExportName("OpenAPIBlogWidgetRestApiEndpoint").domainName(domainNameOptions)
            .deployOptions(StageOptions.builder().stageName(stack.getStage()).build()).deploy(true)
            .build();


    ARecord aRecord =
        ARecord.Builder.create(stack, ApiStack.Config.LogicalIds.OpenAPIDNSForAPIGateway)
            .zone(stack.getHostedZone()).recordName(ApiStack.Config.DNS.apiRecord)
            .target(RecordTarget.fromAlias(new ApiGateway(restAPI))).build();


    // restAPI.addDomainName("apiDNS", domainNameOptions);

    /**
     * This will be the endpoint used to access the documentation.
     */
    // godaddyapi.
    stack.setRestIdOutput(CfnOutput.Builder.create(stack, "OpenAPIBlogAPIRestIdOutput")
        .value(restAPI.getRestApiId()).build());

  }

}
