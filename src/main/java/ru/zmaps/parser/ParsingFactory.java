package ru.zmaps.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParsingFactory {

    ExecutorService executor = Executors.newFixedThreadPool(4);

    public Future<?> addTask(Runnable task){
        return executor.submit(task);
    }

}
