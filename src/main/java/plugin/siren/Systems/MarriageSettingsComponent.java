package plugin.siren.Systems;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.siren.Marriage;

import javax.annotation.Nullable;
import java.util.UUID;

public class MarriageSettingsComponent implements Component<EntityStore> {

    public static final BuilderCodec<MarriageSettingsComponent> CODEC = BuilderCodec.builder(MarriageSettingsComponent.class, MarriageSettingsComponent::new)
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

    public static ComponentType<EntityStore, MarriageSettingsComponent> getComponentType(){
        return Marriage.get().getMarriageSettingsComponentType();
    }

    public MarriageSettingsComponent(){
        this.married = false;
        this.partnerUUID = null;
        this.partnerUsername = "";
    }

    public MarriageSettingsComponent(MarriageSettingsComponent other){
        this.married = other.married;
        this.partnerUUID = other.partnerUUID;
        this.partnerUsername = other.partnerUsername;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new MarriageSettingsComponent(this);
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
