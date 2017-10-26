package be.liege.cti.ged.api.search;

import java.time.LocalDateTime;

public interface RangeDate extends Range {

    LocalDateTime getStart();

    LocalDateTime getEnd();
}
