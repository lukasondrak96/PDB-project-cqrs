package cz.vutbr.fit.pdb.projekt.commands.services;

import cz.vutbr.fit.pdb.projekt.features.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCommandService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDocumentRepository userDocumentRepository;

    public PersistentUser saveUser(PersistentUser user) {
        if(user instanceof UserTable)
            return userRepository.save((UserTable) user);
        else
            return userDocumentRepository.save((UserDocument) user);
    }
}
