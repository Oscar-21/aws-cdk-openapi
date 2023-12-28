package blog.openapi.cdk.stacks;


import blog.openapi.cdk.api.apigateway.BlogApiGatewayEndpoints;
import blog.openapi.cdk.api.docssite.ApiDocsS3Resources;
import blog.openapi.cdk.api.lambda.BlogLambda;
import software.constructs.Construct;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.route53.IHostedZone;

public class ApiStack extends Stack {

  public static class Config {
    public static class DNS {
      public static String ROOT = "heoureialwed.com";
      public static String apiRecord = "openapiapi";
      public static String docsRecord = "heoureialwed.com";
    }
    public static class LogicalIds {
      public static String ImportedHostedZone = "ImportedHostedZone";
      public static String APIGatewayCertificate = "APIGatewayCertificate";
      public static String CloudfronDistubutionCertificate = "CloudfronDistubutionCertificate";
      public static String OpenAPIBlogAsset = "OpenAPIBlogAsset";
      public static String OpenAPIBlogRestAPI = "OpenAPIBlogRestAPI";
      public static String OpenAPIDNSForAPIGateway = "OpenAPIDNSForAPIGateway";
      public static String OpenAPIBlogLambda = "OpenAPIBlogLambda";
      public static String APILambda = "APILambda";
      public static String OpenAPIBlogAPIBucket = "OpenAPIBlogAPIBucket";
    }
  }


  private CfnOutput restIdOutput;

  private CfnOutput bucketNameOutput;

  private CfnOutput cloudFrontURLOutput;

  private CfnOutput cloudFrontDistributionIdOutput;
  private final String stage;
  private IHostedZone hostedZone;

  public void setHostedZone(IHostedZone hostedZone) {
    this.hostedZone = hostedZone;
  }

  public IHostedZone getHostedZone() {
    return hostedZone;
  }

  public CfnOutput getRestIdOutput() {
    return restIdOutput;
  }

  public CfnOutput getBucketNameOutput() {
    return bucketNameOutput;
  }

  public CfnOutput getCloudFrontURLOutput() {
    return cloudFrontURLOutput;
  }

  public CfnOutput getCloudFrontDistributionIdOutput() {
    return cloudFrontDistributionIdOutput;
  }

  public ApiStack(final Construct scope, final String id, String stage) {
    this(scope, id, stage, null);
  }

  public ApiStack(final Construct scope, final String id, String stage,
      /* , String webBucketName Bucket webBucket, */ final StackProps props) {
    super(scope, id, props);
    this.stage = stage;


    ////////////////////////////////////////////////////////////////////
    // //
    // Create the Rest Enabled Lambda via the Quarkus framework //
    // //
    ///////////////////////////////////////////////////////////////////
    BlogLambda blogLambda = new BlogLambda();
    blogLambda.buildImage(this);


    ////////////////////////////////////////////////////////////////////
    // //
    // Create the API Gateway Endpoints from the OpenAPI Document //
    // //
    ///////////////////////////////////////////////////////////////////
    BlogApiGatewayEndpoints apiGatewayEndpoints = new BlogApiGatewayEndpoints();
    apiGatewayEndpoints.build(this);

    ////////////////////////////////////////////////////////////////////
    // //
    // Create the API Documentation Website //
    // //
    ///////////////////////////////////////////////////////////////////
    ApiDocsS3Resources apiDocsS3Resources = new ApiDocsS3Resources();
    apiDocsS3Resources.buildImage(this);

  }

  public String getStage() {
    return stage;
  }

  public void setRestIdOutput(CfnOutput restIdOutput) {
    this.restIdOutput = restIdOutput;
  }

  public void setBucketNameOutput(CfnOutput output) {
    bucketNameOutput = output;
  }

  public void setCloudFrontURLOutput(CfnOutput output) {
    cloudFrontURLOutput = output;
  }

  public void setCloudFrontDistributionIdOutput(CfnOutput output) {
    cloudFrontDistributionIdOutput = output;
  }
}
