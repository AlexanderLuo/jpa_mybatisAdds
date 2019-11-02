package work.share.jpa.adds;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JpaAddsAutoConfigurationStarter   {
    private static final Logger logger = LoggerFactory.getLogger(JpaAddsAutoConfigurationStarter.class);


    public JpaAddsAutoConfigurationStarter(){
        logger.info("********   【JPA ADDS MAPPER ACTIVE】   ********");
    }



}
