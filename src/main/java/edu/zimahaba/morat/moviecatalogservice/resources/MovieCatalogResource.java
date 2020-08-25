package edu.zimahaba.morat.moviecatalogservice.resources;

import edu.zimahaba.morat.moviecatalogservice.models.CatalogItem;
import edu.zimahaba.morat.moviecatalogservice.models.Movie;
import edu.zimahaba.morat.moviecatalogservice.models.Rating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(String userId) {

        RestTemplate restTemplate = new RestTemplate();


        List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("5678", 5)
        );

        return ratings.stream().map(rating -> {
            Movie movie = restTemplate
                    .getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "Test", rating.getRating());
        }).collect(Collectors.toList());
    }
}