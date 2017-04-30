
package me.u6k.sample_minio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private static final Logger L = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setWebEnvironment(false);

        ApplicationContext ctx = app.run(args);

        SpringApplication.exit(ctx);
    }

    @Override
    public void run(String... args) throws Exception {
        L.info("start");
    }

}
