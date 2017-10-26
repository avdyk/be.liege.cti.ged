package be.liege.cti.ged.api.search;

import java.time.LocalDateTime;

public interface AlfredRangeBuilder {

    RangeWithStartInt start(final int start);

    RangeWithStartDate start(final LocalDateTime start);

}
