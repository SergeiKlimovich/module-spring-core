package com.javaprogram.modulespringcore.util.postprocessor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import com.javaprogram.modulespringcore.models.Identifiable;
import com.javaprogram.modulespringcore.util.IdGenerator;
import com.javaprogram.modulespringcore.util.annotation.BindStaticData;
import com.javaprogram.modulespringcore.util.json.JsonMapper;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class BindStaticDataAnnotationBeanPostProcessor implements BeanPostProcessor {

    private JsonMapper jsonMapper;
    private IdGenerator generator;
    private String startInfoMessage;
    private String finishInfoMessage;

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            BindStaticData annotation = field.getAnnotation(BindStaticData.class);
            if (Objects.nonNull(annotation) && Map.class.isAssignableFrom(field.getType())) {
                LOG.info(startInfoMessage);
                JSONArray jsonArray = new JSONArray(readFileFromResources(annotation.fileLocation()));
                Map<Long, Identifiable> hashMap = instantiateCollectionFromJson(jsonArray, annotation.castTo());
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, hashMap);
                LOG.info(finishInfoMessage);
            }
        }
        return bean;
    }

    private <T extends Identifiable> Map<Long, Identifiable> instantiateCollectionFromJson(JSONArray jsonArray,
                                                                                           Class<T> clazz) {
        HashMap<Long, Identifiable> hashMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String jsonObjectAsString = jsonObject.toString();
            T entity = fromJson(clazz, jsonObjectAsString);
            entity.setId(generator.generateId(clazz));
            hashMap.put(entity.getId(), entity);
        }
        return hashMap;
    }

    private <T extends Identifiable> T fromJson(Class<T> clazz, String json) {
        return jsonMapper.fromJson(json, clazz);
    }

    private String readFileFromResources(String filename) {
        URL resource = BindStaticDataAnnotationBeanPostProcessor.class.getClassLoader().getResource(filename);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes);
    }
}
