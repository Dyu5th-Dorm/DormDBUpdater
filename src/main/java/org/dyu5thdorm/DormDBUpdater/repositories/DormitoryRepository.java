package org.dyu5thdorm.DormDBUpdater.repositories;

public interface DormitoryRepository<T> {
    boolean exists(T t);
    void insert(T t);
    void update(T t);
    void delete(T t);
}
