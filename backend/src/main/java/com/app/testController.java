package com.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Levantosina
 */
@RestController
public class testController {
    record test(String name){}

    @GetMapping("/testTest2")
    public test testController() {
        return  new test("testController");
    }
}
