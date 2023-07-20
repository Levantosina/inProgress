package com.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Levantosina
 */
@RestController
public class TestControlle {
    record Testing(String name) {
    }
    @GetMapping("/test")
    public Testing getTesting() {
        return new  Testing("Brrr");
    }
}
