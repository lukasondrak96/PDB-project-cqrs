package cz.vutbr.fit.pdb.projekt.sqlfeatures.group;

import cz.vutbr.fit.pdb.projekt.nosqlfeatures.user.GroupInheritted;
import cz.vutbr.fit.pdb.projekt.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.nosqlfeatures.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> createNewGroup(GroupDto groupDto) {
        GroupTable group = new GroupTable(groupDto.getName(), groupDto.getDescription(), groupDto.getState());
        groupRepository.save(group);

        GroupInheritted groupInheritted = new GroupInheritted(group.getName());

        UserDocument userDocument = new UserDocument("jemno", "nejm", Collections.singletonList(groupInheritted), Collections.singletonList(groupInheritted));
        userRepository.save(userDocument);
        return ResponseEntity.ok("CAJK");
    }
}
