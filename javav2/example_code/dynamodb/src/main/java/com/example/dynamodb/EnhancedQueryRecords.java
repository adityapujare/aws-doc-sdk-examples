//snippet-sourcedescription:[EnhancedQueryRecords.java demonstrates how to query an Amazon DynamoDB table by using the enhanced client]
//snippet-keyword:[SDK for Java 2.0]
//snippet-keyword:[Code Sample]
//snippet-service:[Amazon DynamoDB]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[3/15/2020]
//snippet-sourceauthor:[scmacdon-aws]

/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   This file is licensed under the Apache License, Version 2.0 (the "License").
   You may not use this file except in compliance with the License. A copy of
   the License is located at
    http://aws.amazon.com/apache2.0/
   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   CONDITIONS OF ANY KIND, either express or implied. See the License for the
   specific language governing permissions and limitations under the License.
*/
package com.example.dynamodb;

// snippet-start:[dynamodb.java2.mapping.query.import]
import java.time.Instant;
import java.util.Iterator;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
// snippet-end:[dynamodb.java2.mapping.query.import]

/*
    Prior to running this code example, create a table named Customer and populate it with data
 */

public class EnhancedQueryRecords {


    // Query the Record table
    public static void main(String[] args) {

        //Create a DynamoDbClient object
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        // Create a DynamoDbEnhancedClient and use the DynamoDbClient object
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

        String result = queryTable(enhancedClient);
        System.out.println(result);
    }

    // snippet-start:[dynamodb.java2.mapping.query.main]
    public static String queryTable(DynamoDbEnhancedClient enhancedClient) {

        try{
            //Create a DynamoDbTable object
            DynamoDbTable<Customer> mappedTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));

            // Create a QueryConditional object that is used in the query operation
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder()
                            .partitionValue("id120")
                            .build());

            // Get items in the Record table and write out the ID value
            Iterator<Customer> results = mappedTable.query(queryConditional).items().iterator();
            String result="";

            while (results.hasNext()) {
                Customer rec = results.next();
                result = rec.getId();
                System.out.println("The record id is "+result);
            }
            return result;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
        // snippet-end:[dynamodb.java2.mapping.query.main]
    }

    //Create the Customer table
    @DynamoDbBean
    public static class Customer {

        private String id;
        private String name;
        private String email;
        private Instant regDate;

        @DynamoDbPartitionKey
        public String getId() {
            return this.id;
        };

        public void setId(String id) {

            this.id = id;
        }

        @DynamoDbSortKey
        public String getCustName() {
            return this.name;

        }

        public void setCustName(String name) {

            this.name = name;
        }

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {

            this.email = email;
        }

        public Instant getRegistrationDate() {
            return regDate;
        }
        public void setRegistrationDate(Instant registrationDate) {

            this.regDate = registrationDate;
        }
    }
}
