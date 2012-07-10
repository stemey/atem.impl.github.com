package org.atemsource.atem.impl.common.infrastructure;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

import org.atemsource.atem.impl.MetaLogs;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;


@Component
public class ClasspathScanner
{
	public Collection<Class<?>> findClasses(String basePackage, Class<? extends Annotation>... a) throws IOException
	{
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		Collection<Class<?>> candidates = new ArrayList<Class<?>>();
		String packageSearchPath =
			ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + "/" + "**/*.class";
		Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
		for (Resource resource : resources)
		{
			if (resource.isReadable())
			{
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				if (isCandidate(metadataReader, a))
				{
					try
					{
						candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
					}
					catch (ClassNotFoundException e)
					{
						MetaLogs.LOG.error("cannort find class for scanned metadata", e);
					}
				}
			}
		}
		return candidates;
	}

	private boolean isCandidate(MetadataReader metadataReader, Class<? extends Annotation>[] annotationTypes)
	{
		if (annotationTypes.length == 0)
		{
			return true;
		}
		Class<?> c;
		try
		{
			c = Class.forName(metadataReader.getClassMetadata().getClassName());
			for (Class<? extends Annotation> annotationType : annotationTypes)
			{
				if (c.isAnnotationPresent(annotationType))
				{
					return true;
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			MetaLogs.LOG.error("cannot find class for scanned meta data", e);
		}
		return false;
	}

	private String resolveBasePackage(String basePackage)
	{
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

}
