package blog.openapi.cdk.stacks;


import blog.openapi.cdk.api.apigateway.BlogApiGatewayEndpoints;
import blog.openapi.cdk.api.docssite.ApiDocsS3Resources;
import blog.openapi.cdk.api.lambda.BlogLambda;
import software.constructs.Construct;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class ApiStack extends Stack {

	private CfnOutput restIdOutput;

	private CfnOutput bucketNameOutput;

	private CfnOutput cloudFrontURLOutput;

	private CfnOutput cloudFrontDistributionIdOutput;
	private final String stage;

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
		//                                                                //
		//  Create the Rest Enabled Lambda via the Quarkus framework      // 
		//                                                                //
		///////////////////////////////////////////////////////////////////
		BlogLambda blogLambda =  new BlogLambda();
		blogLambda.buildImage(this);

		
		////////////////////////////////////////////////////////////////////
		//                                                                //
		//  Create the API Gateway Endpoints from the OpenAPI Document    // 
		//                                                                //
		///////////////////////////////////////////////////////////////////
		BlogApiGatewayEndpoints apiGatewayEndpoints = new BlogApiGatewayEndpoints();
		apiGatewayEndpoints.build(this);
		
		////////////////////////////////////////////////////////////////////
		//                                                                //
		//  Create the API Documentation Website                          // 
		//                                                                //
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
