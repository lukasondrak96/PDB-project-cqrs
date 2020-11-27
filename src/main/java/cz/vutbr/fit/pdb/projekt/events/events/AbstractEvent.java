package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandService;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractEvent<T extends PersistentObject> implements EventInterface<T> {
    private ObjectInterface objectInterface;
    protected CommandService<T> commandService;

    public AbstractEvent(ObjectInterface objectInterface) {
        this.objectInterface = objectInterface;
    }

    @Override
    public String toString() {
        return "Event for " + objectInterface;
    }
}
