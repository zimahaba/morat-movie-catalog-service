package edu.zimahaba.morat.moviecatalogservice.resources;

import com.netflix.discovery.DiscoveryClient;
import edu.zimahaba.morat.moviecatalogservice.models.CatalogItem;
import edu.zimahaba.morat.moviecatalogservice.models.Movie;
import edu.zimahaba.morat.moviecatalogservice.models.Rating;
import edu.zimahaba.morat.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient webClient;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(String userId) {

        UserRating ratings = restTemplate
                .getForObject("http://ratings-data-service/ratingsdata/users/"+userId, UserRating.class);

        return ratings.getUserRatings().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
            /*Movie movie = webClient.get().uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve().bodyToMono(Movie.class).block();*/

            return new CatalogItem(movie.getName(), "Test", rating.getRating());
        }).collect(Collectors.toList());
    }
}
