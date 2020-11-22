package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.inherited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInherited {

    private String text;
    private Date createdAt;
    private AuthorInherited author;
}
