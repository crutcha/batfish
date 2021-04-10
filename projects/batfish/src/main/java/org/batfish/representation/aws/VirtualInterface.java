package org.batfish.representation.aws;

import static org.batfish.representation.aws.Utils.checkNonNull;

import com.google.common.collect.ImmutableList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.batfish.common.Warnings;
import org.batfish.datamodel.Configuration;

@JsonIgnoreProperties(ignoreUnknown = true)
@ParametersAreNonnullByDefault
public class VirtualInterface implements AwsVpcEntity, Serializable {

  @Nonnull protected final String _ownerAccount;

  @Nonnull protected final String _virtualInterfaceId;

  @Nonnull protected final String _location;

  @Nonnull protected final String _connectionId;

  @Nonnull protected final String _virtualInterfaceType;

  @Nonnull protected final String _virtualInterfaceName;

  // TODO: validation is valid?
  @Nonnull protected final int _vlan;

  @Nonnull protected final int _asn;

  @Nonnull protected final int _amazonSideAsn;

  @Nonnull protected final String _amazonAddress;

  @Nonnull protected final String _customerAddress;

  @Nonnull protected final String _addressFamily;

  @Nonnull protected final String _virtualInterfaceState;

  @Nonnull protected final int _mtu;

  // TODO: this can actually be null?
  @Nonnull protected final String _virtualGatewayId;

  @Nonnull protected final String _directConnectGatewayId;

  @Nonnull protected final List<String> _routeFilterPrefixes;

  @Nonnull protected final String _region;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @ParametersAreNonnullByDefault
  static final class BgpPeer implements Serializable {
    @Nonnull private final String _bgpPeerId;

    @Nonnull private final String _asn;

    @Nonnull private final String _addressFamily;

    @Nonnull private final String _amazonAddress;

    @Nonnull private final String _customerAddress;

    @JsonCreator
    private static BgpPeer create(
        @Nullable @JsonProperty(JSON_KEY_BGP_PEER_ID) String bgpPeerId,
        @Nullable @JsonProperty(JSON_KEY_ASN) String asn,
        @Nullable @JsonProperty(JSON_KEY_ADDRESS_FAMILY) String addressFamily,
        @Nullable @JsonProperty(JSON_KEY_AMAZON_ADDRESS) String amazonAddress,
        @Nullable @JsonProperty(JSON_KEY_CUSTOMER_ADDRESS) String customerAddress) {
      checkNonNull(bgpPeerId, JSON_KEY_BGP_PEER_ID, "Virtual Interface");
      checkNonNull(asn, JSON_KEY_ASN, "Virtual Interface");
      checkNonNull(addressFamily, JSON_KEY_ADDRESS_FAMILY, "Virtual Interface");
      checkNonNull(amazonAddress, JSON_KEY_AMAZON_ADDRESS, "Virtual Interface");
      checkNonNull(customerAddress, JSON_KEY_CUSTOMER_ADDRESS, "Virtual Interface");

      return new BgpPeer(bgpPeerId, asn, addressFamily, amazonAddress, customerAddress);
    }

    BgpPeer(String bgpPeerId, String asn, String addressFamily, String amazonAddress, String customerAddress) {
      _bgpPeerId = bgpPeerId;
      _asn = asn;
      _addressFamily = addressFamily;
      _amazonAddress = amazonAddress;
      _customerAddress = customerAddress;
    }
  }

  @Nonnull private final List<BgpPeer> _bgpPeers;

  // TODO: this overlaps with AllowedPrefix, should this just be its own object used by this and DXG stuff?
  @ParametersAreNonnullByDefault
  private static final class RouteFilterPrefix {
    @Nonnull private final String _cidr;

    @JsonCreator
    private static RouteFilterPrefix create(@Nullable @JsonProperty(JSON_KEY_CIDR) String cidr) {
      //checkArgument()
      // TODO: validations?
      return new RouteFilterPrefix(cidr);
    }

    private RouteFilterPrefix(String cidr) {
      _cidr = cidr;
    }

    @Nonnull
    String getCidr() { return _cidr; }
  }

