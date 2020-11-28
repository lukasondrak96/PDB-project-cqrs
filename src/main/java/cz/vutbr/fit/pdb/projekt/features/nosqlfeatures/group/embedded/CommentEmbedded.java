package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEmbedded {

    private String text;
    private Date createdAt;
    private CreatorEmbedded creator;
}
