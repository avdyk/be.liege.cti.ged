package be.liege.cti.ged.api.impl.search;

import be.liege.cti.ged.api.search.RangeInt;

public class RangeIntImpl implements RangeInt {

    private final int start;
    private final int end;

    public RangeIntImpl(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }
}
