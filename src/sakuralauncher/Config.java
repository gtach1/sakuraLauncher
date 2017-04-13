/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sakuralauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author t.isayama
 */
public class Config {

    // config.xml 配置ディレクトリ
    public static String getUserHome() {
        return System.getProperties().getProperty("user.home");
    }

    public final static String CONFIG_XML = "config.xml";
    private final List<LaunchItem> launcherList = new ArrayList<>();

    public void loadFromXML() {
        final Properties prop = new Properties();
        try {
            InputStream is = new FileInputStream(new File(getUserHome(), CONFIG_XML));
            prop.loadFromXML(is);
            this.launcherList.clear();
            int index = 0;
            while (true) {
                String caption = prop.getProperty(String.format("launch[%d].caption", index), null);
                String target = prop.getProperty(String.format("launch[%d].target", index), null);
                if (caption == null || target == null) {
                    break;
                }
                LaunchItem launchItem = new LaunchItem() {
                    private String caption;
                    private String target;

                    @Override
                    public void setCaption(String caption) {
                        this.caption = caption;
                    }

                    @Override
                    public String getCaption() {
                        return caption;
                    }

                    @Override
                    public void setTarget(String target) {
                        this.target = target;
                    }

                    @Override
                    public String getTerget() {
                        return target;
                    }
                };
                launchItem.setCaption(caption);
                launchItem.setTarget(target);
                this.launcherList.add(launchItem);
                index++;
            }
        } catch (IOException ex) {
            // configがなければデフォルトで
            this.launcherList.clear();
            LaunchItem launchItem = new LaunchItem() {
                private String caption;
                private String target;

                @Override
                public void setCaption(String caption) {
                    this.caption = caption;
                }

                @Override
                public String getCaption() {
                    return caption;
                }

                @Override
                public void setTarget(String target) {
                    this.target = target;
                }

                @Override
                public String getTerget() {
                    return target;
                }
            };
            launchItem.setCaption("Send to Sakura Editor");
            launchItem.setTarget("C:\\Program Files\\Sakura\\sakura.exe");
            this.launcherList.add(launchItem);
        }
    }

    public List<LaunchItem> getLauncherList() {
        return this.launcherList;
    }

    public interface LaunchItem {

        public void setCaption(String caption);

        public void setTarget(String target);

        public String getCaption();

        public String getTerget();
    }

}
