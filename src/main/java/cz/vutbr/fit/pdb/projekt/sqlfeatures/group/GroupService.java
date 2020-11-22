package cz.vutbr.fit.pdb.projekt.sqlfeatures.group;

import org.springframework.http.ResponseEntity;

public interface GroupService {

    ResponseEntity<?> createNewGroup(GroupDto group);

}
