package org.example;

public interface RequestHandler {
    Response process(Request request) throws Exception;
}