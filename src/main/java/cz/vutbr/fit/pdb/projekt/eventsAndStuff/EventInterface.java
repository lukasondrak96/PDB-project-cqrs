package cz.vutbr.fit.pdb.projekt.eventsAndStuff;

import cz.vutbr.fit.pdb.projekt.features.PersistentUser;

public interface EventInterface {

    PersistentUser apply(PersistentUser persistentBooking);

    PersistentUser reverse(PersistentUser persistentBooking);

}
