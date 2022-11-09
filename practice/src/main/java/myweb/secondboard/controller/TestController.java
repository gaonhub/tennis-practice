package myweb.secondboard.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

  @GetMapping
  public String test() {
    return "test";
  }


  @PostMapping
  @ResponseBody
  public Map<String, String> testAjax(@RequestParam(value = "form") Map<String,String> map ) {
    System.out.println(map);
    return map;
  }


}
