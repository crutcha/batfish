package org.batfish.representation.aws;

import static org.batfish.representation.aws.Utils.checkNonNull;

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
import org.batfish.datamodel.DeviceModel;
import org.batfish.datamodel.Interface;

@JsonIgnoreProperties(ignoreUnknown = true)
@ParametersAreNonnullByDefault
public class DirectConnectGateway implements AwsVpcEntity, Serializable {

  @Nonnull protected final String _id;

  @Nonnull protected final String _name;

  @Nonnull protected final int _amazonSideAsn;

  @Nonnull protected final String _ownerAccount;

  @Nonnull protected final String _state;

  @JsonCreator
  private static DirectConnectGateway create(
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_ID) String directConnectGatewayId,
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_NAME) String directConnectGatewayName,
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_AMAZON_SIDE_ASN) int amazonSideAsn,
     @Nullable @JsonProperty(JSON_KEY_OWNER_ACCOUNT) String ownerAccount,
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_STATE) String directConnectGatewayState) {
    checkNonNull(directConnectGatewayId, JSON_KEY_DIRECT_CONNECT_GATEWAY_ID, "Direct Connect Gateway");
    checkNonNull(directConnectGatewayName, JSON_KEY_DIRECT_CONNECT_GATEWAY_NAME, "Direct Connect Gateway");
    checkNonNull(amazonSideAsn, JSON_KEY_DIRECT_CONNECT_AMAZON_SIDE_ASN, "Direct Connect Gateway");
    checkNonNull(ownerAccount, JSON_KEY_OWNER_ACCOUNT, "Direct Connect Gateway");
    checkNonNull(directConnectGatewayState, JSON_KEY_DIRECT_CONNECT_GATEWAY_STATE, "Direct Connect Gateway");

    return new DirectConnectGateway(
        directConnectGatewayId,
        directConnectGatewayName,
        amazonSideAsn,
        ownerAccount,
        directConnectGatewayState);
  }

  public DirectConnectGateway(String id, String name, int amazonSideAsn, String ownerAccount, String state) {
    _id = id;
    _name = name;
    _amazonSideAsn = amazonSideAsn;
    _ownerAccount = ownerAccount;
    _state = state;
  }

  @Nonnull
  @Override
  public String getId() {
    return _id;
  }

  Configuration toConfigurationNode(
      ConvertedConfiguration awsConfiguration, List<VirtualInterface> dxVifs, Warnings warnings) {
    Configuration cfgNode = Utils.newAwsConfiguration(getId(), "aws", DeviceModel.AWS_DIRECT_CONNECT_GATEWAY);
    cfgNode.getVendorFamily().getAws().setRegion("global");

    for (VirtualInterface vif: dxVifs) {
      Configuration vifCfgNode = awsConfiguration.getNode(vif.getId());
      dxVifInterface = Interface.builder()
          .setName(vif.getId())
          .setOwner(vifCfgNode)
          .setAddress(vif.getInterfaceAddress())
    }

    return cfgNode;
  }
}
