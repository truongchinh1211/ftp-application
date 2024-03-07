/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import bus.FileBus;
import config.AppConfig;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.FormatUtils;
import view.custom.IconRenderer;
import view.custom.TableActionCellEditor;
import view.custom.TableActionCellRender;
import view.custom.TableActionEvent;

/**
 *
 * @author Son
 */
public class Folder extends javax.swing.JPanel {
    private FileBus fileBus;
    private Stack<String> pathHistory = new Stack<>();
    private String shareFileName;
    /**
     * Creates new form folder
     */
    public Folder() throws Exception {
        initComponents();
        setTable();
        fileBus=  new FileBus();
        pathHistory.push(AppConfig.SERVER_FTP_FILE_PATH);
        getAllFile();
        SwingUtilities.invokeLater(() -> {
            parentFrame = (MainLayout) SwingUtilities.getWindowAncestor(this);
        });
    }
    public void setTable(){
        table.setTableHeader(null);               
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void grantPermission(int row) {
                try{
                    if(pathHistory.size()==1)
                        return;
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String filepath = pathHistory.peek()+"/"+model.getValueAt(row,1 ).toString();
                    File file = new File(filepath);
                    String type = file.isDirectory()?"dir":"file";
                    ShareOptionPane shareOptionPane = new ShareOptionPane(parentFrame,type,filepath);
                    shareOptionPane.setVisible(true);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            

        };
        table.getColumnModel().getColumn(0).setCellRenderer(new IconRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        table.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event));
        table.setDefaultRenderer(Object.class, (JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) -> {
            Component component = new DefaultTableCellRenderer().getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
            if (isSelected == false ) {
            component.setBackground(new java.awt.Color(255,255,255));
            }
            if(column==3){
                ((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
            }
            return component;
        });
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[] row = new Object[]{new ImageIcon(getClass().getResource("/view/img/fileIcon/folder.png")),"Row 2","","",""};
        model.addRow(row);
    }
    
    public void getAllFile() throws Exception{
        pathTitle.setText(pathHistory.peek());
        String fileList = fileBus.listAllFilesInStringFormat(pathHistory.peek());
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        if(!fileList.isBlank()){
            String[] lines = fileList.split("\n");
            for(String line : lines){
                if(!line.isBlank()){
                String[] parts = line.split(";");
                String type = parts[0].split("=")[1].trim();
                String owner =parts[1].split("=")[1].trim();
                String modify=parts[2].split("=")[1].trim();
                String size = parts[3].split("=")[1].trim();
                String perm = parts[4].split("=")[1].trim();
                String name = URLDecoder.decode(parts[5],"UTF-8").trim();
                modify = FormatUtils.convertTimestamp(modify);
                ImageIcon img;
                try {
                    if (type.equals("dir")) {
                        img = new ImageIcon(getClass().getResource("/view/img/fileIcon/folder.png"));
                    } else {
                        img = new ImageIcon(getClass().getResource("/view/img/fileIcon/" + name.split("\\.")[1] + ".png"));
                    }
                } catch (Exception ex) {
                    img = new ImageIcon(getClass().getResource("/view/img/fileIcon/txt.png"));
                }
                Object[] row = new Object[]{img,name,owner.equals("null")?"Tôi":owner,modify,FormatUtils.convertBytes(size)};
                // chia đơn vị mb,kg,Gb ******
                model.addRow(row);
                }
            }
        }
    }   
    public void enterFolder(String filepath){
        try {
                    // nếu row được chọn là thư mục
                    pathHistory.push(filepath);
                    getAllFile();
                } catch (Exception ex) {
                    Logger.getLogger(Folder.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        renamePanel = new javax.swing.JPanel();
        renameConfirm = new view.custom.Button();
        renameCancel = new view.custom.Button();
        renameTitle = new javax.swing.JLabel();
        renameField1 = new view.custom.textField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        renameField2 = new view.custom.textField();
        jLabel12 = new javax.swing.JLabel();
        birthdateField = new de.wannawork.jcalendar.JCalendarComboBox();
        jLabel25 = new javax.swing.JLabel();
        male = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        jLabel26 = new javax.swing.JLabel();
        emailField = new view.custom.textField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        usedField = new view.custom.textField();
        jLabel28 = new javax.swing.JLabel();
        quotaField = new view.custom.textField();
        jLabel29 = new javax.swing.JLabel();
        maxuploadField = new view.custom.textField();
        jLabel30 = new javax.swing.JLabel();
        maxdownloadField = new view.custom.textField();
        jLabel31 = new javax.swing.JLabel();
        downloadRb = new javax.swing.JRadioButton();
        uploadRb = new javax.swing.JRadioButton();
        anonymousRb = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        highlightPanel5 = new view.custom.HighlightPanel();
        jLabel34 = new javax.swing.JLabel();
        highlightPanel6 = new view.custom.HighlightPanel();
        pathTitle = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        renamePanel.setBackground(new java.awt.Color(255, 255, 255));
        renamePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(216, 216, 216)));

        renameConfirm.setText("Xác nhận");
        renameConfirm.setColor(new java.awt.Color(204, 204, 255));
        renameConfirm.setColorClick(new java.awt.Color(153, 153, 153));
        renameConfirm.setColorOver(new java.awt.Color(102, 102, 102));
        renameConfirm.setRadius(10);
        renameConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameConfirmActionPerformed(evt);
            }
        });

