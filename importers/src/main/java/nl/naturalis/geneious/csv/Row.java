package nl.naturalis.geneious.csv;

import java.util.EnumMap;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import nl.naturalis.common.collection.EnumToIntMap;

public class Row<T extends Enum<T>> extends EnumMap<T, String> {

  public Row(Class<T> keyType, EnumToIntMap<T> columnNumbers, String[] columnValues) {
    super(keyType);
    for (T col : columnNumbers.keySet()) {
      int colnum = columnNumbers.get(col);
      if (colnum < columnValues.length) {
        put(col, StringUtils.trimToNull(columnValues[colnum]));
      }
    }
  }

  public boolean isEmpty() {
    return values().stream().allMatch(Objects::isNull);
  }

  @SuppressWarnings("unchecked")
  public boolean hasValueFor(T... columns) {
    for (T column : columns) {
      if (get(column) == null) {
        return false;
      }
    }
    return true;
  }
}
