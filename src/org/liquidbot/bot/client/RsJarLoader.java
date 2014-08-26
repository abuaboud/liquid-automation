package org.liquidbot.bot.client;

import org.liquidbot.bot.client.injection.Injector;
import org.liquidbot.bot.client.injection.ModelInjector;
import org.liquidbot.bot.client.injection.ObjectDefinitionInjector;
import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.script.api.wrappers.definitions.ObjectDefinition;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 7/2/14
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class RsJarLoader extends ClassLoader {


	private Hashtable<String, byte[]> entries;
	private Hashtable<String, Class<?>> classes;
	private URL gamepackUrl;

	private Injector[] injectors = {new ModelInjector(), new ObjectDefinitionInjector()};

	public RsJarLoader(URL url) {
		gamepackUrl = url;
		entries = new Hashtable<>();
		classes = new Hashtable<>();
		loadJar();
	}

	@Override
	public Class<?> loadClass(String name, boolean resolve) {
		try {
			if (entries.containsKey(name)) {
				byte[] value = entries.get(name);
				ClassReader cr = new ClassReader(value);
				ClassNode classNode = new ClassNode();
				cr.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES) {
					@Override
					protected String getCommonSuperClass(String one, String two) {
						return "java/lang/Object";
					}
				};
				for (Injector injector : injectors) {
					if (injector.canRun(classNode)) {
						injector.run(classNode);
					}
				}
				classNode.accept(cw);
				value = cw.toByteArray();
				Class<?> clazz = defineClass(name, value, 0, value.length);
				if (!classes.containsKey(clazz)) {
					classes.put(name, clazz);
				}
				return clazz;
			}
			return super.loadClass(name, resolve);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public Hashtable<String, Class<?>> classes() {
		return classes;
	}

	private void loadJar() {
		try {
			JarInputStream jis = new JarInputStream(gamepackUrl.openStream());
			JarEntry entry;
			while ((entry = jis.getNextJarEntry()) != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] data = new byte[1024];
				int read;
				while ((read = jis.read(data, 0, 1024)) > 0) {
					bos.write(data, 0, read);
				}


				entries.put(entry.getName().replace(".class", ""), bos.toByteArray());
				int percent = (entries.size() * 100) / 201;
				bos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}