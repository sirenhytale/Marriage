package plugin.siren.Utils;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import plugin.siren.Marriage;

public class MarriageConfig {

    public static final BuilderCodec<MarriageConfig> CODEC = BuilderCodec.builder(MarriageConfig.class, MarriageConfig::new)
            .append(new KeyedCodec<String>("Config-Information", Codec.STRING),
                    (merConfig, ciStr, extraInfo) -> merConfig.Information = ciStr, // Setter
                    (merConfig, extraInfo) -> merConfig.Information)                    // Getter
            .add()
            .append(new KeyedCodec<Integer>("ConfigVersion", Codec.INTEGER),
                    (merConfig, cvInt, extraInfo) -> merConfig.ConfigVersion = cvInt, // Setter
                    (merConfig, extraInfo) -> merConfig.ConfigVersion)                    // Getter
            .add()
            .append(new KeyedCodec<String>("PluginName", Codec.STRING),
                    (merConfig, pnStr, extraInfo) -> merConfig.PluginName = pnStr, // Setter
                    (merConfig, extraInfo) -> merConfig.PluginName)                    // Getter
            .add()
            .append(new KeyedCodec<String>("Version", Codec.STRING),
                    (merConfig, vStr, extraInfo) -> merConfig.Version = vStr, // Setter
                    (merConfig, extraInfo) -> merConfig.Version)                    // Getter
            .add()
            .append(new KeyedCodec<String>("Website", Codec.STRING),
                    (merConfig, wStr, extraInfo) -> merConfig.Website = wStr, // Setter
                    (merConfig, extraInfo) -> merConfig.Website)                    // Getter
            .add()
            .append(new KeyedCodec<String>("Download-Site", Codec.STRING),
                    (merConfig, dsStr, extraInfo) -> merConfig.DownloadSite = dsStr, // Setter
                    (merConfig, extraInfo) -> merConfig.DownloadSite)                    // Getter
            .add()
            .append(new KeyedCodec<Boolean>("Require-Ring-To-Marry", Codec.BOOLEAN),
                    (marConfig, rrtmBool, extraInfo) -> marConfig.RequireRing = rrtmBool, // Setter
                    (marConfig, extraInfo) -> marConfig.RequireRing)                    // Getter
            .add()
            .append(new KeyedCodec<Boolean>("Use-Permissions-For-Commands", Codec.BOOLEAN),
                    (marConfig, upfcBool, extraInfo) -> marConfig.CmdPermission = upfcBool, // Setter
                    (marConfig, extraInfo) -> marConfig.CmdPermission)                    // Getter
            .add()
            .append(new KeyedCodec<Boolean>("New-Version-Message", Codec.BOOLEAN),
                    (merConfig, nvmBool, extraInfo) -> merConfig.NewVersion = nvmBool, // Setter
                    (merConfig, extraInfo) -> merConfig.NewVersion)                    // Getter
            .add()
            .build();

    private String InformationDefault = "Confused about what one of these statement do? Go to https://mermaids.dev/marriage/config/ or check out the Marriage page on the Curseforge website and scroll down to Config Extra Info.";
    private String Information = InformationDefault;
    private final int ConfigVersionDefault = 3;
    private int ConfigVersion = ConfigVersionDefault;
    private String PluginName = "Marriage";
    private String Version = Marriage.getVersion();
    private String WebsiteDefault = "https://mermaids.dev/marriage/";
    private String Website = WebsiteDefault;
    private String DownloadSiteDefault = "https://www.curseforge.com/hytale/mods/marriage";
    private String DownloadSite = DownloadSiteDefault;
    private boolean RequireRing = false;
    private boolean CmdPermission = false;
    private boolean NewVersion = true;

    public MarriageConfig() {}

    public boolean ifConfigUpdate(){
        boolean configUpdated = false;

        if(ConfigVersionDefault > ConfigVersion){
            configUpdated = true;
            ConfigVersion = ConfigVersionDefault;
        }
        if(!Version.equalsIgnoreCase(Marriage.getVersion())){
            configUpdated = true;
            Version = Marriage.getVersion();
        }
        if(!Information.equalsIgnoreCase(InformationDefault)){
            configUpdated = true;
            Information = InformationDefault;
        }
        if(!Website.equalsIgnoreCase(WebsiteDefault)){
            configUpdated = true;
            Website = WebsiteDefault;
        }
        if(!DownloadSite.equalsIgnoreCase(DownloadSiteDefault)){
            configUpdated = true;
            DownloadSite = DownloadSiteDefault;
        }

        return configUpdated;
    }

    public int getConfigVersionDefault(){
        return this.ConfigVersionDefault;
    }

    public int getConfigVersion(){
        return this.ConfigVersion;
    }

    public void setConfigVersion(int version){
        this.ConfigVersion = version;
    }

    public String getPluginVersion(){
        return this.Version;
    }

    public void setPluginVersion(String version){
        this.Version = version;
    }

    public boolean ifRequireRing(){
        return this.RequireRing;
    }

    public void setRequireRing(boolean requireRing){
        this.RequireRing = requireRing;
    }

    public boolean ifCmdPermission(){
        return this.CmdPermission;
    }

    public boolean ifNewVersion(){
        return NewVersion;
    }
}
