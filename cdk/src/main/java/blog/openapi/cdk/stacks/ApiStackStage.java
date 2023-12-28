package blog.openapi.cdk.stacks;

import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Stage;
import software.amazon.awscdk.StageProps;
import software.constructs.Construct;

public class ApiStackStage extends Stage {

	public ApiStackStage(final Construct scope, final String id) {
		this(scope, id, null);
	}

	public ApiStackStage(final Construct scope, final String deployEnvironment, final StageProps props) {
		super(scope, deployEnvironment, props);
		new ApiStack(this, "OpenAPIBlogAPI", deployEnvironment, StackProps.builder().env(props.getEnv()).build());
	}

}
