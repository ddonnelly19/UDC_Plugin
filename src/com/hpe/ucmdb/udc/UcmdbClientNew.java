package com.hpe.ucmdb.udc;

import com.hp.ucmdb.api.discovery.services.DDMConfigurationService;
import com.hp.ucmdb.api.discovery.types.DiscoveryResType;

public class UcmdbClientNew extends UcmdbClient {
    public UcmdbClientNew(final ServerConfig serverConfig) throws Exception {
        super(serverConfig);
    }

    public void setResourceContent(final DiscoveryResType type, final String name, final byte[] content) {
        final DDMConfigurationService ddmc = this.ucmdbService.getDDMConfigurationService();
        ddmc.updateResourceData(this.toApiResourceType(type), name, content);
    }

    public byte[] getResourceContent(final DiscoveryResType type, final String name) {
        final DDMConfigurationService ddmc = this.ucmdbService.getDDMConfigurationService();
        return ddmc.getResourceData(this.toApiResourceType(type), name);
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public List<String> getAllResourceNamesByType(final DiscoveryResType type) {
//        final DDMConfigurationService ddmc = this.ucmdbService.getDDMConfigurationService();
//        Collection<DiscoveryResData> discoveryResDatas = null;
//        try {
//            discoveryResDatas = (Collection<DiscoveryResData>)ddmc.listResources(this.toApiResourceType(type));
//        }
//        catch (Throwable e) {
//            e.printStackTrace();
//        }
//        final List<String> list = new ArrayList<String>();
//        for (final DiscoveryResData resData : discoveryResDatas) {
//            list.add(resData.getResourceName());
//        }
//        return list;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private DiscoveryResType toApiResourceType(final DiscoveryResType type) {
        return DiscoveryResType.valueOf(type.name());
    }
}