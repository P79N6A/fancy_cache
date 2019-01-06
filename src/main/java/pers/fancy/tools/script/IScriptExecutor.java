package pers.fancy.tools.script;


import javax.script.ScriptException;
import java.util.Map;


public interface IScriptExecutor {

    Map<String, String> context() throws ScriptException;

    Object execute(String script, ScriptType type, String salt) throws ScriptException;

    String reloadContext() throws ScriptException;

    void registerContext(String name, Object value) throws ScriptException;
}
