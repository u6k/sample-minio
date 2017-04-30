
package me.u6k.sample_minio;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private static final Logger L = LoggerFactory.getLogger(Main.class);

    @Value("${s3.url}")
    private String s3url;

    @Value("${s3.access-id}")
    private String s3accessId;

    @Value("${s3.secret-key}")
    private String s3secretKey;

    @Value("${s3.timeout}")
    private int s3timeout;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setWebEnvironment(false);

        ApplicationContext ctx = app.run(args);

        SpringApplication.exit(ctx);
    }

    @Override
    public void run(String... args) throws Exception {
        L.info("s3.url={}", this.s3url);
        L.info("s3.access-id={}", this.s3accessId);
        L.info("s3.secret-key={}", this.s3secretKey);
        L.info("s3.timeout={}", this.s3timeout);

        AWSCredentials credentials = new BasicAWSCredentials(this.s3accessId, this.s3secretKey);
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setSocketTimeout(this.s3timeout);
        configuration.setSignerOverride("AWSS3V4SignerType");
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withEndpointConfiguration(new EndpointConfiguration(this.s3url, "us-east-1"))
                        .withClientConfiguration(configuration)
                        .withPathStyleAccessEnabled(true)
                        .build();

        for (Bucket bucket : s3.listBuckets()) {
            L.info("bucket={}", bucket.getName());
        }
    }

}
