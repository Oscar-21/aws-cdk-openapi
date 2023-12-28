package blog.openapi.cdk.stacks;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import software.amazon.awscdk.services.codebuild.Cache;
import software.amazon.awscdk.services.codebuild.LocalCacheMode;
import software.constructs.Construct;
import software.amazon.awscdk.Arn;
import software.amazon.awscdk.ArnComponents;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.StageProps;
import software.amazon.awscdk.pipelines.AddStageOpts;
import software.amazon.awscdk.pipelines.CodeBuildStep;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodePipelineSource;
import software.amazon.awscdk.pipelines.ConnectionSourceOptions;
import software.amazon.awscdk.pipelines.ShellStep;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;

public class PipelineStack extends Stack {

	public PipelineStack(final Construct scope, final String id,
			String repositoryPath, String repositoryBranch, String connectionArn,
			String codeArtifactRepositoryName, String codeArtifactDomainName) {

		this(scope, id, repositoryPath, repositoryBranch, connectionArn,
				codeArtifactRepositoryName, codeArtifactDomainName, null);
	}

	public PipelineStack(final Construct scope, final String id,
			String repositoryPath, String repositoryBrach, String connectionArn,
			String codeArtifactRepositoryName, String codeArtifactDomainName, final StackProps props) {

		super(scope, id, props);

		/*
		 *
		 * CodePipelineSource.connection(...):
		 * This is creating a pipeline source from a repository.
		 *
		 * The repositoryPath and repositoryBranch parameters specify the repository and branch to use.
		 * The connectionArn parameter specifies the ARN of the AWS CodeStar Connections connection.
 		 */
		CodePipelineSource pipelineSource = CodePipelineSource.connection(repositoryPath, repositoryBrach,
				ConnectionSourceOptions.builder()
						.connectionArn(connectionArn)
						.build());

		// Synth Caching Support
		// https://github.com/aws/aws-cdk/issues/13043,
		// https://github.com/aws/aws-cdk/issues/16375
		// https://aws.amazon.com/blogs/devops/how-to-enable-caching-for-aws-codebuild/
		// https://docs.aws.amazon.com/codebuild/latest/userguide/build-caching.html
		// https://aws.amazon.com/blogs/devops/reducing-docker-image-build-time-on-aws-codebuild-using-an-external-cache/

		/*
		 * ShellStep.Builder.create(...):
		 * This is creating a synthesis step.
		 * The synthesis step is where your CDK application
		 * is synthesized (i.e., compiled) into a CloudFormation template.
		 */
		ShellStep synthStep = ShellStep.Builder.create("Synth")
				// This is specifying the input to the synthesis step.
				// The input is the pipeline source that was created earlier.
				.input(pipelineSource)
				// These are the commands that will be run during the synthesis step.
				// These commands are installing the AWS CDK,
				// changing the current directory to the cdk directory,
				// and synthesizing the CDK application.
				.commands(Arrays.asList("npm install -g aws-cdk", "cd cdk", "cdk synth"))
				// This is specifying the primary output directory for the synthesis step.
				// The contents of this directory after the synthesis step will be the primary output of the step.
				.primaryOutputDirectory("cdk/cdk.out")
				.build();
		//

		// This code is creating an AWS CDK pipeline using the CodePipeline.Builder.create.
		// This initializes a new instance of the CodePipeline class with the
		// current construct (this) and the ID OpenAPIBlogPipeline.
		final CodePipeline pipeline = CodePipeline.Builder.create(this, "OpenAPIBlogPipeline")
				// This sets the name of the pipeline to OpenAPIBlogPipeline.
				.pipelineName("OpenAPIBlogPipeline")
				// This enables self-mutation for the pipeline.
				// Self-mutation means that the pipeline can update itself when changes
				// are made to the pipeline’s infrastructure.
				.selfMutation(true)
				// This enables Docker for the synthesis step.
				// The synthesis step is where your CDK application is synthesized (i.e., compiled)
				// into a CloudFormation template.
				// If Docker is enabled, the commands in the synthesis step will be run inside a Docker container.
				.dockerEnabledForSynth(true)
				// This sets the synthesis step for the pipeline.
				// The synthStep parameter is a ShellStep that was created earlier,
				// which specifies the commands to run during the synthesis step.
				.synth(synthStep)
				// This builds the pipeline.
				// After this method is called, the pipeline is fully constructed and ready to be used.
				// this code is setting up an AWS CDK pipeline with a specific name, enabling self-mutation
				// and Docker for the synthesis step, setting the synthesis step, and building the pipeline.
				// The pipeline can then be used to deploy AWS CDK applications.
				.build();

		PolicyStatement codeArtifactStatement = PolicyStatement.Builder.create()
				.sid("CodeArtifact")
				.effect(Effect.ALLOW)
				.actions(Arrays.asList("codeartifact:GetAuthorizationToken", "codeartifact:GetRepositoryEndpoint",
						"codeartifact:ReadFromRepository", "codeartifact:DescribeRepository",
						"codeartifact:PublishPackageVersion", "codeartifact:PutPackageMetadata"))
				.resources(Arrays.asList(
						Arn.format(
								ArnComponents.builder()
										.service("codeartifact")
										.resource(String.format("repository/%s/%s", codeArtifactDomainName,
												codeArtifactRepositoryName))
										.build(),
								this),
						Arn.format(
								ArnComponents.builder()
										.service("codeartifact")
										.resource(String.format("domain/%s", codeArtifactDomainName))
										.build(),
								this),
						Arn.format(
								ArnComponents.builder()
										.service("codeartifact")
										.resource(String.format("package/%s/%s/*", codeArtifactDomainName,
												codeArtifactRepositoryName))
										.build(),
								this)))
				.build();

		PolicyStatement codeArtifactStsStatement = PolicyStatement.Builder.create()
				.sid("CodeArtifactStsStatement")
				.effect(Effect.ALLOW)
				.actions(Arrays.asList("sts:GetServiceBearerToken"))
				.resources(Arrays.asList("*"))
				.conditions(new HashMap<String, Object>() {
					{
						put("StringEquals", new HashMap<String, String>() {
							{
								put("sts:AWSServiceName", "codeartifact.amazonaws.com");
							}
						});
					}
				})
				.build();

		CodeBuildStep codeArtifactStep = CodeBuildStep.Builder.create("CodeArtifactDeploy")
				.input(pipelineSource)
				.commands(Arrays.asList(
						"echo $REPOSITORY_DOMAIN",
						"echo $REPOSITORY_NAME",
						"export CODEARTIFACT_TOKEN=`aws codeartifact get-authorization-token --domain $REPOSITORY_DOMAIN --query authorizationToken --output text`",
						"export REPOSITORY_ENDPOINT=$(aws codeartifact get-repository-endpoint --domain $REPOSITORY_DOMAIN --repository $REPOSITORY_NAME --format maven | jq .repositoryEndpoint | sed 's/\\\"//g')",
						"echo $REPOSITORY_ENDPOINT",
						"cd api",
						"wget -q https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/5.4.0/openapi-generator-cli-5.4.0.jar -O openapi-generator-cli.jar",
						"cp ./maven-settings.xml /root/.m2/settings.xml",
						"java -jar openapi-generator-cli.jar batch openapi-generator-config.yaml",
						"cd client",
						"mvn -version",
						// mvn --no-transfer-progress for mvn >= 3.6.1
                        // "mvn clean install"
                        // "mvn javadoc:javadoc"
                        // "cd target/site"
						"mvn --no-transfer-progress deploy -DaltDeploymentRepository=openapi--prod::default::$REPOSITORY_ENDPOINT"))
				.rolePolicyStatements(Arrays.asList(codeArtifactStatement, codeArtifactStsStatement))
				.env(new HashMap<String, String>() {
					{
						put("REPOSITORY_DOMAIN", codeArtifactDomainName);
						put("REPOSITORY_NAME", codeArtifactRepositoryName);
					}
				})
				.cache(Cache.local(LocalCacheMode.DOCKER_LAYER))
				.build();

		ApiStackStage apiStackStageDev = new ApiStackStage(this, "DEV", StageProps.builder().build());
		pipeline.addStage(apiStackStageDev,
				AddStageOpts.builder()
						.post(Collections.singletonList(codeArtifactStep))
						.build());

	}

}
