package cz.vutbr.fit.pdb.projekt.api.queries.services;

import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.MemberEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupQueryService {
    private final GroupDocumentRepository groupDocumentRepository;

    public ResponseEntity<List<GroupDocument>> getAllGroups() {
        return ResponseEntity.ok().body(groupDocumentRepository.findAll());
    }


    public ResponseEntity<GroupDocument> getInformationAboutGroup(int groupId) {
        Optional<GroupDocument> groupDocumentOptional = groupDocumentRepository.findById(groupId);
        if(groupDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(groupDocumentOptional.get());
    }


    public ResponseEntity<List<PostEmbedded>> getAllPostInGroup(int groupId) {
        Optional<GroupDocument> groupDocumentOptional = groupDocumentRepository.findById(groupId);
        if(groupDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(groupDocumentOptional.get().getPosts());
    }


    public ResponseEntity<List<MemberEmbedded>> getGroupMembers(int groupId) {
        Optional<GroupDocument> groupDocumentOptional = groupDocumentRepository.findById(groupId);
        if(groupDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(groupDocumentOptional.get().getMembers());
    }

    public ResponseEntity<CreatorEmbedded> getGroupAdmin(int groupId) {
        Optional<GroupDocument> groupDocumentOptional = groupDocumentRepository.findById(groupId);
        if(groupDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body((CreatorEmbedded) groupDocumentOptional.get().getCreator());
    }

}
