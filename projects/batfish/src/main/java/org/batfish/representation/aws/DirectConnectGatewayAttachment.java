package org.batfish.representation.aws;

import static org.batfish.representation.aws.Utils.checkNonNull;

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
public class DirectConnectGatewayAttachment implements AwsVpcEntity, Serializable {

  @Nonnull protected final String _directConnectGatewayId;

  @Nonnull protected final String _virtualInterfaceId;

  @Nonnull protected final String _virtualInterfaceRegion;

  @Nonnull protected final String _virtualInterfaceOwnerAccount;

  @Nonnull protected final String _attachmentState;

  @Nonnull protected final String _attachmentType;

  @JsonCreator
  private static DirectConnectGatewayAttachment create(
     @Nullable @JsonProperty(JSON_KEY_DIRECT_CONNECT_GATEWAY_ID) String directConnectGatewayId,
     @Nullable @JsonProperty(JSON_KEY_VIRTUAL_INTERFACE_ID) String virtualInterfaceId,
     @Nullable @JsonProperty(JSON_KEY_VIRTUAL_INTERFACE_REGION) String virtualInterfaceRegion,
     @Nullable @JsonProperty(JSON_KEY_VIRTUAL_INTERFACE_OWNER_ACCOUNT) String virtualInterfaceOwnerAccount,
     @Nullable @JsonProperty(JSON_KEY_ATTACHMENT_STATE) String attachmentState,
     @Nullable @JsonProperty(JSON_KEY_ATTACHMENT_TYPE) String attachmentType) {
    checkNonNull(directConnectGatewayId, JSON_KEY_DIRECT_CONNECT_GATEWAY_ID, "Direct Connect Gateway Attachment");
    checkNonNull(virtualInterfaceId, JSON_KEY_VIRTUAL_INTERFACE_ID, "Direct Connect Gateway Attachment");
    checkNonNull(virtualInterfaceRegion, JSON_KEY_VIRTUAL_INTERFACE_REGION, "Direct Connect Gateway Attachment");
    checkNonNull(virtualInterfaceOwnerAccount, JSON_KEY_VIRTUAL_INTERFACE_OWNER_ACCOUNT, "Direct Connect Gateway Attachment");
    checkNonNull(attachmentState, JSON_KEY_ATTACHMENT_STATE, "Direct Connect Gateway Attachment");
    checkNonNull(attachmentType, JSON_KEY_ATTACHMENT_TYPE, "Direct Connect Gateway Attachment");

    return new DirectConnectGatewayAttachment(
        directConnectGatewayId,
        virtualInterfaceId,
        virtualInterfaceRegion,
        virtualInterfaceOwnerAccount,
        attachmentState,
        attachmentType);
  }

  public DirectConnectGatewayAttachment(
      String directConnectGatewayId,
      String virtualInterfaceId,
      String virtualInterfaceRegion,
      String virtualInterfaceOwnerAccount,
      String attachmentState,
      String attachmentType) {
    _directConnectGatewayId = directConnectGatewayId;
    _virtualInterfaceId = virtualInterfaceId;
    _virtualInterfaceRegion = virtualInterfaceRegion;
    _virtualInterfaceOwnerAccount = virtualInterfaceOwnerAccount;
    _attachmentState = attachmentState;
    _attachmentType = attachmentType;
  }

  @Nonnull
  @Override
  public String getId() {
    // TODO: no unique id for this?
    // what if virtualInterfaceId doesn't exist? could be another attachmentType
    return String.format("%s:%s", _directConnectGatewayId, _virtualInterfaceId);
  }

  Configuration toConfigurationNode(
      ConvertedConfiguration awsConfiguration, Region region, Warnings warnings) {
    // TODO: does this really need it's own device type??
    Configuration cfgNode = Utils.newAwsConfiguration(_directConnectGatewayId, "aws");
    return cfgNode;
  }
}
