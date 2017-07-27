package org.batfish.datamodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.batfish.common.BatfishException;

public enum OriginType {
  EGP("egp", 1),
  IGP("igp", 2),
  INCOMPLETE("incomplete", 0);

  private static final Map<String, OriginType> _map = buildMap();

  private static Map<String, OriginType> buildMap() {
    Map<String, OriginType> map = new HashMap<>();
    for (OriginType value : OriginType.values()) {
      String name = value._name;
      map.put(name, value);
    }
    return Collections.unmodifiableMap(map);
  }

  @JsonCreator
  public static OriginType fromString(String name) {
    OriginType instance = _map.get(name.toLowerCase());
    if (instance == null) {
      throw new BatfishException("Not a valid OriginType: \"" + name + "\"");
    }
    return instance;
  }

  private final String _name;

  private final int _preference;

  private OriginType(String originType, int preference) {
    _name = originType;
    _preference = preference;
  }

  @JsonValue
  public String getOriginTypeName() {
    return _name;
  }

  public int getPreference() {
    return _preference;
  }
}
