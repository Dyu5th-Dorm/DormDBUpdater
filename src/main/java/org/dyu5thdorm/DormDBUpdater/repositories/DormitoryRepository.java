package org.dyu5thdorm.DormDBUpdater.repositories;

/**
 * Dormitory repository interface.
 * @param <T> Room or Student class.
 */
public interface DormitoryRepository<T> {
    boolean exists(T t);
    void insert(T t);
    void update(T t);
    void delete(T t);
}
