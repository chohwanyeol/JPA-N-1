package com.JPATest.JPATest.controller;


import static com.JPATest.JPATest.util.QueryCountHolder.*;
import com.JPATest.JPATest.domain.Post;
import com.JPATest.JPATest.dto.BenchmarkResult;
import com.JPATest.JPATest.dto.PostCommentDto;
import com.JPATest.JPATest.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    // 1. N+1 발생
    @GetMapping("/basic")
    public List<Post> basic() {
        return postRepository.findBasicById(1L);
    }

    // 2. Fetch Join
    @GetMapping("/fetch")
    public List<Post> fetchJoin() {
        return postRepository.findWithFetchJoinById(1L);
    }

    // 3. DTO 방식
    @GetMapping("/dto")
    public List<PostCommentDto> dto() {
        return postRepository.findAsDtoById(1L);
    }


    @GetMapping("/bench/basic")
    public ResponseEntity<BenchmarkResult<List<Post>>> benchmarkBasic() throws JsonProcessingException {
        queryCountReset();

        long start = System.currentTimeMillis();
        List<Post> result = postRepository.findAllBasic();
        long end = System.currentTimeMillis();

        BenchmarkResult<List<Post>> benchmark = new BenchmarkResult<>(
                end - start,
                getQueryCount(),
                calculateJsonSize(result),
                estimateObjectSize(result),
                result
        );

        return ResponseEntity.ok(benchmark);
    }

    @GetMapping("/bench/fetch")
    public ResponseEntity<BenchmarkResult<List<Post>>> benchmarkFetchJoin() throws JsonProcessingException {
        queryCountReset();

        long start = System.currentTimeMillis();
        List<Post> result = postRepository.findAllWithFetchJoin();
        long end = System.currentTimeMillis();

        BenchmarkResult<List<Post>> benchmark = new BenchmarkResult<>(
                end - start,
                getQueryCount(),
                calculateJsonSize(result),
                estimateObjectSize(result),
                result
        );

        return ResponseEntity.ok(benchmark);
    }

    @GetMapping("/bench/dto")
    public ResponseEntity<BenchmarkResult<List<PostCommentDto>>> benchmarkDto() throws JsonProcessingException {
        queryCountReset();

        long start = System.currentTimeMillis();
        List<PostCommentDto> result = postRepository.findAllAsDto();
        long end = System.currentTimeMillis();

        BenchmarkResult<List<PostCommentDto>> benchmark = new BenchmarkResult<>(
                end - start,
                getQueryCount(),
                calculateJsonSize(result),
                estimateObjectSize(result),
                result
        );

        return ResponseEntity.ok(benchmark);
    }




    private final ObjectMapper objectMapper;

    private long calculateJsonSize(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(obj).length;
    }

    private long estimateObjectSize(Object obj) {
        if (obj instanceof List<?> list) {
            return list.size() * 500L; // 1개당 대략 500바이트 가정
        }
        return 0L;
    }



    private void queryCountReset() {
        clear();
    }

    private int getQueryCount() {
        return getCount();
    }


}
