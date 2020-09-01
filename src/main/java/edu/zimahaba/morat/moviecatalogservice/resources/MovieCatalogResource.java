package edu.zimahaba.morat.moviecatalogservice.resources;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import edu.zimahaba.morat.moviecatalogservice.models.CatalogItem;
import edu.zimahaba.morat.moviecatalogservice.models.Movie;
import edu.zimahaba.morat.moviecatalogservice.models.Rating;
import edu.zimahaba.morat.moviecatalogservice.models.UserRating;
import edu.zimahaba.morat.moviecatalogservice.services.MovieInfoService;
import edu.zimahaba.morat.moviecatalogservice.services.UserRatingInfoService;
import netscape.security.UserTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient webClient;

    @Autowired
    MovieInfoService movieInfoService;

    @Autowired
    UserRatingInfoService userRatingInfoService;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(String userId) {

        UserRating ratings = userRatingInfoService.getUserRating(userId);

        return ratings.getUserRatings().stream()
                .map(rating -> movieInfoService.getCatalogItem(rating))
                .collect(Collectors.toList());
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie", "", 0);
    }

    private UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setUserRatings(
                Arrays.asList(new Rating("0", 0))
        );
        return userRating;
    }

    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }
}
