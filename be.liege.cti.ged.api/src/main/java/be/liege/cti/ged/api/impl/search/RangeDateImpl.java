package be.liege.cti.ged.api.impl.search;

import be.liege.cti.ged.api.search.RangeDate;

import java.time.LocalDateTime;

public class RangeDateImpl implements RangeDate {

    private final LocalDateTime start;
    private final LocalDateTime end;

    public RangeDateImpl(final LocalDateTime start, final LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public LocalDateTime getStart() {
        return start;
    }

    @Override
    public LocalDateTime getEnd() {
        return end;
    }

}
