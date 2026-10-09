package com.sponsorhub.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LightConverter implements AttributeConverter<Light, String>
{
    @Override
    public String convertToDatabaseColumn(Light light)
    {
        if (light == null)
        {
            return null;
        }
        return light.name().replace('_', ' ');
    }

    @Override
    public Light convertToEntityAttribute(String label)
    {
        if (label == null)
        {
            return null;
        }
        return Light.valueOf(label.replace(' ', '_'));
    }
}
