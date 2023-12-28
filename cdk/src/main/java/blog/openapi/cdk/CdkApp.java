package blog.openapi.cdk;

import blog.openapi.cdk.stacks.PipelineStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class CdkApp {

  // Helper method to build an environment
  static Environment makeEnv(String account, String region) {
    account = (account == null) ? System.getenv("CDK_DEPLOY_ACCOUNT") : account;
    region = (region == null) ? System.getenv("CDK_DEPLOY_REGION") : region;
    account = (account == null) ? System.getenv("CDK_DEFAULT_ACCOUNT") : account;
    region = (region == null) ? System.getenv("CDK_DEFAULT_REGION") : region;

    return Environment.builder().account(account).region(region).build();
  }

  public static void main(final String[] args) {
    App app = new App();

    // Environment envEU = makeEnv(null, null);
    // Environment envUSA = makeEnv(null, null);

    String repositoryString = app.getNode().tryGetContext("RepositoryString").toString();
    String repositoryBranch = app.getNode().tryGetContext("RepositoryBranch").toString();
    String codestarConnectionArn = app.getNode().tryGetContext("CodestarConnectionArn").toString();
    String codeArtifactDomain = app.getNode().tryGetContext("CodeArtifactDomain").toString();
    String codeArtifactRepository =
        app.getNode().tryGetContext("CodeArtifactRepository").toString();
    String deployEnvironment = app.getNode().tryGetContext("DeployEnvironment").toString();

    new PipelineStack(app, "OpenAPIBlogPipeline", repositoryString, repositoryBranch,
        codestarConnectionArn, codeArtifactRepository, codeArtifactDomain,
        StackProps.builder().env(makeEnv(null, null)).build(), deployEnvironment);

    app.synth();
  }
}
