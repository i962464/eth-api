package org.pundi.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月10日 18:26:00
 */
public class ObjectMappingUtil {
  private static final ModelMapper modelMapper;

  static {
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
  }

  private ObjectMappingUtil() {

  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  /**
   * <p>Note: outClass object must have default constructor with no arguments</p>
   *
   * @param <D>         type of result object.
   * @param <T>         type of source object to map from.
   * @param entity      entity that needs to be mapped.
   * @param outputClass class of result object.
   * @return new object of <code>outClass</code> type.
   */
  public static <D, T> D map(final T entity, Class<D> outputClass) {

    return modelMapper.map(entity, outputClass);
  }

  /**
   * <p>Note: outClass object must have default constructor with no arguments</p>
   *
   * @param entityList  list of entities that needs to be mapped
   * @param outputClass class of result list element
   * @param <D>         type of objects in result list
   * @param <T>         type of entity in <code>entityList</code>
   * @return list of mapped object with <code><D></code> type.
   */
  public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outputClass) {

    return entityList.stream()
        .map(entity -> map(entity, outputClass))
        .collect(Collectors.toList());
  }

  /**
   * Maps {@code source} to {@code destination}.
   *
   * @param source      object to map from
   * @param destination object to map to
   */
  public static <S, D> D map(final S source, D destination) {

    modelMapper.map(source, destination);

    return destination;
  }
}
