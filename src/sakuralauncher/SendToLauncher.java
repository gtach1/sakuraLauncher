/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sakuralauncher;

import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JMenuItem;

/**
 *
 * @author t.isayama
 */
public class SendToLauncher implements Config.LaunchItem {

    private final Config.LaunchItem launchItem;

    public SendToLauncher(Config.LaunchItem launchItem) {
        this.launchItem = launchItem;
    }

    public String getCaption() {
        return this.launchItem.getCaption();
    }

    public void setCaption(String caption) {
        this.launchItem.setCaption(caption);
    }

    public String getTerget() {
        return this.launchItem.getTerget();
    }

    public void setTarget(String target) {
        this.launchItem.setTarget(target);
    }

    public JMenuItem createMenuItem(IContextMenuInvocation invocation) {
        final IContextMenuInvocation contextMenuInvocation = invocation;
        javax.swing.JMenuItem menuItem = new javax.swing.JMenuItem();
        menuItem.setText(this.getCaption());
        menuItem.addActionListener(new ActionListener() {

            // メニュークリック時
            @Override
            public void actionPerformed(ActionEvent e) {
                IHttpRequestResponse[] messageInfo = contextMenuInvocation.getSelectedMessages();
                if (messageInfo.length > 0) {
                    try {
                        File[] tmpFiles = new File[messageInfo.length];
                        for (int i = 0; i < messageInfo.length; i++) {
                            tmpFiles[i] = tempMessageFile(messageInfo[i], i);
                        }
                        String[] args = new String[tmpFiles.length];
                        for (int i = 0; i < args.length; i++) {
                            args[i] = tmpFiles[i].toString();
                        }
                        executeProcess(getTerget(), args);
                    } catch (IOException ex) {
                    }
                }
            }
        });
        return menuItem;
    }

    // プロセスの起動
    public static Process executeProcess(String target, String args[]) throws IOException {
        List<String> list = new ArrayList<>(Arrays.asList(args));
        list.add(0, target);
        return Runtime.getRuntime().exec((String[]) list.toArray(new String[0]));
    }

    // テンポラリーファイルの作成
    public static File tempMessageFile(IHttpRequestResponse messageInfo, int index) throws IOException {
        File file = File.createTempFile(messageInfo.hashCode() + "." + index + ".", ".tmp");
        file.deleteOnExit();
        try (FileOutputStream fostm = new FileOutputStream(file, true)) {
            if (messageInfo.getRequest() != null) {
                fostm.write(messageInfo.getRequest());
            }
            fostm.write(new byte[]{0x0d, 0x0a});
            if (messageInfo.getResponse() != null) {
                fostm.write(messageInfo.getResponse());
            }
        }
        return file;
    }

}
