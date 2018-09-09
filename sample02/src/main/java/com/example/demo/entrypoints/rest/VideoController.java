package com.example.demo.entrypoints.rest;

import com.example.demo.core.domain.Video;
import com.example.demo.core.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping
    public Flux<Video> findAll() {
        return videoRepository.findAll();
    }

    @GetMapping("/{nid}")
    public Mono<Video> findByNid(@PathVariable String nid) {
        return videoRepository.findByNid(nid);
    }

}
