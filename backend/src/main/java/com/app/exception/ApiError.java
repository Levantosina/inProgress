package com.app.exception;

import java.time.LocalDateTime;

/**
 * @author Levantosina
 */
public record ApiError(
    String path,
    String message,
    int statusCode,
    LocalDateTime localDateTime
){

}