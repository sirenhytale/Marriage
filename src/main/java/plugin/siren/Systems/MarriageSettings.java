package plugin.siren.Systems;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.UUID;

public class MarriageSettings implements Component<EntityStore> {

    public static final BuilderCodec<MarriageSettings> CODEC = BuilderCodec.builder(MarriageSettings.class, MarriageSettings::new)
            .append(new KeyedCodec<Boolean>("Married?", Codec.BOOLEAN),
                    (marSettings, mBool) -> marSettings.married = mBool, // Setter
                    (marSettings) -> marSettings.married)                    // Getter
            .add()
            .append(new KeyedCodec<>("PartnerUUID", Codec.UUID_STRING),
                    (marSettings, pUUID) -> marSettings.partnerUUID = pUUID, // Setter
                    (marSettings) -> marSettings.partnerUUID)                    // Getter
            .add()
            .append(new KeyedCodec<String>("PartnerUsername", Codec.STRING),
                    (marSettings, puStr) -> marSettings.partnerUsername = puStr, // Setter
                    (marSettings) -> marSettings.partnerUsername)                    // Getter
            .add()
            .build();

    private boolean married;
    private UUID partnerUUID;
    private String partnerUsername;

    public MarriageSettings(){
        this.married = false;
        this.partnerUUID = null;
        this.partnerUsername = "";
    }

    public MarriageSettings(MarriageSettings other){
        this.married = other.married;
        this.partnerUUID = other.partnerUUID;
        this.partnerUsername = other.partnerUsername;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new MarriageSettings(this);
    }

    public boolean isMarried(){
        return this.married;
    }

    public void setMarried(boolean married){
        this.married = married;
    }

    public UUID getPartnerUUID(){
        return this.partnerUUID;
    }

    public void setPartnerUUID(UUID partnerUUID){
        this.partnerUUID = partnerUUID;
    }

    public void clearPartnerUUID(){
        this.partnerUUID = null;
    }

    public String getPartnerUsername(){
        return this.partnerUsername;
    }

    public void setPartnerUsername(String partnerUsername) {
        this.partnerUsername = partnerUsername;
    }
}
