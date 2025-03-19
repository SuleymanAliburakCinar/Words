package com.example.demo.mapper;

import java.util.List;

public interface GenericMapper<D, E> {
    E mapToEntity(D dto);
    D mapToDto(E entity);
    List<E> mapListToEntity(List<D> listDto);
    List<D> mapListToDto(List<E> listEntity);
}
