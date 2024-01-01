package blog.openapi.cdk.quicksight;


import java.util.List;
import blog.openapi.cdk.stacks.ApiStack;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.services.quicksight.*;
public class QuickSightTest {

  public void build(ApiStack stack) {

    CfnVPCConnection cfnVPCConnection = CfnVPCConnection.Builder.create(this, "MyCfnVPCConnection")
       .availabilityStatus("availabilityStatus")
       .awsAccountId("awsAccountId")
       .dnsResolvers(List.of("dnsResolvers"))
       .name("name")
       .roleArn("roleArn")
       .securityGroupIds(List.of("securityGroupIds"))
       .subnetIds(List.of("subnetIds"))
       .tags(List.of(CfnTag.builder()
           .key("key")
           .value("value")
           .build()))
       .vpcConnectionId("vpcConnectionId")
       .build();

  }

}


// {
//     "Status": 200,
//     "VPCConnectionSummaries": [
//         {
//             "VPCConnectionId": "f8f34873-d954-417c-bbf8-e441d53f2a85",
//             "Arn": "arn:aws:quicksight:us-east-1:282691401813:vpcConnection/f8f34873-d954-417c-bbf8-e441d53f2a85",
//             "Name": "vpc-connection-rds",
//             "VPCId": "vpc-3800bb40",
//             "SecurityGroupIds": [
//                 "sg-0655d246e783d9c59"
//             ],
//             "Status": "CREATION_SUCCESSFUL",
//             "AvailabilityStatus": "AVAILABLE",
//             "NetworkInterfaces": [
//                 {
//                     "SubnetId": "subnet-bddd1992",
//                     "AvailabilityZone": "us-east-1b",
//                     "Status": "AVAILABLE",
//                     "NetworkInterfaceId": "eni-005d478822811b2ba"
//                 },
//                 {
//                     "SubnetId": "subnet-3b23a570",
//                     "AvailabilityZone": "us-east-1c",
//                     "Status": "AVAILABLE",
//                     "NetworkInterfaceId": "eni-07b9f11776103ebff"
//                 }
//             ],
//             "RoleArn": "arn:aws:iam::282691401813:role/service-role/aws-quicksight-service-role-v0",
//             "CreatedTime": "2023-12-21T23:06:36.219000+00:00",
//             "LastUpdatedTime": "2023-12-21T23:07:59.063000+00:00"
//         }
//     ],
//     "RequestId": "34454134-abb4-46f7-a5e5-4634c8055b9e"
// }