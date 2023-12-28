package blog.openapi.cdk.api.docssite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import blog.openapi.cdk.api.AbstractCustomLambdaRuntime;
import blog.openapi.cdk.stacks.ApiStack;
import software.amazon.awscdk.BundlingOptions;
import software.amazon.awscdk.BundlingOutput;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.DockerImage;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.ILocalBundling;
import software.amazon.awscdk.PhysicalName;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.CacheControl;
import software.amazon.awscdk.services.s3.deployment.ISource;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.amazon.awscdk.services.cloudfront.Behavior;
import software.amazon.awscdk.services.cloudfront.CloudFrontWebDistribution;
import software.amazon.awscdk.services.cloudfront.OriginAccessIdentity;
import software.amazon.awscdk.services.cloudfront.S3OriginConfig;
import software.amazon.awscdk.services.cloudfront.SourceConfiguration;
import software.amazon.awscdk.services.cloudfront.ViewerCertificate;
import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.route53.targets.CloudFrontTarget;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;

public class ApiDocsS3Resources extends AbstractCustomLambdaRuntime<ApiStack> {

	@Override
	public void buildImage(ApiStack stack) {
		/**
		 * Generate the documentation from the OpenAPI Specification docker build -t openapidoc .
		 * docker run -it openapidoc sh
		 */
		String entry = "../app/api-docs/docker";

		// Alpine ruby docker image size = 532.12 MB
		// Ruby slim docker image size = 829.31 MB
		DockerImage apDocImage = DockerImage.fromBuild(entry);
		// TODO: Rather than build the docker image, this should be pushed to a
		// container registry like Amazon ECR and pulled from there.
		// The build of the image can take up to 5 minutes. The image can be referenced
		// from the registry like:
		// DockerImage apDocImage = DockerImage.fromRegistry(path);

		List<String> apiDocPackagingInstructions = Arrays.asList("/bin/sh", "-c",
				"pwd && ls -l && " + "redocly build-docs openapi.yaml --output=index.html && "
						+ "ls -la && " + "cp index.html /asset-output/");

		BundlingOptions.Builder apiDocBuilderOptions =
				BundlingOptions.builder().command(defaultApiPackagingInstructions).image(apDocImage)
						.local(new ILocalBundling() {
							@Override
							public @NotNull Boolean tryBundle(@NotNull String outputDir,
									@NotNull BundlingOptions options) {
								// Use container for all builds.
								return false;
							}
						}).user("root").outputType(BundlingOutput.NOT_ARCHIVED);

		ISource apiDocSource = Source.asset("../app/api-docs",
				AssetOptions.builder()
						.bundling(apiDocBuilderOptions.command(apiDocPackagingInstructions).build())
						.build());

		Bucket webBucket = Bucket.Builder.create(stack,
				ApiStack.LogicalIds.ApiDocsBucketAndCloudFrontDistributionResources.OpenAPIBlogAPIBucket)
				.bucketName(PhysicalName.GENERATE_IF_NEEDED).versioned(true)
				.encryption(BucketEncryption.UNENCRYPTED).autoDeleteObjects(true)
				.removalPolicy(RemovalPolicy.DESTROY).build();

		OriginAccessIdentity oai =
				OriginAccessIdentity.Builder.create(stack, "OpenAPIBlogWidgetAPIOAI")
						.comment("OAI for the OpenAPI Blog Widget API Document Website").build();

		S3OriginConfig s3OriginConfig = S3OriginConfig.builder().s3BucketSource(webBucket)
				.originAccessIdentity(oai).build();

		webBucket.grantRead(oai);

		List<SourceConfiguration> cloudFrontConfigs = new ArrayList<SourceConfiguration>();
		cloudFrontConfigs.add(SourceConfiguration.builder().s3OriginSource(s3OriginConfig)
				.behaviors(Arrays.asList(Behavior.builder().isDefaultBehavior(true).build()))
				.build());

		Certificate certificate = Certificate.Builder
				.create(stack, ApiStack.LogicalIds.APIGatewayResources.APIGatewayCertificate)
				.domainName("openapidocs.heoureialwed.com")
				.validation(CertificateValidation.fromDns(stack.getHostedZone())).build();

		CloudFrontWebDistribution cloudFrontWebDistribution = CloudFrontWebDistribution.Builder
				.create(stack, "OpenAPIBlogCFD").originConfigs(cloudFrontConfigs)
				.viewerCertificate(ViewerCertificate.fromAcmCertificate(certificate)).build();

		ARecord.Builder.create(stack, "aRecordcfds").zone(stack.getHostedZone())
				.recordName("openapidocs")
				.target(RecordTarget.fromAlias(new CloudFrontTarget(cloudFrontWebDistribution)))
				.build();

		BucketDeployment bucketDeployment = BucketDeployment.Builder
				.create(stack, "OpenAPIBlogS3Deployment").sources(Arrays.asList(apiDocSource))
				.destinationBucket(webBucket).distribution(cloudFrontWebDistribution)
				.distributionPaths(Arrays.asList("/*")).prune(true)
				.cacheControl(Arrays.asList(CacheControl.setPublic(),
						CacheControl.maxAge(Duration.seconds(0)),
						CacheControl.sMaxAge(Duration.seconds(0))))
				.build();

		stack.setBucketNameOutput(CfnOutput.Builder.create(stack, "OpenAPIBlogWebBucketName")
				.value(webBucket.getBucketName()).build());

		stack.setCloudFrontURLOutput(CfnOutput.Builder.create(stack, "OpenAPIBlogCloudFrontURL")
				.value(cloudFrontWebDistribution.getDistributionDomainName()).build());

		stack.setCloudFrontDistributionIdOutput(
				CfnOutput.Builder.create(stack, "OpenAPIBlogCloudFrontDistributionID")
						.value(cloudFrontWebDistribution.getDistributionId()).build());

	}

}
