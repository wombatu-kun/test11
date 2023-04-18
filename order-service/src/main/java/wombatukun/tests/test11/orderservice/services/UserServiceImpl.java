package wombatukun.tests.test11.orderservice.services;

import brave.ScopedSpan;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import wombatukun.tests.test11.common.dto.CommonResponse;
import wombatukun.tests.test11.common.enums.UserStatus;
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException;
import wombatukun.tests.test11.orderservice.dao.entities.UserCache;
import wombatukun.tests.test11.orderservice.dao.repositories.UserCacheRepository;
import wombatukun.tests.test11.orderservice.dto.UserDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserCacheRepository userCacheRepository;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final Tracer tracer;

    @Value("${auth.service-id:auth-service}")
    private String serviceId;
    @Value("${auth.instance}")
    private String authInstance;
    @Value("${auth.user-path:v1/users}")
    private String userPath;

    public void saveUserCache(UserCache userCache) {
        try {
            userCacheRepository.save(userCache);
        } catch (Exception ex){
            log.error("unable to cache user with id={}, exception {}", userCache.getId(), ex);
        }
    }

    @Override
    public UserCache getUser(Long id) {
        //3 custom spans for demonstration of distributed tracing with Zipkin
        ScopedSpan span = tracer.startScopedSpan("read-user-cache-from-redis");
        UserCache cache = getUserCache(id);
        finishSpan(span, "redis", "read cache");
        if (cache == null) {
            span = tracer.startScopedSpan("read-user-from-auth-service");
            cache = getUserFromAuthService(id);
            finishSpan(span, "auth-service", "get user");
            span = tracer.startScopedSpan("save-user-cache-to-redis");
            saveUserCache(cache);
            finishSpan(span, "redis", "save cache");
        }
        return cache;
    }

    private UserCache getUserCache(Long id) {
        try {
            return userCacheRepository.findById(id).orElse(null);
        } catch (Exception ex){
            log.error("error trying to retrieve userCache by id={}, exception {}", id, ex);
            return null;
        }
    }

    private UserCache getUserFromAuthService(Long id) {
        List<ServiceInstance> authInstances = discoveryClient.getInstances(serviceId);
        String serviceUri = String.format("%s/%s/%d",
                authInstances.isEmpty() ? authInstance : authInstances.get(0).getUri().toString(), userPath, id);
        log.debug("auth-service uri: {}", serviceUri);
        ResponseEntity<CommonResponse<UserDto>> response;
        try {
            response = restTemplate.exchange(serviceUri, HttpMethod.GET,
                    null, new ParameterizedTypeReference<CommonResponse<UserDto>>(){});
        } catch (ResourceAccessException ex) {
            throw new RuntimeException("connection to auth-service refused", ex);
        }
        log.debug("Auth-service response: {}", response);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getContent() != null) {
            UserDto user = response.getBody().getContent();
            return new UserCache(user.getId(), user.getRole(), user.getStatus());
        } else {
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("no courier with id=" + id);
            } else {
                throw new RuntimeException("error response form auth-service: " + response.getStatusCode() + " " + response.getBody());
            }
        }
    }

    private void finishSpan(ScopedSpan span, String serviceTag, String anno) {
        if (span != null) {
            span.tag("peer.service", serviceTag);
            span.annotate(anno);
            span.finish();
        }
    }

}