        renameCancel.setText("Hủy");
        renameCancel.setColor(new java.awt.Color(204, 204, 255));
        renameCancel.setColorClick(new java.awt.Color(153, 153, 153));
        renameCancel.setColorOver(new java.awt.Color(102, 102, 102));
        renameCancel.setRadius(10);
        renameCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameCancelActionPerformed(evt);
            }
        });

        renameTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        renameTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        renameTitle.setText("Thông tin cá nhân");

        renameField1.setFocusable(false);
        renameField1.setLabelText("");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Họ:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tên:");

        renameField2.setLabelText("");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Ngày sinh:");

        birthdateField.setEnabled(false);
        birthdateField.setFocusable(false);
        birthdateField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Giới tính:");

        male.setText("Nam");
        male.setEnabled(false);
        male.setFocusPainted(false);
        male.setFocusable(false);
        male.setRequestFocusEnabled(false);

        female.setText("Nữ");
        female.setEnabled(false);
        female.setFocusPainted(false);
        female.setFocusable(false);
        female.setRolloverEnabled(false);

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("Email:");

        emailField.setFocusable(false);
        emailField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        emailField.setLabelText("");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("Bộ nhớ sử dụng:");

        usedField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        usedField.setFocusable(false);
        usedField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usedField.setLabelText("");
        usedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usedFieldActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("/");

        quotaField.setFocusable(false);
        quotaField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        quotaField.setLabelText("");
        quotaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quotaFieldActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setText("Dung lượng upload tối đa: ");

        maxuploadField.setFocusable(false);
        maxuploadField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        maxuploadField.setLabelText("");
        maxuploadField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxuploadFieldActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setText("Dung lượng download tối đa: ");

        maxdownloadField.setFocusable(false);
        maxdownloadField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        maxdownloadField.setLabelText("");
        maxdownloadField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxdownloadFieldActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel31.setText("Quyền:");

        downloadRb.setText("Download");
        downloadRb.setEnabled(false);
        downloadRb.setFocusable(false);

        uploadRb.setText("upload");
        uploadRb.setEnabled(false);
        uploadRb.setFocusable(false);

        anonymousRb.setText("anonymous");
        anonymousRb.setEnabled(false);
        anonymousRb.setFocusable(false);

        javax.swing.GroupLayout renamePanelLayout = new javax.swing.GroupLayout(renamePanel);
        renamePanel.setLayout(renamePanelLayout);
        renamePanelLayout.setHorizontalGroup(
            renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(renamePanelLayout.createSequentialGroup()
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(renamePanelLayout.createSequentialGroup()
                        .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(renamePanelLayout.createSequentialGroup()
                                    .addGap(94, 94, 94)
                                    .addComponent(renameTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, renamePanelLayout.createSequentialGroup()
                                    .addGap(156, 156, 156)
                                    .addComponent(renameConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(renameCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(renamePanelLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(renamePanelLayout.createSequentialGroup()
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(usedField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(quotaField, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(renamePanelLayout.createSequentialGroup()
                                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(renamePanelLayout.createSequentialGroup()
                                            .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(renamePanelLayout.createSequentialGroup()
                                                    .addComponent(jLabel12)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(birthdateField, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                                                .addGroup(renamePanelLayout.createSequentialGroup()
                                                    .addComponent(jLabel1)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(renameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel2)))
                                            .addGap(18, 18, 18)
                                            .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(renameField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, renamePanelLayout.createSequentialGroup()
                                                    .addComponent(jLabel25)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(male)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(female)))))
                                    .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(renamePanelLayout.createSequentialGroup()
                                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(maxdownloadField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(renamePanelLayout.createSequentialGroup()
                                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(33, 33, 33)
                                            .addComponent(maxuploadField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(renamePanelLayout.createSequentialGroup()
                                        .addComponent(jLabel31)
                                        .addGap(28, 28, 28)
                                        .addComponent(downloadRb)
                                        .addGap(18, 18, 18)
                                        .addComponent(uploadRb)
                                        .addGap(18, 18, 18)
                                        .addComponent(anonymousRb)))))
                        .addGap(0, 40, Short.MAX_VALUE))
                    .addGroup(renamePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1)))
                .addContainerGap())
        );
        renamePanelLayout.setVerticalGroup(
            renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(renamePanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(renameTitle)
                .addGap(18, 18, 18)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(renameField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(renameField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(birthdateField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(male)
                        .addComponent(female)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quotaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxuploadField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxdownloadField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(downloadRb)
                    .addComponent(uploadRb)
                    .addComponent(anonymousRb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(renamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(renameCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(renameConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel8jPanel1MousePressed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel35.setText("Tên");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel36.setText("Chủ sở hữu");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel37.setText("Ngày sửa đổi");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel38.setText("Kích cỡ tệp");

        highlightPanel5.setBackground(new java.awt.Color(204, 204, 255));
        highlightPanel5.setColor(new java.awt.Color(204, 204, 255));
        highlightPanel5.setColorClick(new java.awt.Color(153, 153, 153));
        highlightPanel5.setColorOver(new java.awt.Color(204, 204, 204));
        highlightPanel5.setPreferredSize(new java.awt.Dimension(171, 40));
        highlightPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                highlightPanel5MouseClicked(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("< quay lại");

        javax.swing.GroupLayout highlightPanel5Layout = new javax.swing.GroupLayout(highlightPanel5);
        highlightPanel5.setLayout(highlightPanel5Layout);
        highlightPanel5Layout.setHorizontalGroup(
            highlightPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(highlightPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );
        highlightPanel5Layout.setVerticalGroup(
            highlightPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(highlightPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        highlightPanel6.setBackground(new java.awt.Color(204, 204, 204));
        highlightPanel6.setColor(new java.awt.Color(204, 204, 204));
        highlightPanel6.setColorClick(new java.awt.Color(153, 153, 153));
        highlightPanel6.setColorOver(new java.awt.Color(204, 204, 204));
        highlightPanel6.setPreferredSize(new java.awt.Dimension(171, 40));
        highlightPanel6.setRadius(10);
        highlightPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                highlightPanel6MouseClicked(evt);
            }
        });

        pathTitle.setFont(new java.awt.Font("Constantia", 1, 18)); // NOI18N
        pathTitle.setText("/users");
        pathTitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout highlightPanel6Layout = new javax.swing.GroupLayout(highlightPanel6);
        highlightPanel6.setLayout(highlightPanel6Layout);
        highlightPanel6Layout.setHorizontalGroup(
            highlightPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(highlightPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pathTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        highlightPanel6Layout.setVerticalGroup(
            highlightPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(highlightPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pathTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator9)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(336, 336, 336)
                        .addComponent(jLabel36)
                        .addGap(285, 285, 285)
                        .addComponent(jLabel37)
                        .addGap(82, 82, 82)
                        .addComponent(jLabel38)
                        .addContainerGap(285, Short.MAX_VALUE))))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(highlightPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(highlightPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(highlightPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(highlightPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(3, 3, 3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane3.setBorder(null);
        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jScrollPane3MousePressed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "Example1", "Chinh", "30 thg 9, 2023", "69 bytes", null, null},
                {null, "Example2", "Chinh", "30 thg 9, 2023", "69 bytes", null, null},
                {null, "Example3", "Chinh", "30 thg 9, 2023", "69 bytes", null, null},
                {null, "Example4", "Chinh", "30 thg 9, 2023", "69 bytes", null, null}
            },
            new String [] {
                "", "Tên", "Chủ sở hữu", "Ngày sửa đổi", "Kích thước tệp", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setFillsViewportHeight(true);
        table.setFocusable(false);
        table.setGridColor(new java.awt.Color(216, 216, 216));
        table.setRowHeight(60);
        table.setSelectionBackground(new java.awt.Color(204, 204, 204));
        table.setSelectionForeground(new java.awt.Color(51, 51, 51));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMinWidth(40);
            table.getColumnModel().getColumn(0).setMaxWidth(40);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(150);
            table.getColumnModel().getColumn(3).setPreferredWidth(150);
            table.getColumnModel().getColumn(4).setPreferredWidth(100);
            table.getColumnModel().getColumn(5).setMinWidth(0);
            table.getColumnModel().getColumn(5).setMaxWidth(0);
            table.getColumnModel().getColumn(6).setPreferredWidth(10);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void highlightPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_highlightPanel5MouseClicked
        try {
            if(pathHistory.size()>1)
                pathHistory.pop();
            getAllFile();
        } catch (Exception ex) {
            Logger.getLogger(Folder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_highlightPanel5MouseClicked

    private void jPanel8jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8jPanel1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel8jPanel1MousePressed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        if (evt.getClickCount() == 2) {
            JTable target = (JTable) evt.getSource();
            int row = target.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String filepath = pathHistory.peek()+"/"+model.getValueAt(row,1 ).toString();
            System.out.println(filepath);
            File file = new File(filepath);
            if(file.isDirectory()) {
                enterFolder(filepath);
            }else{
                
            }
        }
    }//GEN-LAST:event_tableMouseClicked

    private void tableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableMousePressed

    private void jScrollPane3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane3MousePressed

    private void renameConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameConfirmActionPerformed
   
    }//GEN-LAST:event_renameConfirmActionPerformed

    private void renameCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameCancelActionPerformed

    }//GEN-LAST:event_renameCancelActionPerformed

    private void usedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usedFieldActionPerformed

    private void quotaFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quotaFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quotaFieldActionPerformed

    private void maxuploadFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxuploadFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxuploadFieldActionPerformed

    private void maxdownloadFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxdownloadFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxdownloadFieldActionPerformed

    private void highlightPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_highlightPanel6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_highlightPanel6MouseClicked

    private MainLayout parentFrame;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton anonymousRb;
    private de.wannawork.jcalendar.JCalendarComboBox birthdateField;
    private javax.swing.JRadioButton downloadRb;
    private view.custom.textField emailField;
    private javax.swing.JRadioButton female;
    private view.custom.HighlightPanel highlightPanel5;
    private view.custom.HighlightPanel highlightPanel6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JRadioButton male;
    private view.custom.textField maxdownloadField;
    private view.custom.textField maxuploadField;
    private javax.swing.JLabel pathTitle;
    private view.custom.textField quotaField;
    private view.custom.Button renameCancel;
    private view.custom.Button renameConfirm;
    private view.custom.textField renameField1;
    private view.custom.textField renameField2;
    private javax.swing.JPanel renamePanel;
    private javax.swing.JLabel renameTitle;
    private javax.swing.JTable table;
    private javax.swing.JRadioButton uploadRb;
    private view.custom.textField usedField;
    // End of variables declaration//GEN-END:variables
}
