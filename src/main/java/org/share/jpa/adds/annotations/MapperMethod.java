package org.share.jpa.adds.annotations;

import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@QueryAnnotation
@Documented
public @interface MapperMethod {


}
