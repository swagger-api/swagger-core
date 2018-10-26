package io.swagger.v3.core.converter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;

import io.swagger.v3.oas.models.media.Schema;

public class ModelConverterContextImpl implements ModelConverterContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelConverterContextImpl.class);

	private final List<ModelConverter> converters;
	private final Map<String, Schema> modelByName;
	private final HashMap<AnnotatedType, Schema> modelByType;
	private final Set<AnnotatedType> processedTypes;

	public ModelConverterContextImpl(List<ModelConverter> converters) {

		this.converters = converters;
		modelByName = new TreeMap<>();
		modelByType = new HashMap<>();
		processedTypes = new HashSet<>();
	}

	public ModelConverterContextImpl(ModelConverter converter) {

		this(new ArrayList<ModelConverter>());
		converters.add(converter);
	}

	public Iterator<ModelConverter> getConverters() {

		return converters.iterator();
	}

	@Override
	public void defineModel(String name, Schema model) {

		AnnotatedType aType = null;
		defineModel(name, model, aType, null);
	}

	@Override
	public void defineModel(String name, Schema model, Type type, String prevName) {

		defineModel(name, model, new AnnotatedType().type(type), prevName);
	}

	@Override
	public void defineModel(String name, Schema model, AnnotatedType type, String prevName) {

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(String.format("defineModel %s %s", name, model));
		}
		modelByName.put(name, model);

		if (StringUtils.isNotBlank(prevName) && !prevName.equals(name)) {
			modelByName.remove(prevName);
		}

		if (type != null && type.getType() != null) {
			modelByType.put(type, model);
		}
	}

	@Override
	public Map<String, Schema> getDefinedModels() {

		return Collections.unmodifiableMap(modelByName);
	}

	@Override
	public Schema resolve(AnnotatedType type) {

		if (processedTypes.contains(type)) {
			Schema cachedSchema = modelByType.get(type);
			if (JavaType.class.isAssignableFrom(type.getType().getClass())) {
				JavaType jType = (JavaType) type.getType();
				if (jType.isMapLikeType() && cachedSchema != null) {

					return clone(cachedSchema);
				}
			}

			return cachedSchema;
		} else {
			processedTypes.add(type);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("resolve %s", type.getType()));
		}
		Iterator<ModelConverter> converters = getConverters();
		Schema resolved = null;
		if (converters.hasNext()) {
			ModelConverter converter = converters.next();
			LOGGER.trace("trying extension " + converter);
			resolved = converter.resolve(type, this, converters);
		}
		if (resolved != null) {
			modelByType.put(type, resolved);

			Schema resolvedImpl = resolved;
			if (resolvedImpl.getName() != null) {
				modelByName.put(resolvedImpl.getName(), resolved);
			}
		} else {
			processedTypes.remove(type);
		}

		return resolved;
	}

	private Schema clone(Schema cachedSchema) {

		Schema copy = new Schema<>();
		copy.set$ref(cachedSchema.get$ref());
		copy.setAdditionalProperties(cachedSchema.getAdditionalProperties());
		copy.setDefault(cachedSchema.getDefault());
		copy.setDeprecated(cachedSchema.getDeprecated());
		copy.setDescription(cachedSchema.getDescription());
		copy.setDiscriminator(cachedSchema.getDiscriminator());
		copy.setEnum(cachedSchema.getEnum());
		copy.setExample(cachedSchema.getExample());
		copy.setExclusiveMaximum(cachedSchema.getExclusiveMaximum());
		copy.setExclusiveMinimum(cachedSchema.getExclusiveMinimum());
		copy.setExtensions(cachedSchema.getExtensions());
		copy.setExternalDocs(cachedSchema.getExternalDocs());
		copy.setFormat(cachedSchema.getFormat());
		copy.setMaximum(cachedSchema.getMaximum());
		copy.setMaxItems(cachedSchema.getMaxItems());
		copy.setMaxLength(cachedSchema.getMaxLength());
		copy.setMaxProperties(cachedSchema.getMaxProperties());
		copy.setMinimum(cachedSchema.getMinimum());
		copy.setMinItems(cachedSchema.getMinItems());
		copy.setMinLength(cachedSchema.getMinLength());
		copy.setMinProperties(cachedSchema.getMinProperties());
		copy.setMultipleOf(cachedSchema.getMultipleOf());
		copy.setName(cachedSchema.getName());
		copy.setNot(cachedSchema.getNot());
		copy.setNullable(cachedSchema.getNullable());
		copy.setPattern(cachedSchema.getPattern());
		copy.setProperties(cachedSchema.getProperties());
		copy.setReadOnly(cachedSchema.getReadOnly());
		copy.setRequired(cachedSchema.getRequired());
		copy.setTitle(cachedSchema.getTitle());
		copy.setType(cachedSchema.getType());
		copy.setUniqueItems(cachedSchema.getUniqueItems());
		copy.setWriteOnly(cachedSchema.getWriteOnly());
		copy.setXml(cachedSchema.getXml());
		return copy;
	}
}
