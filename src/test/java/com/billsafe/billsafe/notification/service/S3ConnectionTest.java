package com.billsafe.billsafe.notification.service;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

public class S3ConnectionTest {

    @Test
    void testS3Connection() {

        String bucketName = System.getenv("AWS_S3_BUCKET");
        String region = System.getenv("AWS_REGION");

        try (S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .build()) {

            s3Client.headBucket(
                    HeadBucketRequest.builder()
                            .bucket(bucketName)
                            .build()
            );

            System.out.println("S3 bucket connection successful!");
        }
    }
}
