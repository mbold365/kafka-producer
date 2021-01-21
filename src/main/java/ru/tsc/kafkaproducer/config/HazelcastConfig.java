package ru.tsc.kafkaproducer.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HazelcastConfig {

    private static final String HC_INSTANCE_BEAN_NAME = "hcInstance";

    @Bean
    CacheManager cacheManage(@Qualifier(HC_INSTANCE_BEAN_NAME) HazelcastInstance hcInstance) {
        return new HazelcastCacheManager(hcInstance);
    }

    @Bean(HC_INSTANCE_BEAN_NAME)
    public HazelcastInstance hazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public Config config() {
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true).addMember("127.0.0.1:5701");
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        return config;
    }

    @Bean
    public IMap<Long, byte[]> configMap(
            @Qualifier(HC_INSTANCE_BEAN_NAME) HazelcastInstance hcInstance,
            @Value("${hazelcast.map.name}") String configMapName) {
        return hcInstance.getMap(configMapName);
    }
}
