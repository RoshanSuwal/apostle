package com.ekbana.bigdata;

import com.ekbana.bigdata.plugin_api.factory.NotificationFactory;
import com.ekbana.bigdata.plugin_api.factory.ValidatorFactory;
import com.ekbana.bigdata.plugin_api.Plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public class PluginLoader {

    private final Map<String,NotificationFactory> notificationFactoryMap=new HashMap<>();
    private final Map<String,ValidatorFactory> validatorFactoryMap=new HashMap<>();
    private final File pluginsDir;
    private final AtomicBoolean loading = new AtomicBoolean();

    public PluginLoader(final File pluginsDir) {
        this.pluginsDir = pluginsDir;
    }

    public void loadPlugins() {
        if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
            System.err.println("Skipping Plugin Loading. Plugin dir not found: " + pluginsDir);
            return;
        }

        if (loading.compareAndSet(false, true)) {
            final File[] files = requireNonNull(pluginsDir.listFiles());
            for (File pluginDir : files) {
                System.out.println(pluginDir.getName());
                if (pluginDir.isDirectory()) {
                    loadPlugin(pluginDir);
                }
            }
        }
    }

    private void loadPlugin(final File pluginDir) {
        System.out.println("Loading plugin: " + pluginDir);
        final URLClassLoader pluginClassLoader = createPluginClassLoader(pluginDir);
        final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(pluginClassLoader);
            ServiceLoader<Plugin> plugins = ServiceLoader.load(Plugin.class, pluginClassLoader);
            for (Plugin plugin : plugins) {
                installPlugin(plugin);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    public void loadInBuiltPlugins(List<Plugin> inbuiltPlugins){
        System.out.println("Loading built-in plugins");
        for (Plugin plugin:inbuiltPlugins) installPlugin(plugin);
    }


    private void installPlugin(final Plugin plugin) {
        System.out.println("Installing plugin: " + plugin.getClass().getName());

//        registerMvcEndPoints(plugin);

        for (NotificationFactory notificationFactory:plugin.notificationFactories()){
            System.out.println("installing notification plugin : "+notificationFactory.identity());
            notificationFactoryMap.put(notificationFactory.identity(),notificationFactory);
        }

        for (ValidatorFactory validatorFactory:plugin.validatorFactories()){
            System.out.println("installing validator plugin : "+validatorFactory.identity());
            validatorFactoryMap.put(validatorFactory.identity(),validatorFactory);
        }
    }

    private URLClassLoader createPluginClassLoader(File dir) {
        File[] value = dir.listFiles();
        final URL[] urls = Arrays.stream(Optional.ofNullable(value).orElse(new File[]{}))
                .sorted()
                .map(File::toURI)
                .map(this::toUrl)
                .toArray(URL[]::new);

        return new PluginClassLoader(urls, getClass().getClassLoader());
    }

    private URL toUrl(final URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public NotificationFactory getNotificationFactory(String identity){
        return notificationFactoryMap.get(identity);
    }
    public ValidatorFactory getValidatorFactory(String identity){return validatorFactoryMap.get(identity);}

//    public void registerMvcEndPoints(Plugin plugin){
//        plugin.mvcControllers().stream().forEach(
//                controller->((ConfigurableBeanFactory)beanFactory).registerSingleton(controller.getClass().getName(),controller)
//        );
//
//        applicationContext.getBeansOfType(RequestMappingHandlerMapping.class)
//                .forEach((k,v)->{
//                    System.out.println(v.getClass().getSimpleName());
//                    v.afterPropertiesSet();
//                });
//    }
}
