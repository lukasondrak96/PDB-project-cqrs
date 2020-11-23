package cz.vutbr.fit.pdb.projekt.events.events.group;

import cz.vutbr.fit.pdb.projekt.commands.services.GroupCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentGroup;

public class GroupUpdatedEvent implements EventInterface<PersistentGroup> {

    private final GroupCommandService service;

    public GroupUpdatedEvent(GroupCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentGroup apply(PersistentGroup persistentGroup) {
        return service.updateGroup(persistentGroup);
    }

    @Override
    public PersistentGroup reverse(PersistentGroup persistentGroup) {
        return null;
    }
}
