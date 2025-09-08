package gc.board.article.config;

import kuke.board.common.snowflake.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakConfig {

    @Bean
    public Snowflake snowflake() {
        return new Snowflake();
    }
}
