package plugin.siren.Systems;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import plugin.siren.Marriage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MarriageComponent implements Component<EntityStore> {

    private List<PlayerRef> requestsList;
    private int divorceTimer;

    public static ComponentType<EntityStore, MarriageComponent> getComponentType(){
        return Marriage.get().getMarriageComponentType();
    }

    public MarriageComponent(){
        requestsList = new ArrayList<>();
        divorceTimer = 0;
    }

    public MarriageComponent(MarriageComponent other){
        this.requestsList = other.requestsList;
        this.divorceTimer = other.divorceTimer;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new MarriageComponent(this);
    }

    public List<PlayerRef> getRequestsList(){
        return this.requestsList;
    }

    public void addRequestToList(PlayerRef playerRef){
        this.requestsList.add(playerRef);
    }

    /*public void setRequestsList(List<PlayerRef> list){
        this.requestsList = list;
    }*/

    public void clearRequestsList(){
        this.requestsList = new ArrayList<>();
    }

    public int getDivorceTimer(){
        return divorceTimer;
    }

    public void setDivorceTimer(int time){
        divorceTimer = time;
    }

}
