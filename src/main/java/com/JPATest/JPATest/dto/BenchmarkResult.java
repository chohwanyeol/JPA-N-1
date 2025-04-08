package com.JPATest.JPATest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BenchmarkResult<T> {
    private long executionTimeMs;     // 실행 시간
    private int queryCount;           // 쿼리 수
    private long responseSizeBytes;   // JSON 바이트 크기
    private long estimatedObjectSize; // 자바 객체 메모리 추정치
    private T data;                   // 실제 데이터 (optional)
}
