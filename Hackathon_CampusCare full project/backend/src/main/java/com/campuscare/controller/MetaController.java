
package com.campuscare.controller;
import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/meta")
public class MetaController {
 @GetMapping("/categories") public List<String> categories(){return List.of("ELECTRICAL","NETWORK","CLASSROOM","PLUMBING","WATER","FURNITURE","LAB_EQUIPMENT","CLEANLINESS","SECURITY","OTHER");}
 @GetMapping("/departments") public List<String> departments(){return List.of("IT SUPPORT","ELECTRICAL","MAINTENANCE","HOUSEKEEPING","SECURITY","LAB SUPPORT","ADMINISTRATION");}
}
