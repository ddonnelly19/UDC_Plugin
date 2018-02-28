package com.hpe.ucmdb.udc;

import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.*;

public class ResTypeUtil {
    private static Map<String, DiscoveryResType> resMap;

    static {
        (ResTypeUtil.resMap = new HashMap<>()).put("discoveryConfigFiles", DiscoveryResType.SERVER_DATA);
        ResTypeUtil.resMap.put("discoveryScripts", DiscoveryResType.SCRIPT);
        ResTypeUtil.resMap.put("discoveryResources", DiscoveryResType.USER_EXT);
        ResTypeUtil.resMap.put("discoveryPatterns", DiscoveryResType.ADAPTER);
        ResTypeUtil.resMap.put("discoveryJobs", DiscoveryResType.JOB);
        ResTypeUtil.resMap.put("discoveryWizards", DiscoveryResType.WIZARD);
        ResTypeUtil.resMap.put("discoveryModules", DiscoveryResType.MODULE);
        ResTypeUtil.resMap.put("class", null);
    }

    public static DiscoveryResType getResType(final String name) {
        return ResTypeUtil.resMap.get(name);
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public static String[] getNames() {
//        return ResTypeUtil.resMap.keySet().toArray(new String[0]);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public static String getResName(final DiscoveryResType type) {
        final Set<Map.Entry<String, DiscoveryResType>> entries = ResTypeUtil.resMap.entrySet();
        for (final Map.Entry<String, DiscoveryResType> x : entries) {
            if (x.getValue() == type) {
                return x.getKey();
            }
        }
        return null;
    }

    public static Object[] getResourceName(final VirtualFile vf) {
        final List<String> paths = new ArrayList<>();
        VirtualFile tmp = vf;
        paths.add(vf.getName());
        DiscoveryResType resourceType = null;
        while (true) {
            final VirtualFile parent = tmp.getParent();
            if (parent == null) {
                break;
            }
            tmp = parent;
            final DiscoveryResType resType = getResType(tmp.getName());
            if (resType != null) {
                resourceType = resType;
                break;
            }
            paths.add(tmp.getName());
        }
        if (resourceType == null && vf.getName().endsWith(".py")) {
            paths.clear();
            paths.add(vf.getName());
            resourceType = DiscoveryResType.SCRIPT;
        }
        if (resourceType != null) {
            Collections.reverse(paths);
            final StringBuilder sb = new StringBuilder();
            for (final String x : paths) {
                sb.append(x).append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
            return new Object[]{resourceType, sb.toString()};
        }
        return null;
    }
}