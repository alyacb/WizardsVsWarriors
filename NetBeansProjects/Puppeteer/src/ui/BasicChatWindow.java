
package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.SwingUtilities.invokeLater;

/**
 *
 * @author alyacarina
 */
public class BasicChatWindow extends JFrame {
    private JButton submit;
    private TextArea messageInput, log;
    
    public BasicChatWindow(String newTitle){
        super(newTitle);
        initialize();
    }
    
    public BasicChatWindow(){
        super("Chat UI");
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setLayout(new BorderLayout());
        initializeInputPanel();
        initializeOutputPanel();
        
        setVisible(true);
    }

    private void initializeInputPanel() {
        Panel input = new Panel();
        input.setLayout(new FlowLayout());
        
        // Input textarea
        messageInput = new TextArea("", 1, 38, TextArea.SCROLLBARS_NONE);
        messageInput.addKeyListener(new EnterKeyListener() {
            @Override
            public void doThis(KeyEvent e) {
                submit.doClick();
            }
        });
        input.add(messageInput);
        
        // Submit button
        submit = new JButton("Submit");
        submit.addActionListener((ActionEvent e) -> {
            String message = messageInput.getText();
            if(!message.endsWith("\n")){
                message+="\n";
            }
            messageInput.setText("");
            log.append(message);            
        });
        input.add(submit);
        
        add("North", input);
    }

    private void initializeOutputPanel() {
        Panel output = new Panel();
        output.setLayout(new FlowLayout());
        
        // Output log
        log = new TextArea(
                "Your conversation...\n", 30, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
        log.setEditable(false);
        output.add(log);
        
        add("Center", output);
    }
    
    public static void main(String[] args){
        invokeLater(() -> {
            BasicChatWindow chatte = new BasicChatWindow("Testing... 1,2,3...");
        });
    }
}
