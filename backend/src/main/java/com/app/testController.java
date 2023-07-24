package com.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Levantosina
 */
@RestController
public class testController {
    record test(String name){}
    private static  int COUNTER=0;
    @GetMapping("/test2")
    public test testController() {
        return  new test("testController %s".formatted(++COUNTER));
    }
}
