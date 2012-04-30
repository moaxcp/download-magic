/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.task;

/**
 *
 * @author john
 */
public enum Status {

    QUEUED("Queued"),
    RUNNING("Running"),
    FINISHED("Finished"),
    STOPPED("Stopped"),
    ERROR("Error");

    private String str;

    private Status(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
