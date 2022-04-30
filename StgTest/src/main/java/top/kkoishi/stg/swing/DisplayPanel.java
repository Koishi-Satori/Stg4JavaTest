package top.kkoishi.stg.swing;

import top.kkoishi.stg.env.Stage;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Graphics;

/**
 * @author KKoishi_
 */
public final class DisplayPanel extends JPanel {
    private Stage stage;

    public Stage stage () {
        return stage;
    }

    public void setStage (Stage stage) {
        this.stage = stage;
    }

    public DisplayPanel (Stage stage) {
        super(new BorderLayout());
        this.stage = stage;
    }

    @Override
    public void paint (Graphics g) {
        System.out.println("Rendering stage...");
        if (stage != null) {
            g.drawImage(stage.getBackground(), 0, 0, null);
            System.out.println("Success to render!");
        }
    }
}
