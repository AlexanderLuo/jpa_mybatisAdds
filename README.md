## 简介
    1。以插件形式在spring-data-jpa框架下，提供mybatis的xml动态sql解析生成功能。
    2。对spring-data-jpa的使用无任何影响。
    3。与mybatis的dao接口使用方法一致，只需要注入，调用接口方法即可。
 
 
## 一分钟使用
   1.springboot Main 函数
    
    ```
     @EnableJpaAdds
     @SpringBootApplication
     public class Start{......SpringBoot.run(Start.class,args).....}
    ```
   2.Repository
   
    @Repository
    public interface UserEntityRepositroy extends CrudRepository<UserEntity, Long> {
        
        Page<UserEntity> findFromMapper(Pageable pageable);
    }
    
   3.XMl(META-INF/UserEntityMapper.xml)
     
    <?xml version="1.0" encoding="UTF-8" ?>
    <mapper namespace="org.learn.hikaricp.dao.UserEntityRepositroy">

        <select id="findFromMapper">
            select * from t_user where 1=1  
        </select>
    </mapper>

         
   4.调用
    
     @Autowired
     UserEntityRepositroy userEntityRepositroy
     ......
     Page<UserEntity> result = userEntityRepositroy.findFromMapper(PageRequest.of(0,4, Sort.by(Sort.Direction.DESC,"user_id")));
     ......  
       
       
## todo
    目前支持 <if> , <where> ,<trim>
    支持所有sql节点
    
    