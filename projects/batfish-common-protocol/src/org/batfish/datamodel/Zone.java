package org.batfish.datamodel;

import java.util.SortedMap;
import java.util.TreeMap;

import org.batfish.common.util.ComparableStructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Zone extends ComparableStructure<String> {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private IpAccessList _fromHostFilter;

   private IpAccessList _inboundFilter;

   private SortedMap<String, IpAccessList> _inboundInterfaceFilters;

   private IpAccessList _toHostFilter;

   private SortedMap<String, IpAccessList> _toZonePolicies;

   @JsonCreator
   public Zone(@JsonProperty(NAME_VAR) String name) {
      super(name);
   }

   public Zone(String name, IpAccessList inboundFilter,
         IpAccessList fromHostFilter, IpAccessList toHostFilter) {
      super(name);
      _inboundFilter = inboundFilter;
      _inboundInterfaceFilters = new TreeMap<>();
      _fromHostFilter = fromHostFilter;
      _toHostFilter = toHostFilter;
      _toZonePolicies = new TreeMap<>();
   }

   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      }
      Zone other = (Zone) object;
      if (!this._fromHostFilter.equals(other._fromHostFilter)) {
         return false;
      }
      if (!this._inboundFilter.equals(other._inboundFilter)) {
         return false;
      }
      if (!this._inboundInterfaceFilters
            .equals(other._inboundInterfaceFilters)) {
         return false;
      }
      if (!this._toHostFilter.equals(other._toHostFilter)) {
         return false;
      }
      if (!this._toZonePolicies.equals(other._toZonePolicies)) {
         return false;
      }
      return true;
   }

   public IpAccessList getFromHostFilter() {
      return _fromHostFilter;
   }

   public IpAccessList getInboundFilter() {
      return _inboundFilter;
   }

   public SortedMap<String, IpAccessList> getInboundInterfaceFilters() {
      return _inboundInterfaceFilters;
   }

   public IpAccessList getToHostFilter() {
      return _toHostFilter;
   }

   public SortedMap<String, IpAccessList> getToZonePolicies() {
      return _toZonePolicies;
   }

   public void setFromHostFilter(IpAccessList fromHostFilter) {
      _fromHostFilter = fromHostFilter;
   }

   public void setInboundFilter(IpAccessList inboundFilter) {
      _inboundFilter = inboundFilter;
   }

   public void setInboundInterfaceFilters(
         SortedMap<String, IpAccessList> inboundInterfaceFilters) {
      _inboundInterfaceFilters = inboundInterfaceFilters;
   }

   public void setToHostFilter(IpAccessList toHostFilter) {
      _toHostFilter = toHostFilter;
   }

   public void setToZonePolicies(
         SortedMap<String, IpAccessList> toZonePolicies) {
      _toZonePolicies = toZonePolicies;
   }

   public boolean unorderedEqual(Object object) {
      if (this == object) {
         return true;
      }
      Zone other = (Zone) object;
      if (this.equals(other)) {
         return true;
      }
      if (!this._fromHostFilter.unorderedEqual(other._fromHostFilter)) {
         return false;
      }
      if (!this._inboundFilter.unorderedEqual(other._inboundFilter)) {
         return false;
      }
      if (unorderedEqualSortedMap(this._inboundInterfaceFilters,
            other._inboundInterfaceFilters)) {
         return false;
      }
      if (!this._toHostFilter.unorderedEqual(other._toHostFilter)) {
         return false;
      }
      if (unorderedEqualSortedMap(this._toZonePolicies,
            other._toZonePolicies)) {
         return false;
      }
      return true;
   }

   private boolean unorderedEqualSortedMap(SortedMap<String, IpAccessList> a,
         SortedMap<String, IpAccessList> b) {
      if (!a.keySet().equals(b.keySet())) {
         return false;
      }
      for (String s : a.keySet()) {
         if (!a.get(s).unorderedEqual(b.get(s))) {
            return false;
         }
      }
      return true;
   }

}
