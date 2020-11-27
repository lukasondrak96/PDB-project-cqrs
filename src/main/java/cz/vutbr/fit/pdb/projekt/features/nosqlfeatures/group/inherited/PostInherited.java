package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.inherited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInherited {
    private String title;
    private String text;
    private Date createdAt;
    private CreatorInherited creator;
    private List<CommentInherited> comments;
}
