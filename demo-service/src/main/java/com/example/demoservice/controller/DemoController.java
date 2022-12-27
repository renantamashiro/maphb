package com.example.demoservice.controller;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

//@RestController
class DemoController {
//
//    @ConfigurationProperties(prefix = "demo")
//    record DemoProperties(String message) {}
//
//    private final DemoProperties demoProperties;
//
//    @Autowired
//    private Connection connection;
//
//    DemoController(DemoProperties demoProperties) {
//        this.demoProperties = demoProperties;
//    }
//
//    @GetMapping("/")
//    Mono<String> getMessage() throws IOException {
//        Table table = connection.getTable(TableName.valueOf("test"));
//
//        ResultScanner results = table.getScanner(new Scan());
//        for (Result r : results) {
//            System.out.println(Bytes.toString(r.getValue(Bytes.toBytes("cf"), Bytes.toBytes("a"))));
//        }
//
//        results.close();
//
//        return Mono.just(this.demoProperties.message());
//    }
}
