# maphb
> Object Non-Relational Mapper for Apache HBase (Wide-Column Model)

This repository is an Object Non-Relational Mapper created for simplify HBase Operations in a Java Application.

The HBase native client is built in Java and it is available on https://mvnrepository.com/artifact/org.apache.hbase/hbase-client

## Features
* Implementation of HBase basic operations (get, scan, put, delete)
* Support for rowkey design based
* Schema auto-configuration
* Filter abstraction

## Usage
Try this [Example of usage](https://github.com/renantamashiro/maphb/tree/main/demo-service) that shows how to use MapHB to simplify CRUD operations.


### Basic configuration
MapHb needs a first instatiation to map all models declared at main application (that uses MapHb for persistence in HBase). To do this, you need to call for `startApplication` from `MapHBContext` in your main function:

```java
public class DemoApplication {

  public static void main(String[] args) {
    MapHBContext.startApplication(DemoApplication.class);
  }
}
```

At this moment, MapHB starts a connection with HBase also an entity scanning to create or modify the corresponding HBase Table.


### Entity Mapping
To create a relationship between a java class and hbase tabel, you need to annotate the corresponding class with `@Table` annotation passing as parameters the table name and the column families:

```java
@Table(name = "table_name", columnFamilies = {"cf1", "cf2"}
public class ModelClass {

}

```
For each attribute that represents a column qualifier, just use `@Column` annotation:

```java
@Table(name = "table_name", columnFamilies = {"cf1", "cf2"}
public class ModelClass {

  @Column(family = "cf1", qualifier = "qualifier1")
  private String qualifier;
  
}

```



## TODOs
- [ ] Pagination support
- [ ] Coprocessors




