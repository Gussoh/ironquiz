/*
 * ServerConfig.java
 *
 * Created on den 24 november 2006, 16:05
 */

package quizgame.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author rheo
 */
public class ServerConfig implements Serializable {
    
    private IniEditor ini;
    private boolean hasChanged;
    private static ServerConfig instance = new ServerConfig();
    private String currentSection;
    
    private ServerConfig() {
        ini = new IniEditor(true);
        File file = null;
        try {
            file = new File("config.ini");
            if(!file.exists()) {
                file.createNewFile();
            }
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
            ini.load(reader);
            reader.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        checkSection("Gameboard");
        checkOption("charset", "ISO-8859-1");
        checkOption("boardDirectory", "boards");
        checkOption("categoryMark", "#");
        checkOption("questionDelimiter", ";");
        
        checkSection("Use account-file");
        checkOption("ADMIN", "true");
        checkOption("PULPIT", "true");
        checkOption("MAIN_SCREEN", "true");
        checkOption("TYPER", "false");
        checkOption("accountFile", "accounts.ini");
        checkOption("loadAtStartup", "true");
        checkOption("charset", "ISO-8859-1");
        checkOption("SHA-Encrypted", "false");
        
        
        if(hasChanged) {
            try {
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");
                ini.save(writer);
                writer.close();
                
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkOption(String option, String value) { 
        if(!ini.hasOption(currentSection, option)) {
            ini.set(currentSection, option, value);
            hasChanged = true;
        }
    }

    private void checkSection(String section) {
        if(!ini.hasSection(section)) {
            ini.addSection(section);
            hasChanged = true;
        }
        currentSection = section;
    }
    
    public static ServerConfig getInstance() {
        return instance;
    }
    
    public String getString(String section, String option) {
        return ini.get(section, option);
    }
    
    public int getInt(String section, String option) {
        return Integer.parseInt(ini.get(section, option));
    }
    
    public boolean getBoolean(String section, String option) {
        return Boolean.parseBoolean(ini.get(section, option));
    }
    
    public double getDouble(String section, String option) {
        return Double.parseDouble(ini.get(section, option));
    }
}
