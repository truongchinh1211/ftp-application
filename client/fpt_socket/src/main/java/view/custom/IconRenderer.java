/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.custom;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Son
 */
 public class IconRenderer extends DefaultCellEditor implements TableCellRenderer {
        private JLabel label;
        private JButton button;

        public IconRenderer() {
            super(new JCheckBox());
            label = new JLabel();
            button = new JButton();

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Icon) {
                ImageIcon icon = (ImageIcon) value;
                Image img = icon.getImage().getScaledInstance(40, 60, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
                label.setIcon(icon);
                return label;
            } else {
                button.setText(value == null ? "" : value.toString());
                return button;
            }
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Icon) {
                ImageIcon icon = (ImageIcon) value;
                Image img = icon.getImage().getScaledInstance(40, 60, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
                button.setIcon(icon);
                return button;
            } else {
                button.setText(value == null ? "" : value.toString());
                return button;
            }
        }
    }
