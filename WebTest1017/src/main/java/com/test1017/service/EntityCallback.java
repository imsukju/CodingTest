package com.test1017.service;

import java.util.List;

public interface EntityCallback<T> {
    
    void post(T entity);
    
    T findById(Long id);
    
    List<T> findAll();
    
    void update(T entity,String title, String text);
    
    void delete(T entity);
}