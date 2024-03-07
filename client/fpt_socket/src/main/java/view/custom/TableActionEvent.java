package view.custom;

/**
 *
 * @author RAVEN
 */
public interface TableActionEvent {

    public void onRename(int row);

    public void onMove(int row);
    
    public void onDelete(int row);
    
    public void onDownload(int row);
    
    public void onShare(int row);
}
