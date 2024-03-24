package cn.itcast.storage;

import io.seata.spring.annotation.GlobalTransactional;
import io.seata.spring.boot.autoconfigure.SeataAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 虎哥
 */
@MapperScan("cn.itcast.storage.mapper")
@SpringBootApplication()
@EnableDiscoveryClient   // 开启nacos服务
public class StorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class, args);
    }
}
