package com.example.demo.controller;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class DemoController {

    /**
     * 使用GroovyClassLoader动态地载入Groovy的类
     * @param id
     * @return
     */
    @GetMapping(value = "/test1/{id}")
    public String myTest1(@PathVariable String id){
        try{
            //通过JAVA来加载如一个groovy脚本文件，然后调用该脚本中的方法
            ClassLoader parent = ClassLoader.getSystemClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);
            Class groovyClass = loader.parseClass(
                    new File("src/main/java/com/example/demo/groovyscript/"+id+".groovy")
            );

            GroovyObject groovyObject= (GroovyObject)groovyClass.newInstance();

            String result = (String) groovyObject.invokeMethod("test", id);
            return result;
        }
        catch (Exception ex){

        }
        return "error";
    }

    /**
     * 使用GroovyScriptEngine脚本引擎加载Groovy脚本
     * @param id
     * @return
     */
    @GetMapping(value = "/test2/{id}")
    public String myTest2(@PathVariable String id){
        try{
            //通过设定CLASSPATH来初始化groovy脚本引擎，可以运行该path下的任何groovy脚本文件了
            String path= "src/main/java/com/example/demo/groovyscript";
            GroovyScriptEngine engine = new GroovyScriptEngine(path);
            Script script = engine.createScript(id+".groovy", new Binding());
            return (String) script.invokeMethod("test",id);
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

    /**
     * 使用GroovyScriptEngine脚本引擎加载Groovy脚本
     * @param id
     * @return
     */
    @GetMapping(value = "/test3/{id}")
    public String myTest3(@PathVariable String id){
        try{
            //通过设定CLASSPATH来初始化groovy脚本引擎，可以运行该path下的任何groovy脚本文件了
            String path="src/main/java/com/example/demo/groovyscript";
            GroovyScriptEngine engine = new GroovyScriptEngine(path);

            Binding binding = new Binding();
            binding.setVariable("id",id);
            engine.run(id+".groovy",binding);
            return binding.getVariable("output").toString();
        }
        catch (Exception ex){
            return ex.toString();
        }
    }

} 