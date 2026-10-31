
package com.campuscare.controller;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(Exception.class) public ResponseEntity<Map<String,String>> handle(Exception e){return ResponseEntity.status(400).body(Map.of("message",e.getMessage()==null?"Request failed":e.getMessage()));}
}
