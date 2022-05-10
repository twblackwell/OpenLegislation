package gov.nysenate.openleg.legislation;

import com.google.common.eventbus.EventBus;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.internal.statistics.DefaultStatisticsService;
import org.ehcache.core.spi.service.StatisticsService;
import org.ehcache.core.statistics.CacheStatistics;
import org.ehcache.expiry.ExpiryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.ParameterizedType;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public abstract class CachingService<Key, Value> {
    private static final Logger logger = LoggerFactory.getLogger(CachingService.class);
    private static final StatisticsService statisticsService = new DefaultStatisticsService();
    private static final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .using(statisticsService).build(true);
    // Adds some extra space into caches beyond their initial entries.
    private static final double WIGGLE_ROOM = 1.2;
    private static final int DEFAULT_SIZE = 50;
    private static final EnumMap<CacheType, CachingService<?, ?>> cacheTypeMap =
            new EnumMap<>(CacheType.class);

    @Autowired
    private Environment environment;
    @Autowired
    protected EventBus eventBus;
    protected Cache<Key, Value> cache;

    protected abstract CacheType cacheType();

    @Nonnull
    public static CacheStatistics getStats(CacheType type) {
        return statisticsService.getCacheStatistics(type.name());
    }

    public static Configuration cacheManagerConfig() {
        return cacheManager.getRuntimeConfiguration();
    }

    protected EvictionAdvisor<Key, Value> evictionAdvisor() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    protected void init() {
        var classes = ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments();
        var keyClass = (Class<Key>) classes[0];
        var valueClass = (Class<Value>) classes[1];
        var type = cacheType();
        var currCache = cacheManager.getCache(type.name().toLowerCase(), keyClass, valueClass);
        if (currCache != null) {
            this.cache = currCache;
            logger.warn("Class " + this.getClass() + " tried to duplicate a cache.");
            return;
        }

        Map<Key, Value> initialEntries = initialEntries();
        int numEntries = (int) (initialEntries.size() * WIGGLE_ROOM);
        if (numEntries == 0) {
            String size = environment.getProperty(type.name().toLowerCase() + ".cache.size");
            numEntries = size == null ? DEFAULT_SIZE : Integer.parseInt(size);
        }
        var config = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(keyClass, valueClass, ResourcePoolsBuilder.heap(numEntries))
                .withSizeOfMaxObjectGraph(100000).withExpiry(ExpiryPolicy.NO_EXPIRY)
                .withEvictionAdvisor(evictionAdvisor());
        this.cache = cacheManager.createCache(type.name(), config);
        cacheTypeMap.put(type, this);
        initialEntries.forEach((k, v) -> cache.put(k, v));
    }

    @PreDestroy
    protected void cleanUp() {
        cache.clear();
        cacheManager.removeCache(cacheType().name());
    }

    public Map<Key, Value> initialEntries() {
        return Map.of();
    }

    private synchronized void clearCache(boolean warmCaches) {
        cache.clear();
        if (warmCaches) {
            cache.putAll(initialEntries());
        }
    }

    public static synchronized void clearCaches(Set<CacheType> types, boolean warmCaches) {
        if (types.contains(CacheType.BILL) || types.contains(CacheType.BILL_INFO)) {
            // Ensures the Set is mutable.
            types = new HashSet<>(types);
            types.add(CacheType.BILL);
            types.add(CacheType.BILL_INFO);
        }
        for (var cachingService : types.stream().map(cacheTypeMap::get).toList()) {
            cachingService.clearCache(warmCaches);
        }
    }
}
