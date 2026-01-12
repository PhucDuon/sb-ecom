package com.ecommerce.project.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.ecommerce.project.product.model.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchOperations operations;
    private final ElasticsearchClient elasticsearchClient;


    public Page<ProductDocument> search(
            String keyword,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    ) {

        try {
            BoolQuery.Builder bool = new BoolQuery.Builder();


            if (keyword != null && !keyword.isBlank()) {
                bool.must(m -> m.matchBoolPrefix(mbp -> mbp
                        .field("name")
                        .query(keyword)
                        .boost(3.0f)
                ));
            }


            if (minPrice != null || maxPrice != null) {
                bool.filter(f -> f.range(r -> {
                    r.field("price");
                    if (minPrice != null) r.gte(JsonData.of(minPrice));
                    if (maxPrice != null) r.lte(JsonData.of(maxPrice));
                    return r;
                }));
            }


            bool.filter(f -> f.term(t -> t
                    .field("active")
                    .value(true)
            ));


            SearchResponse<ProductDocument> response =
                    elasticsearchClient.search(s -> s
                                    .index("products")
                                    .query(q -> q.bool(bool.build()))
                                    .from((int) pageable.getOffset())
                                    .size(pageable.getPageSize()),
                            ProductDocument.class
                    );

            List<ProductDocument> content = response.hits().hits()
                    .stream()
                    .map(Hit::source)
                    .toList();

            long total = response.hits().total() != null
                    ? response.hits().total().value()
                    : content.size();

            return new PageImpl<>(content, pageable, total);

        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch search failed", e);
        }
    }


    public List<String> autocomplete(String keyword, int limit) {

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        NativeQuery query = new NativeQueryBuilder()
                .withQuery(q -> q
                        .matchBoolPrefix(m -> m
                                .field("name")
                                .query(keyword)
                        )
                )
                .withFilter(f -> f.term(t -> t
                        .field("active")
                        .value(true)
                ))
                .withPageable(PageRequest.of(0, limit))
                .build();


        SearchHits<ProductDocument> hits =
                operations.search(query, ProductDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().getName())
                .distinct()
                .toList();
    }
}
