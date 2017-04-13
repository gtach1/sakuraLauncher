/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp;

import sakuralauncher.SendToLauncher;
import sakuralauncher.Config;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;

/**
 *
 * @author t.isayama
 */
public class BurpExtender implements IBurpExtender, IContextMenuFactory, IHttpListener, IProxyListener {

    private final Config config = new Config();

    @Override
    /* IBurpExtender interface implements method */
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        // 設定の読み込み
        this.config.loadFromXML();
        // コンテキストメニューの登録
        callbacks.registerContextMenuFactory(this);
        callbacks.registerHttpListener(this);
        callbacks.registerProxyListener(this);
    }

    private final List menuList = new ArrayList<>();

    @Override
    /* IContextMenuFactory interface implements method */
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        menuList.clear();
        List<Config.LaunchItem> launchItems = this.config.getLauncherList();
        for (Config.LaunchItem item : launchItems) {
            SendToLauncher launch = new SendToLauncher(item);
            menuList.add(launch.createMenuItem(invocation));
        }
        return menuList;
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {

    }

    @Override
    public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message) {

    }

}
