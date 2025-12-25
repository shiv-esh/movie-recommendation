package com.project.movieRecommendation.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.movieRecommendation.entity.elastic.MovieDocument;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieSearchRepository {

    private final ElasticsearchClient client;
    private static final String INDEX = "movies";

    public void index(MovieDocument movie) throws IOException {
        client.index(i -> i
                .index(INDEX)
                .id(movie.getId())
                .document(movie)
        );
    }

    public List<MovieDocument> search(String title, String genre, Double minRating, Double maxRating) throws IOException {
        if (StringUtils.isBlank(title) &&
                StringUtils.isBlank(genre) &&
                minRating == null &&
                maxRating == null) {
            return Collections.emptyList();
        }
        List<Query> mustQueries = new ArrayList<>();
        List<Query> filterQueries = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            mustQueries.add(
                    MatchQuery.of(m -> m
                            .field("title")
                            .query(title)
                    )._toQuery()
            );
        }

        if (genre != null && !genre.isBlank()) {
            filterQueries.add(
                    MatchQuery.of(t -> t
                            .field("genre")
                            .query(genre)
                    )._toQuery()
            );
        }

        if (minRating != null || maxRating != null) {
            filterQueries.add(
                    RangeQuery.of(r -> r
                            .number(n -> {
                                n.field("rating");
                                if (minRating != null) n.gte(minRating);
                                if (maxRating != null) n.lte(maxRating);
                                return n;
                            })
                    )._toQuery()
            );
        }
        SearchResponse<MovieDocument> searchResponse = client.search(s -> s
                        .index("movies")
                        .query(q -> q.bool(b -> {
                            b.must(mustQueries).
                                    filter(filterQueries);

                            return b;
                        }))
                        .sort(so -> so.field(f -> f.field("rating").order(SortOrder.Desc))),
                MovieDocument.class
        );
        List<MovieDocument> movies = new ArrayList<>();
        for (Hit<MovieDocument> hit : searchResponse.hits().hits()) {
            movies.add(hit.source());
        }
        return movies;

    }

    public void deleteById(String id) throws IOException {
        client.delete(d -> d
                .index(INDEX)
                .id(id)
        );
    }

    public List<MovieDocument> recommend(String movieId, String genre, int limit) throws IOException {
        Query query = FunctionScoreQuery.of(fs->fs
                .query(q->q
                        .bool(b->b
                                .must(m->m
                                        .term(t->t
                                                .field("genre")
                                                .value(genre)
                                        )
                                )
                                .mustNot(m-> m
                                        .term(t->t
                                                .field("_id")
                                                .value(movieId)
                                        )
                                )
                        )
                )
                .functions(f->f
                        .fieldValueFactor(ff->ff
                                .field("rating")
                                .factor(1.2)
                        )
                )
        )._toQuery();

        SearchResponse<MovieDocument> response = client.search(s->s
                .index(INDEX)
                .query(query)
                .size(limit),
                MovieDocument.class);
        List<MovieDocument> documents = new ArrayList<>();
        for(Hit<MovieDocument> doc: response.hits().hits()){
            documents.add(doc.source());
        }
        return documents;

    }

}
