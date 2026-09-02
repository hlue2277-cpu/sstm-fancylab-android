package com.liuj.huabo.db;

import java.util.List;
import java.util.Objects;

public class TicketRecordByDay {

    public List<TicketRecord> records;

    public String day;

    @Override
    public boolean equals(Object o) {
        TicketRecordByDay that = (TicketRecordByDay) o;
        return day.equals(that.day);
    }


}
