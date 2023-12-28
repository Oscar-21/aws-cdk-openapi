package blog.openapi.cdk.api;

import java.util.Arrays;
import java.util.List;

import software.amazon.awscdk.Stack;

public abstract class AbstractCustomLambdaRuntime<T extends Stack> {

	
	/**
	 * Commands to run on the custom docker image for a lambda environment. This
	 * lambda environment was built from the quarkus lambda rest function in ../app
	 */
	protected static List<String> defaultApiPackagingInstructions = Arrays.asList("/bin/sh", "-c", "pwd && ls -l && "
			+ "mvn --no-transfer-progress clean package && " + "cp target/function.zip /asset-output/");

	public abstract void buildImage(T stack);

}
