package org.atemsource.atem.impl.common.infrastructure;

import java.io.IOException;
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
	private CandidateResolver candidateResolver;

	public Collection<Class<?>> findClasses(String basePackage, CandidateResolver candidateResolver) throws IOException
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
				try
				{
					Class<?> clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
					if (candidateResolver == null || candidateResolver.isCandidate(clazz))
					{
						candidates.add(clazz);
					}
				}
				catch (ClassNotFoundException e)
				{
					MetaLogs.LOG.error("cannot find class for scanned meta data", e);
				}
			}
		}
		return candidates;
	}

	public CandidateResolver getCandidateResolver()
	{
		return candidateResolver;
	}

	private String resolveBasePackage(String basePackage)
	{
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	public void setCandidateResolver(CandidateResolver candidateResolver)
	{
		this.candidateResolver = candidateResolver;
	}

}
