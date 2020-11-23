package cz.vutbr.fit.pdb.projekt.events.events.message;


import cz.vutbr.fit.pdb.projekt.commands.services.MessageCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentMessage;

public class MessageDeletedEvent implements EventInterface<PersistentMessage> {

    private final MessageCommandService service;

    public MessageDeletedEvent(MessageCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentMessage apply(PersistentMessage persistentMessage) {
        return service.deleteMessage(persistentMessage);
    }

    @Override
    public PersistentMessage reverse(PersistentMessage persistentMessage) {
        return null;
    }
}
