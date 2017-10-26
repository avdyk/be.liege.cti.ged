package be.liege.cti.ged.api.search;

import java.time.LocalDateTime;

public interface RangeWithStartDate {

    RangeDate end(final LocalDateTime end);
}
