package cz.vutbr.fit.pdb.projekt.commands.services;

import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupCommandService {

    private final GroupRepository groupRepository;
    // todo private final UserDocumentRepository userDocumentRepository;

    public PersistentGroup updateGroup(PersistentGroup persistentGroup) {
        //todo
        return null;
    }

    public PersistentGroup saveGroup(PersistentGroup persistentGroup) {
        //todo
        return null;
    }

    public PersistentGroup deleteGroup(PersistentGroup persistentGroup) {
        return null;
    }
}
