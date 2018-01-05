package bgu.spl181.net.impl.DataHandling.movies;

import java.io.Serializable;
import java.util.List;

public class MoviesList implements Serializable {
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}

