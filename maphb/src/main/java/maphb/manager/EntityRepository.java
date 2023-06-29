package maphb.manager;

import org.apache.hadoop.hbase.filter.FilterList;

import java.util.List;

public interface EntityRepository<T> {

    T get(String rowKey);

    T put(T object);

    List<T> put(List<T> listOfObjects);

    void delete(String rowKey);

    List<T> scan(FilterList filterList);

}
