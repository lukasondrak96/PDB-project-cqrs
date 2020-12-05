package cz.vutbr.fit.pdb.projekt.api.queries.services;

import cz.vutbr.fit.pdb.projekt.api.AbstractServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractQueryServiceTest extends AbstractServiceTest {

    @Autowired
    public UserQueryService userQueryService;

    @Autowired
    public GroupQueryService groupQueryService;

    @Autowired
    public PostQueryService postQueryService;

    @Autowired
    public MessageQueryService messageQueryService;
}
