package org.example;

import lombok.Getter;
import org.example.models.Ticket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class TicketTableManger {
    private DefaultTableModel tableModel;
    private JTable table;

    String[] columnNames = {"ID", "creator" , "Title",  "Priority", "Category", "TicketStatus", "CreatedAt"};

    // Constructor to initialize the table
    public TicketTableManger(List<Ticket> tickets) {
        Object[][] data = new Object[tickets.size()][columnNames.length];

        for(int i = 0; i < tickets.size(); i++){
            Ticket ticket = tickets.get(i);
            data[i] = ticketToRowData(ticket);
        }
        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
    }

    public void addTicket(Ticket ticket) {
        tableModel.addRow(ticketToRowData(ticket));
    }

    private Object[] ticketToRowData(Ticket ticket){
        Object[] rowData = {
                ticket.getId(),
                ticket.getCreatorName(),
                ticket.getTitle(),
                ticket.getPriority().toString(),
                ticket.getCategory().toString(),
                ticket.getTicketStatus().toString(),
                ticket.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        };

        return rowData;
    }

    public void updateTickets(List<Ticket> tickets){
        tableModel.setRowCount(0);

        for(Ticket ticket : tickets) addTicket(ticket);
    }
}
