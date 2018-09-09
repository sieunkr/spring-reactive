package com.example.demo.core.repository;

import com.example.demo.core.domain.Video;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  VideoRepository extends ReactiveMongoRepository<Video, Integer> {

    Flux<Video> findAll();
    Mono<Video> findByNid(String nid);

}
