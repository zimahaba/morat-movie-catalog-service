package edu.zimahaba.morat.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import edu.zimahaba.morat.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRatingInfoService {

    @Autowired
    private RestTemplate restTemplate;

    // Bulkhead
    @HystrixCommand(fallbackMethod = "getFallbackUserRating",
            threadPoolKey = "userRatingServicePool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            })
    public UserRating getUserRating(@PathVariable("userId") String userId) {
        return restTemplate
                .getForObject("http://ratings-data-service/ratingsdata/users/"+userId, UserRating.class);
    }
}
