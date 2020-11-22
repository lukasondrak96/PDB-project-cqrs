package cz.vutbr.fit.pdb.projekt.sqlfeatures.group;

import cz.vutbr.fit.pdb.projekt.nosqlfeatures.user.GroupInheritted;
import cz.vutbr.fit.pdb.projekt.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.nosqlfeatures.user.UserNoSqlRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserNoSqlRepository userNoSqlRepository;

    @Override
    public ResponseEntity<?> createNewGroup(GroupDto groupDto) {
        GroupTable group = new GroupTable(groupDto.getName(), groupDto.getDescription(), groupDto.getState(), null);
        groupRepository.save(group);

        GroupInheritted groupInheritted = new GroupInheritted(group.getName());

        UserDocument userDocument = new UserDocument("jemno", "nejm", Collections.singletonList(groupInheritted), Collections.singletonList(groupInheritted));
        userNoSqlRepository.save(userDocument);
        return ResponseEntity.ok("CAJK");
    }
}