  @JsonCreator
  private static VirtualInterface create(
      @Nullable @JsonProperty(JSON_KEY_OWNER_ACCOUNT) String ownerAccount,
      @Nullable @JsonProperty(JSON_KEY_VIRTUAL_INTERFACE_ID) String virtualInterfaceId,
      @Nullable @JsonProperty(JSON_KEY_LOCATION) String location,
      @Nullable @JsonProperty(JSON_KEY_CONNECTION_ID) String connectionId,
      @Nullable @JsonProperty(JSON_KEY_VIRTUAL_INTERFACE_TYPE) String virtualInterfaceType,
      @Nullable @JsonProperty(JSON_KEY_VIRTUAL_INTERFACE_NAME) String virtualInterfaceName,
      @Nullable @JsonProperty(JSON_KEY_VLAN) int vlan,
      @Nullable @JsonProperty(JSON_KEY_ASN) int asn,
      @Nullable @JsonProperty(JSON_KEY_AMAZON_SIDE_ASN) int amazonSideAsn,
      @Nullable @JsonProperty(JSON_KEY_AMAZON_ADDRESS) String amazonAddress,
      @Nullable @JsonProperty(JSON_KEY_CUSTOMER_ADDRESS) String customerAddress,
      @Nullable @JsonProperty(JSON_KEY_ADDRESS_FAMILY) String addressFamily,
      @Nullable @JsonProperty(JSON_KEY_VIRTUAL_INTERFACE_STATE) String virtualInterfaceState,
      @Nullable @JsonProperty(JSON_KEY_MTU) int mtu,
      @Nullable @JsonProperty(JSON_KEY_VIRTUAL_GATEWAY_ID) String virtualGatewayId,
      @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_ID) String directConnectGatewayId,
      @Nullable @JsonProperty(JSON_KEY_BGP_PEERS) List<BgpPeer> bgpPeers,
      @Nullable @JsonProperty(JSON_KEY_ROUTE_FILTER_PREFIXES) List<RouteFilterPrefix> routeFilterPrefixes,
      @Nullable @JsonProperty(JSON_KEY_REGION) String region) {
    // TODO: validation things
    checkNonNull(ownerAccount, JSON_KEY_OWNER_ACCOUNT, "Virtual Interface");
    checkNonNull(virtualInterfaceId, JSON_KEY_VIRTUAL_INTERFACE_ID, "Virtual Interface");
    checkNonNull(connectionId, JSON_KEY_CONNECTION_ID, "Virtual Interface");
    checkNonNull(virtualInterfaceType, JSON_KEY_VIRTUAL_INTERFACE_TYPE, "Virtual Interface");
    checkNonNull(virtualInterfaceName, JSON_KEY_VIRTUAL_INTERFACE_NAME, "Virtual Interface");
    checkNonNull(vlan, JSON_KEY_VLAN, "Virtual Interface");
    checkNonNull(asn, JSON_KEY_ASN, "Virtual Interface");
    checkNonNull(amazonAddress, JSON_KEY_AMAZON_ADDRESS, "Virtual Interface");
    checkNonNull(customerAddress, JSON_KEY_CUSTOMER_ADDRESS, "Virtual Interface");
    checkNonNull(addressFamily, JSON_KEY_ADDRESS_FAMILY, "Virtual Interface");
    checkNonNull(directConnectGatewayId, JSON_KEY_DIRECT_CONNECT_GATEWAY_ID, "Virtual Interface");
    checkNonNull(region, JSON_KEY_REGION, "Virtual Interface");

    return new VirtualInterface(
        ownerAccount, virtualInterfaceId, location, connectionId, virtualInterfaceType,
        virtualInterfaceName, vlan, asn, amazonSideAsn, amazonAddress, customerAddress, addressFamily,
        virtualInterfaceState, mtu, virtualGatewayId, directConnectGatewayId, routeFilterPrefixes, bgpPeers,
        region);
  }

  VirtualInterface(
      String ownerAccount,
      String virtualInterfaceId,
      String location,
      String connectionId,
      String virtualInterfaceType,
      String virtualInterfaceName,
      int vlan,
      int asn,
      int amazonSideAsn,
      String amazonAddress,
      String customerAddress,
      String addressFamily,
      String virtualInterfaceState,
      int mtu,
      String virtualGatewayId,
      String directConnectGatewayId,
      List<RouteFilterPrefix> routeFilterPrefixes,
      List<BgpPeer> bgpPeers,
      String region
  ) {
    _ownerAccount = ownerAccount;
    _virtualInterfaceId = virtualInterfaceId;
    _location = location;
    _connectionId = connectionId;
    _virtualInterfaceType = virtualInterfaceType;
    _virtualInterfaceName = virtualInterfaceName;
    _vlan = vlan;
    _asn = asn;
    _amazonSideAsn = amazonSideAsn;
    _amazonAddress = amazonAddress;
    _customerAddress = customerAddress;
    _addressFamily = addressFamily;
    _virtualInterfaceState = virtualInterfaceState;
    _mtu = mtu;
    _virtualGatewayId = virtualGatewayId;
    _directConnectGatewayId = directConnectGatewayId;
    _routeFilterPrefixes = routeFilterPrefixes
        .stream()
        .map(RouteFilterPrefix::getCidr)
        .collect(ImmutableList.toImmutableList());
    _bgpPeers = bgpPeers;
    _region = region;
  }

  @Nonnull
  @Override
  public String getId() {
    return _virtualInterfaceId;
  }

  public String getDirectConnectGatewayId() {
    return _directConnectGatewayId;
  }

  public String getConnectId() {
    return _connectionId;
  }

  public String getInterfaceAddress() {
    return _amazonAddress;
  }

  Configuration toConfigurationNode(
      ConvertedConfiguration awsConfiguration, Region region, Warnings warnings) {
    Configuration cfgNode = Utils.newAwsConfiguration(_virtualInterfaceId, "aws");
    return cfgNode;
  }
}
