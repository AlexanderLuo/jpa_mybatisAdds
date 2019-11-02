package work.share.jpa.adds.annotations;

import work.share.jpa.adds.ExtensionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@EnableJpaRepositories(repositoryFactoryBeanClass = ExtensionRepositoryFactoryBean.class)
public @interface EnableJpaAdds {

}
