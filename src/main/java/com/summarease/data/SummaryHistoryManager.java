package com.summarease.data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.summarease.model.SummaryRecord;



public class SummaryHistoryManager {
    private final List<SummaryRecord> history = new ArrayList<>();

    public void addRecord(SummaryRecord record) {
        history.add(record);
    }

    public List<SummaryRecord> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void clearHistory() {
        history.clear();
    }
}
